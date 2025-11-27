/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/sincronizacao/SyncViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela de Sincronização.
*
*/
package com.marin.catfeina.ui.telas.sincronizacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.usecases.SyncAllDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SyncUiState {
    data object Idle : SyncUiState
    data object Syncing : SyncUiState
    data class Success(val message: String) : SyncUiState
    data class Error(val message: String) : SyncUiState
}

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncAllDataUseCase: SyncAllDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SyncUiState>(SyncUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun startSync() {
        viewModelScope.launch {
            _uiState.value = SyncUiState.Syncing
            syncAllDataUseCase()
                .onSuccess {
                    _uiState.value = SyncUiState.Success("Sincronização concluída com sucesso!")
                }
                .onFailure { throwable ->
                    _uiState.value = SyncUiState.Error("Falha na sincronização: ${throwable.message}")
                }
        }
    }
}
