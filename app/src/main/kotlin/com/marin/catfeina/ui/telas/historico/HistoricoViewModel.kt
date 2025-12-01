/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/historico/HistoricoViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela de Histórico.
*
*/
package com.marin.catfeina.ui.telas.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.models.Historico
import com.marin.catfeina.usecases.GetHistoricoUseCase
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface HistoricoUiState {
    data class Success(val historico: List<Historico>) : HistoricoUiState
    data object Error : HistoricoUiState
    data object Loading : HistoricoUiState
}

@HiltViewModel
class HistoricoViewModel @Inject constructor(
    getHistoricoUseCase: GetHistoricoUseCase
) : ViewModel() {

    val uiState: StateFlow<HistoricoUiState> = getHistoricoUseCase()
        .map {
            when (it) {
                is UiState.Success -> HistoricoUiState.Success(it.data)
                is UiState.Error -> HistoricoUiState.Error
                is UiState.Loading -> HistoricoUiState.Loading
                is UiState.Idle -> HistoricoUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HistoricoUiState.Loading
        )
}
