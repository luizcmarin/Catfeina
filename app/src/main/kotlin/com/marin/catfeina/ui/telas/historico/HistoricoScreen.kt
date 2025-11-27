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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.R
import com.marin.catfeina.data.models.Historico
import com.marin.core.ui.Icones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoScreen(
    viewModel: HistoricoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onPoesiaClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.historico_titulo)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icones.Voltar, contentDescription = stringResource(R.string.configuracoes_voltar))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is HistoricoUiState.Loading -> CircularProgressIndicator()
                is HistoricoUiState.Error -> Text(stringResource(R.string.historico_erro))
                is HistoricoUiState.Success -> {
                    if (state.historico.isEmpty()) {
                        Text(stringResource(R.string.historico_vazio))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.historico) { item ->
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
}

@Composable
private fun HistoricoItem(item: Historico, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(item.titulo) },
        supportingContent = { Text("Visto em: ${item.vistoem}") }, // TODO: Formatar a data
        modifier = Modifier.clickable(onClick = onClick)
    )
}
