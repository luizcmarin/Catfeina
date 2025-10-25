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

package com.marin.catfeina.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.marin.catfeina.core.temas.TemaModelKey
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
        val TEXT_SIZE = floatPreferencesKey("text_size")
        val IS_FULL_SCREEN = booleanPreferencesKey("is_full_screen")
    }

    val onboardingCompleto: Flow<Boolean> = dataStore.data.map { it[Keys.ONBOARDING_COMPLETO] ?: false }

    val selectedThemeKey: Flow<TemaModelKey> = dataStore.data.map { preferences ->
        val keyName = preferences[Keys.SELECTED_THEME_KEY] ?: TemaModelKey.VERAO.name
        try {
            TemaModelKey.valueOf(keyName)
        } catch (e: IllegalArgumentException) {
            TemaModelKey.VERAO
        }
    }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { it[Keys.IS_DARK_MODE] ?: false }

    val textSize: Flow<Float> = dataStore.data.map { it[Keys.TEXT_SIZE] ?: 0.5f }

    val isFullScreen: Flow<Boolean> = dataStore.data.map { it[Keys.IS_FULL_SCREEN] ?: false }

    suspend fun setOnboardingCompleto(isCompleto: Boolean) {
        dataStore.edit { it[Keys.ONBOARDING_COMPLETO] = isCompleto }
    }

    suspend fun setSelectedThemeKey(themeKey: TemaModelKey) {
        dataStore.edit { it[Keys.SELECTED_THEME_KEY] = themeKey.name }
    }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStore.edit { it[Keys.IS_DARK_MODE] = isDarkMode }
    }

    suspend fun setTextSize(size: Float) {
        dataStore.edit { it[Keys.TEXT_SIZE] = size }
    }

    suspend fun setIsFullScreen(isFullScreen: Boolean) {
        dataStore.edit { it[Keys.IS_FULL_SCREEN] = isFullScreen }
    }
}