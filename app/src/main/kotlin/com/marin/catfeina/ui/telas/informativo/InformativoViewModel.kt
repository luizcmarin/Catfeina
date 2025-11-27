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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
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

    private val _uiState = MutableStateFlow<InformativoUiState>(InformativoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val informativoKey: String? = savedStateHandle.get<String>("informativoKey")
        if (informativoKey != null) {
            viewModelScope.launch {
                getInformativoUseCase(informativoKey)
                    .catch { _uiState.value = InformativoUiState.Error }
                    .collect { informativo ->
                        if (informativo != null) {
                            _uiState.value = InformativoUiState.Success(informativo)
                        } else {
                            _uiState.value = InformativoUiState.Error
                        }
                    }
            }
        } else {
            _uiState.value = InformativoUiState.Error
        }
    }
}
