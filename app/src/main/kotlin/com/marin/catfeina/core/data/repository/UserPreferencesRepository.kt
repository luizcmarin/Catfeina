/*
 *  Projeto: Catfeina
 *  Arquivo: UserPreferencesRepository.kt
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

/*
 *
 *  Projeto: Catfeina
 *  Arquivo: UserPreferencesRepository.kt
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: UserPreferencesRepository.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
 *
 */

package com.marin.catfeina.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.marin.catfeina.core.temas.ThemeModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val ONBOARDING_COMPLETO = booleanPreferencesKey("onboarding_completo")
        val SELECTED_THEME_KEY = stringPreferencesKey("selected_theme_key")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    val onboardingCompleto: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Keys.ONBOARDING_COMPLETO] ?: false
    }

    val selectedThemeKey: Flow<ThemeModelKey> = dataStore.data.map { preferences ->
        val keyName = preferences[Keys.SELECTED_THEME_KEY] ?: ThemeModelKey.VERAO.name
        try {
            ThemeModelKey.valueOf(keyName)
        } catch (e: IllegalArgumentException) {
            ThemeModelKey.VERAO
        }
    }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Keys.IS_DARK_MODE] ?: false
    }

    suspend fun setOnboardingCompleto(isCompleto: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETO] = isCompleto
        }
    }

    suspend fun setSelectedThemeKey(themeKey: ThemeModelKey) {
        dataStore.edit { preferences ->
            preferences[Keys.SELECTED_THEME_KEY] = themeKey.name
        }
    }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.IS_DARK_MODE] = isDarkMode
        }
    }
}