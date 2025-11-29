/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/debug/DebugScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela de depuração.
*
*/
package com.marin.catfeina.ui.telas.debug

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.marin.catfeina.R

@Composable
fun DebugScreen(
    modifier: Modifier = Modifier,
    viewModel: DebugViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    DebugScreenContent(
        modifier = modifier,
        uiState = uiState,
        onForceSync = {
            viewModel.forcarRessincronizacaoCompleta()
            Toast.makeText(context, "Registros de sincronização foram limpos!", Toast.LENGTH_SHORT).show()
        }
    )
}

@Composable
private fun DebugScreenContent(
    modifier: Modifier = Modifier,
    uiState: DebugUiState,
    onForceSync: () -> Unit
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            Text(stringResource(R.string.debug_build_info), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
        items(uiState.buildInfo) {
            InfoRow(label = it.first, value = it.second)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.debug_db_stats), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
        items(uiState.dbStats) {
            InfoRow(label = it.first, value = it.second.toString())
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Ações de Depuração", style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        item {
            Button(
                onClick = onForceSync,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Forçar Ressincronização Completa")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

@Preview(showBackground = true)
@Composable
private fun DebugScreenPreview() {
    val buildInfo = listOf(
        "Build Type" to "debug",
        "Version Name" to "1.0.0",
        "Version Code" to "1",
        "Build Time" to "01/01/2025 12:00:00",
        "Sync URL" to "https://example.com"
    )
    val dbStats = listOf(
        "Poesias" to 100L,
        "Atelier" to 5L,
        "Personagens" to 10L
    )

    DebugScreenContent(
        uiState = DebugUiState(buildInfo = buildInfo, dbStats = dbStats),
        onForceSync = {}
    )
}
