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

/*
 *
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: PoesiaDetailViewModel.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class PoesiaDetailUiState(
    val isLoading: Boolean = true,
    val poesia: GetPoesiaCompletaById? = null,
    val error: String? = null
)

@HiltViewModel
class PoesiaDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    poesiaRepository: PoesiaRepository
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
}
