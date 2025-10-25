/*
 *  Projeto: Catfeina
 *  Arquivo: MainViewModel.kt
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

package com.marin.catfeina.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.core.data.repository.SincronizacaoRepository
import com.marin.catfeina.core.ui.SyncState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sincronizacaoRepository: SincronizacaoRepository
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Ocioso)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()
    private val _syncStatusMessage = MutableStateFlow<String?>(null)
    val syncStatusMessage: StateFlow<String?> = _syncStatusMessage

    fun iniciarSincronizacao() {
        // Não inicia uma nova sincronização se uma já estiver em andamento.
        if (_syncState.value is SyncState.Executando) return

        viewModelScope.launch {
            _syncState.value = SyncState.Executando

            // Coleta as mensagens do fluxo do repositório e as envia para a UI.
            sincronizacaoRepository.executarSincronizacao()
                .collect { mensagem ->
                    _syncStatusMessage.value = mensagem
                }

            // Ao final da coleta, a sincronização terminou (com sucesso ou falha).
            // A mensagem final já foi enviada. Agora, apenas resetamos o estado do ícone.
            _syncState.value = SyncState.Ocioso
        }
    }

    /**
     * Limpa a mensagem de status da sincronização para que não seja exibida novamente.
     * Deve ser chamado pela UI após a mensagem ter sido mostrada (ex: em um Snackbar).
     */
    fun limparMensagemSincronizacao() {
        _syncStatusMessage.value = null
    }
}
