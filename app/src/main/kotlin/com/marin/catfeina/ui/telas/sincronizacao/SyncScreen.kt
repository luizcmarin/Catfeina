/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/sincronizacao/SyncScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela de Sincronização.
*
*/
package com.marin.catfeina.ui.telas.sincronizacao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.core.ui.UiState

@Composable
fun SyncScreen(
    viewModel: SyncViewModel = hiltViewModel(),
    onSyncComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = uiState) {
            is UiState.Idle -> {
                Text("Pronto para sincronizar os dados.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.startSync() }) {
                    Text("Iniciar Sincronização")
                }
            }
            is UiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                val message = state.message
                if (message != null) {
                    Text(message)
                }
            }
            is UiState.Success -> {
                Text("Sincronização concluída com sucesso!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onSyncComplete) {
                    Text("Voltar")
                }
            }
            is UiState.Error -> {
                Text(state.message)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.startSync() }) {
                    Text("Tentar Novamente")
                }
            }
        }
    }
}
