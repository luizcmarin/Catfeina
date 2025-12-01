/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/leitorpoesia/PoesiaReaderViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela do leitor de poesia.
*
*/
package com.marin.catfeina.ui.telas.leitorpoesia

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.catfeina.usecases.GetPoesiaByIdUseCase
import com.marin.core.tts.TtsEstado
import com.marin.core.tts.TtsService
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PoesiaReaderUiState {
    data class Success(val poesia: Poesia, val titulo: String, val ttsState: TtsEstado) : PoesiaReaderUiState
    data object Error : PoesiaReaderUiState
    data object Loading : PoesiaReaderUiState
}

@HiltViewModel
class PoesiaReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPoesiaByIdUseCase: GetPoesiaByIdUseCase,
    private val poesiaRepository: PoesiaRepository,
    private val ttsService: TtsService
) : ViewModel() {

    private val poesiaId: Long = checkNotNull(savedStateHandle["poesiaId"]).toString().toLong()
    private var poesiaAtual: Poesia? = null

    val uiState: StateFlow<PoesiaReaderUiState> = combine(
        getPoesiaByIdUseCase(poesiaId),
        ttsService.estado
    ) { poesiaResult, ttsState ->
        when (poesiaResult) {
            is UiState.Success -> {
                val poesia = poesiaResult.data
                if (poesia != null) {
                    poesiaAtual = poesia
                    val titulo = poesiaRepository.extrairTitulo(poesia)
                    PoesiaReaderUiState.Success(poesia, titulo, ttsState)
                } else {
                    PoesiaReaderUiState.Error
                }
            }
            is UiState.Error -> PoesiaReaderUiState.Error
            else -> PoesiaReaderUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PoesiaReaderUiState.Loading
    )

    fun toggleFavorita() = viewModelScope.launch {
        poesiaAtual?.let { poesia ->
            poesiaRepository.updateFavorita(poesia.id, !poesia.favorita)
        }
    }

    fun toggleLida() = viewModelScope.launch {
        poesiaAtual?.let { poesia ->
            poesiaRepository.updateLida(poesia.id, !poesia.lida)
        }
    }

    fun salvarNota(nota: String) = viewModelScope.launch {
        poesiaAtual?.let { poesia ->
            poesiaRepository.updateNotaUsuario(poesia.id, nota)
        }
    }

    fun onPlayPauseClick() {
        poesiaAtual?.let { poesia ->
            val estadoAtual = ttsService.estado.value
            if (estadoAtual == TtsEstado.REPRODUZINDO) {
                ttsService.pausar()
            } else {
                val textoLimpo = poesiaRepository.limparMarkdownParaTts(poesia)
                ttsService.reproduzir(textoLimpo)
            }
        }
    }

    fun onStopClick() {
        ttsService.parar()
    }

    override fun onCleared() {
        ttsService.finalizar()
        super.onCleared()
    }
}
