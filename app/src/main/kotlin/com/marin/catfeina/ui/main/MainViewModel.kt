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
import com.marin.catfeina.ui.main.SyncState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sincronizacaoRepository: SincronizacaoRepository
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Ocioso)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    fun iniciarSincronizacao() {
        // Não inicia uma nova sincronização se uma já estiver em andamento.
        if (_syncState.value is SyncState.Executando) return

        viewModelScope.launch {
            _syncState.value = SyncState.Executando
            try {
                sincronizacaoRepository.executarSincronizacao()
                _syncState.value = SyncState.Sucesso("Dados sincronizados com sucesso!")
            } catch (e: Exception) {
                Timber.e(e, "Falha ao executar a sincronização a partir do ViewModel")
                _syncState.value = SyncState.Falha("Falha ao sincronizar os dados.")
            }
        }
    }

    /**
     * Reseta o estado da UI para Ocioso, permitindo que o usuário veja o ícone novamente.
     */
    fun resetarSyncState() {
        _syncState.value = SyncState.Ocioso
    }
}
