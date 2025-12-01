/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/informativo/InformativoViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela que exibe um informativo.
*
*/
package com.marin.catfeina.ui.telas.informativo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.models.Informativo
import com.marin.catfeina.usecases.GetInformativoUseCase
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface InformativoUiState {
    data class Success(val informativo: Informativo) : InformativoUiState
    data object Error : InformativoUiState
    data object Loading : InformativoUiState
}

@HiltViewModel
class InformativoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getInformativoUseCase: GetInformativoUseCase
) : ViewModel() {

    val uiState: StateFlow<InformativoUiState> = getInformativoUseCase(checkNotNull(savedStateHandle["informativoKey"]))
        .map {
            when (it) {
                is UiState.Success -> {
                    val informativo = it.data
                    if (informativo != null) {
                        InformativoUiState.Success(informativo)
                    } else {
                        InformativoUiState.Error
                    }
                }
                is UiState.Error -> InformativoUiState.Error
                is UiState.Loading -> InformativoUiState.Loading
                is UiState.Idle -> InformativoUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = InformativoUiState.Loading
        )
}
