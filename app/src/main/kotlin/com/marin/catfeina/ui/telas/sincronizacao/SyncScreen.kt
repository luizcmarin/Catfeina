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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.R
import com.marin.core.ui.UiState

@Composable
fun SyncScreen(
    modifier: Modifier = Modifier,
    viewModel: SyncViewModel = hiltViewModel(),
    onSyncComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.startSync()
    }

    SyncScreenContent(
        modifier = modifier,
        uiState = uiState,
        onTryAgain = { viewModel.startSync() },
        onFinish = onSyncComplete
    )
}

@Composable
private fun SyncScreenContent(
    modifier: Modifier = Modifier,
    uiState: UiState<Unit>,
    onTryAgain: () -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is UiState.Idle -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Iniciando sincronização...")
            }
            is UiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                uiState.message?.let { Text(it) }
            }
            is UiState.Success -> {
                Text("Sincronização concluída com sucesso!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onFinish) {
                    Text("Voltar")
                }
            }
            is UiState.Error -> {
                Text(uiState.message ?: stringResource(R.string.erro_carregar_dados))
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onTryAgain) {
                    Text("Tentar Novamente")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Sincronização - Ociosa")
@Composable
private fun SyncScreenPreview_Idle() {
    SyncScreenContent(
        uiState = UiState.Idle,
        onTryAgain = {},
        onFinish = {}
    )
}

@Preview(showBackground = true, name = "Sincronização - Carregando")
@Composable
private fun SyncScreenPreview_Loading() {
    SyncScreenContent(
        uiState = UiState.Loading("Baixando poesias..."),
        onTryAgain = {},
        onFinish = {}
    )
}

@Preview(showBackground = true, name = "Sincronização - Sucesso")
@Composable
private fun SyncScreenPreview_Success() {
    SyncScreenContent(
        uiState = UiState.Success(Unit),
        onTryAgain = {},
        onFinish = {}
    )
}

@Preview(showBackground = true, name = "Sincronização - Erro")
@Composable
private fun SyncScreenPreview_Error() {
    SyncScreenContent(
        uiState = UiState.Error("Falha na conexão com a internet."),
        onTryAgain = {},
        onFinish = {}
    )
}
