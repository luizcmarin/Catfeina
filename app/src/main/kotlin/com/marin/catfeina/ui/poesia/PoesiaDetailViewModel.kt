/*
 *  Projeto: Catfeina
 *  Arquivo: PoesiaDetailViewModel.kt
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

package com.marin.catfeina.ui.poesia

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.AppDestinationsArgs
import com.marin.catfeina.core.data.repository.PoesiaRepository
import com.marin.catfeina.sqldelight.GetPoesiaCompletaById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PoesiaDetailUiState(
    val isLoading: Boolean = true,
    val poesia: GetPoesiaCompletaById? = null,
    val error: String? = null
)

@OptIn(FlowPreview::class)
@HiltViewModel
class PoesiaDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val poesiaRepository: PoesiaRepository
) : ViewModel() {

    private val poesiaId: Long = checkNotNull(savedStateHandle[AppDestinationsArgs.POESIA_ID_ARG])

    val uiState: StateFlow<PoesiaDetailUiState> = poesiaRepository.getPoesiaById(poesiaId)
        .map { poesia ->
            if (poesia != null) {
                PoesiaDetailUiState(isLoading = false, poesia = poesia)
            } else {
                PoesiaDetailUiState(isLoading = false, error = "Poesia não encontrada")
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PoesiaDetailUiState(isLoading = true)
        )

    private val notaUsuarioFlow = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            notaUsuarioFlow
                .filterNotNull()
                .debounce(500) // Aguarda 500ms de inatividade para salvar
                .collect { nota ->
                    poesiaRepository.updateNotaUsuario(poesiaId, nota)
                }
        }
    }

    fun onToggleFavorito() {
        viewModelScope.launch {
            val poesia = uiState.value.poesia ?: return@launch
            poesiaRepository.toggleFavorito(poesia)
        }
    }

    fun onToggleLido() {
        viewModelScope.launch {
            val poesia = uiState.value.poesia ?: return@launch
            poesiaRepository.toggleLido(poesia)
        }
    }

    fun updateNotaUsuario(nota: String) {
        notaUsuarioFlow.value = nota
    }
}