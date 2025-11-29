/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/historico/HistoricoScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela de Histórico.
*
*/
package com.marin.catfeina.ui.telas.historico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.R
import com.marin.catfeina.data.models.Historico
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoricoScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoricoViewModel = hiltViewModel(),
    onPoesiaClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoricoScreenContent(
        modifier = modifier,
        uiState = uiState,
        onPoesiaClick = onPoesiaClick
    )
}

@Composable
private fun HistoricoScreenContent(
    modifier: Modifier = Modifier,
    uiState: HistoricoUiState,
    onPoesiaClick: (Long) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is HistoricoUiState.Loading -> CircularProgressIndicator()
            is HistoricoUiState.Error -> Text(stringResource(R.string.historico_erro))
            is HistoricoUiState.Success -> {
                if (uiState.historico.isEmpty()) {
                    Text(stringResource(R.string.historico_vazio))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.historico) { item ->
                            HistoricoItem(
                                item = item,
                                onClick = { if (item.nometabela == "poesias") onPoesiaClick(item.conteudoid) }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoricoItem(item: Historico, onClick: () -> Unit) {
    val formattedDate = remember(item.vistoem) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(item.vistoem))
    }
    ListItem(
        headlineContent = { Text(item.titulo) },
        supportingContent = { Text("Visto em: $formattedDate") },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview(showBackground = true, name = "Histórico - Carregando")
@Composable
private fun HistoricoScreenPreview_Loading() {
    HistoricoScreenContent(
        uiState = HistoricoUiState.Loading,
        onPoesiaClick = {}
    )
}

@Preview(showBackground = true, name = "Histórico - Vazio")
@Composable
private fun HistoricoScreenPreview_Empty() {
    HistoricoScreenContent(
        uiState = HistoricoUiState.Success(emptyList()),
        onPoesiaClick = {}
    )
}

@Preview(showBackground = true, name = "Histórico - Com Itens")
@Composable
private fun HistoricoScreenPreview_WithItems() {
    val currentTime = System.currentTimeMillis()
    val historicoMock = listOf(
        Historico(1, "poesias", 10, "Título da Poesia 1", currentTime - 1000 * 60 * 5),       // 5 minutes ago
        Historico(2, "informativo", 5, "Título do Informativo", currentTime - 1000 * 60 * 60 * 2) // 2 hours ago
    )
    HistoricoScreenContent(
        uiState = HistoricoUiState.Success(historicoMock),
        onPoesiaClick = {}
    )
}
