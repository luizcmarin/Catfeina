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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HistoricoUiState {
    data class Success(val historico: List<Historico>) : HistoricoUiState
    data object Error : HistoricoUiState
    data object Loading : HistoricoUiState
}

@HiltViewModel
class HistoricoViewModel @Inject constructor(
    private val getHistoricoUseCase: GetHistoricoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoricoUiState>(HistoricoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getHistoricoUseCase()
                .catch { _uiState.value = HistoricoUiState.Error }
                .collect { historico ->
                    _uiState.value = HistoricoUiState.Success(historico)
                }
        }
    }
}
