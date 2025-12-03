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
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.marin.catfeina.R
import com.marin.core.util.rememberCliqueSeguro

@Composable
fun DebugScreen(
    modifier: Modifier = Modifier,
    viewModel: DebugViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val poesiasPaginadas = uiState.poesiasPaginadas.collectAsLazyPagingItems()

    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            Text(stringResource(R.string.debug_build_info), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
        items(uiState.buildInfo) { (label, value) ->
            InfoRow(label = label, value = value)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.debug_db_stats), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
        items(uiState.dbStats) { (label, value) ->
            InfoRow(label = label, value = value.toString())
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Diagnóstico de Imagens", style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
        item {
            InfoRow(label = "Caminho das Imagens", value = uiState.imageDiagnostics.imagePath)
            InfoRow(label = "Total de Imagens na Pasta", value = uiState.imageDiagnostics.imageCount.toString())
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Verificação de Poesias com Imagens", style = MaterialTheme.typography.titleMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        items(poesiasPaginadas.itemCount) { index ->
            val poesia = poesiasPaginadas[index]
            if (poesia != null && !poesia.imagem.isNullOrBlank()) {
                PoesiaStatusRow(poesia)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Ações de Depuração", style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        item {
            Button(
                onClick = rememberCliqueSeguro(onClick = {
                    viewModel.forcarRessincronizacaoCompleta()
                    Toast.makeText(context, "Registros de sincronização foram limpos!", Toast.LENGTH_SHORT).show()
                }),
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
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(text = value, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PoesiaStatusRow(poesia: PoesiaStatus) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("[${poesia.id}] ${poesia.titulo}", fontWeight = FontWeight.Bold)
        InfoRow(label = "Arquivo Esperado", value = poesia.imagem ?: "N/A")
        InfoRow(label = "Caminho Completo", value = poesia.caminhoCompleto)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Status", fontWeight = FontWeight.Bold)
            Text(
                text = if (poesia.existe) "ENCONTRADO" else "NÃO ENCONTRADO",
                color = if (poesia.existe) Color.Green else Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}
