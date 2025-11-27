/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/repositories/UserPreferencesRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Repositório que gerencia as preferências do usuário, como tema e estado de sincronização, usando o DataStore.
*
*/
package com.marin.catfeina.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.marin.catfeina.data.sync.AppUpdateDto
import com.marin.core.tema.ChaveTema
import com.marin.core.tema.ModoNoturno
import com.marin.core.tema.PreferenciasDeTemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : PreferenciasDeTemaRepository {

    private object Keys {
        // Tema e UI
        val TEMA_CHAVE = stringPreferencesKey("ui_theme_key")
        val MODO_NOTURNO = stringPreferencesKey("ui_night_mode") // Chave atualizada para String

        // Sincronização
        fun moduloVersao(nomeModulo: String) = intPreferencesKey("sync_version_${nomeModulo}")
        val APP_UPDATE_INFO = stringPreferencesKey("sync_app_update_info")
    }

    override val chaveTema: Flow<ChaveTema> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val nomeTema = preferences[Keys.TEMA_CHAVE] ?: ChaveTema.PRIMAVERA.name
            try {
                ChaveTema.valueOf(nomeTema)
            } catch (e: IllegalArgumentException) {
                ChaveTema.PRIMAVERA
            }
        }

    override val modoNoturno: Flow<ModoNoturno> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val nomeModo = preferences[Keys.MODO_NOTURNO] ?: ModoNoturno.SISTEMA.name
            try {
                ModoNoturno.valueOf(nomeModo)
            } catch (e: IllegalArgumentException) {
                ModoNoturno.SISTEMA // Fallback seguro
            }
        }

    override suspend fun setChaveTema(chave: ChaveTema) {
        dataStore.edit { it[Keys.TEMA_CHAVE] = chave.name }
    }

    override suspend fun setModoNoturno(modo: ModoNoturno) {
        dataStore.edit { it[Keys.MODO_NOTURNO] = modo.name }
    }

    fun getModuloVersao(nomeModulo: String): Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[Keys.moduloVersao(nomeModulo)] ?: 0 }

    suspend fun updateModuloVersao(nomeModulo: String, novaVersao: Int) {
        dataStore.edit { it[Keys.moduloVersao(nomeModulo)] = novaVersao }
    }

    suspend fun setAppUpdateInfo(appUpdate: AppUpdateDto) {
        val appUpdateJson = json.encodeToString(AppUpdateDto.serializer(), appUpdate)
        dataStore.edit { it[Keys.APP_UPDATE_INFO] = appUpdateJson }
    }

    val appUpdateInfo: Flow<AppUpdateDto?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val jsonString = preferences[Keys.APP_UPDATE_INFO]
            if (jsonString.isNullOrBlank()) null else {
                try {
                    json.decodeFromString(AppUpdateDto.serializer(), jsonString)
                } catch (e: Exception) {
                    null // Retorna nulo se a desserialização falhar
                }
            }
        }
}
