/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/busca/BuscaViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela de Busca.
*
*/
package com.marin.catfeina.ui.telas.busca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.usecases.BuscarPoesiasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BuscaUiState {
    data class Success(val resultados: List<Poesia>) : BuscaUiState
    data object Error : BuscaUiState
    data object Loading : BuscaUiState
    data object Idle : BuscaUiState
}

@OptIn(FlowPreview::class)
@HiltViewModel
class BuscaViewModel @Inject constructor(
    private val buscarPoesiasUseCase: BuscarPoesiasUseCase
) : ViewModel() {

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    private val _uiState = MutableStateFlow<BuscaUiState>(BuscaUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        _searchTerm
            .debounce(300) // Aguarda 300ms após o usuário parar de digitar
            .onEach { term ->
                if (term.length > 2) {
                    realizarBusca(term)
                } else {
                    _uiState.value = BuscaUiState.Idle
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchTermChange(term: String) {
        _searchTerm.value = term
    }

    private fun realizarBusca(term: String) {
        viewModelScope.launch {
            _uiState.value = BuscaUiState.Loading
            buscarPoesiasUseCase(term)
                .catch { _uiState.value = BuscaUiState.Error }
                .collect { resultados ->
                    _uiState.value = BuscaUiState.Success(resultados)
                }
        }
    }
}
