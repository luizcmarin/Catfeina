/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/informativo/InformativoScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Tela para exibir o conteúdo de um informativo.
*
*/
package com.marin.catfeina.ui.telas.informativo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.R
import com.marin.core.ui.Icones
import com.marin.core.util.RenderizadorMarkdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformativoScreen(
    viewModel: InformativoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is InformativoUiState.Loading -> {
            Scaffold {
                Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
        is InformativoUiState.Error -> {
            Scaffold {
                Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.erro_ocorreu, "Informativo não encontrado"))
                }
            }
        }
        is InformativoUiState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(state.informativo.titulo) },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icones.Voltar, contentDescription = stringResource(R.string.configuracoes_voltar))
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    item {
                        RenderizadorMarkdown(markdown = state.informativo.conteudo)
                    }
                }
            }
        }
    }
}
