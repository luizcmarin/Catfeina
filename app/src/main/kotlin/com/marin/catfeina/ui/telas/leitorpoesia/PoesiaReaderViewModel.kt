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
import com.marin.catfeina.usecases.GetPoesiaByIdUseCase
import com.marin.core.tts.TtsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface PoesiaReaderUiState {
    data class Success(val poesia: Poesia) : PoesiaReaderUiState
    data object Error : PoesiaReaderUiState
    data object Loading : PoesiaReaderUiState
}

@HiltViewModel
class PoesiaReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPoesiaByIdUseCase: GetPoesiaByIdUseCase,
    private val ttsService: TtsService
) : ViewModel() {

    private val poesiaId: Long = savedStateHandle.get<Long>("poesiaId")!!

    val uiState: StateFlow<PoesiaReaderUiState> = getPoesiaByIdUseCase(poesiaId)
        .map { poesia ->
            if (poesia != null) {
                PoesiaReaderUiState.Success(poesia)
            } else {
                PoesiaReaderUiState.Error
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = PoesiaReaderUiState.Loading
        )

    fun falar(texto: String) {
        ttsService.falar(texto)
    }

    fun parar() {
        ttsService.parar()
    }

    override fun onCleared() {
        ttsService.finalizar()
        super.onCleared()
    }
}
