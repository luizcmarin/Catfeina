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

/*
 *
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: GerenciadorTemas.kt
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

package com.marin.catfeina.core.temas

import androidx.compose.material3.ColorScheme
import com.marin.catfeina.core.data.repository.UserPreferencesRepository
import com.marin.catfeina.core.temas.ThemeModel
import com.marin.catfeina.core.temas.ThemeModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GerenciadorTemas @Inject constructor(
    private val themePreferences: UserPreferencesRepository
) {
    private val availableThemes: Map<ThemeModelKey, ThemeModel> = TemasPredefinidos.get()

    /**
     * O Flow reativo principal. Combina a chave do tema e o modo escuro
     * para emitir o ColorScheme correto sempre que qualquer um deles mudar.
     */
    val colorScheme: Flow<ColorScheme> = combine(
        themePreferences.selectedThemeKey,
        themePreferences.isDarkMode
    ) { themeKey, isDark ->
        val themeModel = availableThemes[themeKey] ?: availableThemes.getValue(ThemeModelKey.VERAO)
        if (isDark) themeModel.colorPalette.darkModeColors else themeModel.colorPalette.lightModeColors
    }

    fun getAvailableThemes(): Map<ThemeModelKey, ThemeModel> = availableThemes

    suspend fun setTheme(key: ThemeModelKey) {
        themePreferences.setSelectedThemeKey(key)
    }

    val currentThemeKey: Flow<ThemeModelKey> = themePreferences.selectedThemeKey
    val isDarkMode: Flow<Boolean> = themePreferences.isDarkMode

    suspend fun setDarkMode(isDark: Boolean) {
        themePreferences.setDarkMode(isDark)
    }
}
