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

package com.marin.catfeina.ui.temas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.core.temas.GerenciadorTemas
import com.marin.catfeina.core.temas.ThemeModel
import com.marin.catfeina.core.temas.ThemeModelKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TemasUiState(
    val temasDisponiveis: Map<ThemeModelKey, ThemeModel> = emptyMap(),
    val temaAtualKey: ThemeModelKey = ThemeModelKey.VERAO,
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
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TemasUiState(
                temasDisponiveis = gerenciadorTemas.getAvailableThemes(),
                temaAtualKey = ThemeModelKey.VERAO
            )
        )
    }

    fun onThemeSelected(key: ThemeModelKey) {
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