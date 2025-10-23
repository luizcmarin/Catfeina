/*
 *  Projeto: Catfeina
 *  Arquivo: SearchViewModel.kt
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


package com.marin.catfeina.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.features.search.data.repository.SearchRepository
import com.marin.catfeina.features.search.domain.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * UiState para a tela de Busca.
 */
data class SearchUiState(
    val query: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val isSearching: Boolean = false,
    val nothingFound: Boolean = false
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    // StateFlow para a query de busca, que será observada para acionar as buscas.
    private val queryFlow = MutableStateFlow("")

    init {
        queryFlow
            .debounce(300) // Aguarda 300ms após a última digitação antes de buscar
            .distinctUntilChanged() // Só busca se o texto for diferente do anterior
            .onEach { query ->
                _uiState.update { it.copy(isSearching = query.isNotBlank(), nothingFound = false) }
            }
            .flatMapLatest { query ->
                searchRepository.search(query) // Executa a busca no repositório
            }
            .onEach { results ->
                _uiState.update {
                    it.copy(
                        searchResults = results,
                        isSearching = false,
                        nothingFound = results.isEmpty() && it.query.isNotBlank()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Chamado pela UI sempre que o texto de busca é alterado.
     */
    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        queryFlow.value = query
    }
}
