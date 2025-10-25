/*
 *  Projeto: Catfeina
 *  Arquivo: GerenciadorTemas.kt
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

package com.marin.catfeina.core.temas

import androidx.compose.material3.ColorScheme
import com.marin.catfeina.core.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GerenciadorTemas @Inject constructor(
    private val themePreferences: UserPreferencesRepository
) {
    private val availableThemes: Map<TemaModelKey, TemaModel> = TemasPredefinidos.get()

    val colorScheme: Flow<ColorScheme> = combine(
        themePreferences.selectedThemeKey,
        themePreferences.isDarkMode
    ) { themeKey, isDark ->
        val temaModel = availableThemes[themeKey] ?: availableThemes.getValue(TemaModelKey.VERAO)
        if (isDark) temaModel.colorPalette.darkModeColors else temaModel.colorPalette.lightModeColors
    }

    fun getAvailableThemes(): Map<TemaModelKey, TemaModel> = availableThemes

    suspend fun setTheme(key: TemaModelKey) {
        themePreferences.setSelectedThemeKey(key)
    }

    val currentThemeKey: Flow<TemaModelKey> = themePreferences.selectedThemeKey
    val isDarkMode: Flow<Boolean> = themePreferences.isDarkMode
    val textSize: Flow<Float> = themePreferences.textSize
    val isFullScreen: Flow<Boolean> = themePreferences.isFullScreen

    suspend fun setDarkMode(isDark: Boolean) {
        themePreferences.setDarkMode(isDark)
    }

    suspend fun setTextSize(size: Float) {
        themePreferences.setTextSize(size)
    }

    suspend fun setFullScreen(isFullScreen: Boolean) {
        themePreferences.setIsFullScreen(isFullScreen)
    }
}