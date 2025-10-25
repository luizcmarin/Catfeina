/*
 *  Projeto: Catfeina
 *  Arquivo: TemasViewModel.kt
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.core.temas.inverno.invernoDarkScheme
import com.marin.catfeina.core.temas.inverno.invernoLightScheme
import com.marin.catfeina.core.temas.outono.outonoDarkScheme
import com.marin.catfeina.core.temas.outono.outonoLightScheme
import com.marin.catfeina.core.temas.primavera.primaveraDarkScheme
import com.marin.catfeina.core.temas.primavera.primaveraLightScheme
import com.marin.catfeina.core.temas.verao.veraoDarkScheme
import com.marin.catfeina.core.temas.verao.veraoLightScheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Enum para as chaves de identificação dos temas.
 * Garante segurança de tipo ao selecionar e salvar temas.
 */
enum class TemaModelKey {
    VERAO,  // Tema padrão
    OUTONO,
    INVERNO,
    PRIMAVERA
}

/**
 * Objeto Singleton que fornece um mapa de todos os temas disponíveis na aplicação.
 * Centraliza a definição dos temas, facilitando a manutenção.
 */
object TemasPredefinidos {

    fun get(): Map<TemaModelKey, TemaModel> {
        return mapOf(
            TemaModelKey.VERAO to TemaModel(
                colorPalette = ColorPalette(
                    lightModeColors = veraoLightScheme,
                    darkModeColors = veraoDarkScheme
                )
            ),
            TemaModelKey.OUTONO to TemaModel(
                colorPalette = ColorPalette(
                    lightModeColors = outonoLightScheme,
                    darkModeColors = outonoDarkScheme
                )
            ),
            TemaModelKey.INVERNO to TemaModel(
                colorPalette = ColorPalette(
                    lightModeColors = invernoLightScheme,
                    darkModeColors = invernoDarkScheme
                )
            ),
            TemaModelKey.PRIMAVERA to TemaModel(
                colorPalette = ColorPalette(
                    lightModeColors = primaveraLightScheme,
                    darkModeColors = primaveraDarkScheme
                )
            )
        )
    }
}

/**
 * Representa um único tema, contendo suas paletas de cores.
 */
data class TemaModel(
    val colorPalette: ColorPalette
)

/**
 * Contém as paletas de cores para os modos claro e escuro de um tema.
 */
data class ColorPalette(
    val lightModeColors: ColorScheme,
    val darkModeColors: ColorScheme
)

data class TemasUiState(
    val temasDisponiveis: Map<TemaModelKey, TemaModel> = emptyMap(),
    val temaAtualKey: TemaModelKey = TemaModelKey.VERAO,
    val isDarkMode: Boolean = false,
    val textSize: Float = 0.5f,
    val isFullScreen: Boolean = false
)

@HiltViewModel
open class TemasViewModel @Inject constructor(
    val gerenciadorTemas: GerenciadorTemas
) : ViewModel() {

    open val uiState: StateFlow<TemasUiState> by lazy {
        combine(
            gerenciadorTemas.currentThemeKey,
            gerenciadorTemas.isDarkMode,
            gerenciadorTemas.textSize,
            gerenciadorTemas.isFullScreen
        ) { themeKey, isDark, size, isFull ->
            TemasUiState(
                temasDisponiveis = gerenciadorTemas.getAvailableThemes(),
                temaAtualKey = themeKey,
                isDarkMode = isDark,
                textSize = size,
                isFullScreen = isFull
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = TemasUiState(
                temasDisponiveis = gerenciadorTemas.getAvailableThemes(),
                temaAtualKey = TemaModelKey.VERAO
            )
        )
    }

    fun onThemeSelected(key: TemaModelKey) {
        viewModelScope.launch {
            gerenciadorTemas.setTheme(key)
        }
    }

    fun onDarkModeChange(isDarkMode: Boolean) {
        viewModelScope.launch {
            gerenciadorTemas.setDarkMode(isDarkMode)
        }
    }

    fun onTextSizeChange(size: Float) {
        viewModelScope.launch {
            gerenciadorTemas.setTextSize(size)
        }
    }

    fun onFullScreenChange(isFullScreen: Boolean) {
        viewModelScope.launch {
            gerenciadorTemas.setFullScreen(isFullScreen)
        }
    }
}