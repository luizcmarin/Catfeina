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
import com.marin.catfeina.usecases.GetPoesiasFavoritasUseCase
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class BuscaUiState(
    val isLoading: Boolean = false,
    val resultados: List<Poesia> = emptyList(),
    val favoritos: List<Poesia> = emptyList(),
    val error: String? = null,
    val debugQuery: String? = null // Adicionado para depuração
)

@OptIn(FlowPreview::class)
@HiltViewModel
class BuscaViewModel @Inject constructor(
    private val buscarPoesiasUseCase: BuscarPoesiasUseCase,
    private val getPoesiasFavoritasUseCase: GetPoesiasFavoritasUseCase
) : ViewModel() {

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    private val resultadosBuscaFlow = _searchTerm
        .debounce(300)
        .flatMapLatest { termo ->
            if (termo.length <= 2) {
                flowOf(UiState.Success(emptyList()))
            } else {
                buscarPoesiasUseCase(termo)
            }
        }

    val uiState: StateFlow<BuscaUiState> = combine(
        resultadosBuscaFlow,
        getPoesiasFavoritasUseCase(),
        _searchTerm.debounce(300) // Inclui o termo de busca aqui para gerar a query
    ) { resultadoBusca, favoritosState, termo ->
        
        val favoritos = (favoritosState as? UiState.Success)?.data?.sortedByDescending { it.dataleitura } ?: emptyList()

        val queryParaExibicao = if (termo.length > 2) {
            "SELECT * FROM poesiaView WHERE titulo LIKE '%$termo%' OR texto LIKE '%$termo%';"
        } else {
            null
        }

        when (resultadoBusca) {
            is UiState.Loading -> BuscaUiState(isLoading = true, favoritos = favoritos, debugQuery = queryParaExibicao)
            is UiState.Success -> BuscaUiState(resultados = resultadoBusca.data, favoritos = favoritos, debugQuery = queryParaExibicao)
            is UiState.Error -> BuscaUiState(error = resultadoBusca.message, favoritos = favoritos, debugQuery = queryParaExibicao)
            is UiState.Idle -> BuscaUiState(favoritos = favoritos, debugQuery = queryParaExibicao) // Trata o caso que faltava
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = BuscaUiState(isLoading = true)
    )

    fun onSearchTermChange(term: String) {
        _searchTerm.value = term
    }
}
