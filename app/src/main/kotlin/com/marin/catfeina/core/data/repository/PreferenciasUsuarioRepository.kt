/*
 *  Projeto: Catfeina
 *  Arquivo: PreferenciasUsuarioRepository.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota:
 *
 */

package com.marin.catfeina.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenciasUsuarioRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    // Flow que emite o valor do timestamp sempre que ele muda. O valor padrão é 0L.
    val ultimaSincronizacao: Flow<Long> = dataStore.data.map {
        it[Keys.ULTIMA_SINCRONIZACAO_TIMESTAMP] ?: 0L
    }

    // Função para salvar o novo timestamp após uma sincronização bem-sucedida.
    suspend fun salvarUltimaSincronizacao(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.ULTIMA_SINCRONIZACAO_TIMESTAMP] = timestamp
        }
    }

    // Objeto privado para manter as chaves de preferência organizadas.
    private object Keys {
        val ULTIMA_SINCRONIZACAO_TIMESTAMP = longPreferencesKey("ultima_sincronizacao_timestamp")
    }
}