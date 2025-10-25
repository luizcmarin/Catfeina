/*
 *  Projeto: Catfeina
 *  Arquivo: HistoricoViewModel.kt
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
 *  Projeto: Catfeina
 *  Arquivo: HistoricoViewModel.kt
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

package com.marin.catfeina.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.core.data.repository.HistoricoRepository
import com.marin.catfeina.sqldelight.Historico
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UiState para a tela de Histórico.
 */
data class HistoricoUiState(
    val historico: List<Historico> = emptyList(),
    val isLoading: Boolean = true
)

/**
 * ViewModel para gerenciar a exibição e as ações do histórico.
 */
@HiltViewModel
class HistoricoViewModel @Inject constructor(
    private val historicoRepository: HistoricoRepository
) : ViewModel() {

    /**
     * StateFlow que emite o estado atual da UI para a lista de histórico.
     */
    val uiState: StateFlow<HistoricoUiState> = historicoRepository.getHistoricoCompleto()
        .map { historico -> HistoricoUiState(historico = historico, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoricoUiState()
        )

    /**
     * Limpa todos os registros do histórico.
     */
    fun limparHistorico() {
        viewModelScope.launch {
            historicoRepository.limparHistorico()
        }
    }
}
