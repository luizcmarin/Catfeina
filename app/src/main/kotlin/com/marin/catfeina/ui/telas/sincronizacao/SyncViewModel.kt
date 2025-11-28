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
*  Nota: ViewModel para a tela de Sincronização, responsável por orquestrar o processo de sincronização.
*
*/
package com.marin.catfeina.ui.telas.sincronizacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.repositories.SyncRepository
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun startSync() {
        viewModelScope.launch {
            syncRepository.executarSincronizacao().collect { newState ->
                _uiState.value = newState
            }
        }
    }
}
