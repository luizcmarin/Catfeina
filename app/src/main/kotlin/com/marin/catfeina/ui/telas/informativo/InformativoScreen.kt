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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.marin.catfeina.R
import com.marin.catfeina.data.models.Informativo
import com.marin.core.util.RenderizadorMarkdown
import com.marin.core.util.rememberCliqueSeguro

@Composable
fun InformativoScreen(
    modifier: Modifier = Modifier,
    viewModel: InformativoViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    InformativoScreenContent(
        modifier = modifier,
        uiState = uiState,
        onBackClick = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InformativoScreenContent(
    modifier: Modifier = Modifier,
    uiState: InformativoUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is InformativoUiState.Success) {
                        Text(uiState.informativo.titulo, maxLines = 1)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = rememberCliqueSeguro(onClick = onBackClick)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.voltar))
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is InformativoUiState.Loading -> CircularProgressIndicator()
                is InformativoUiState.Error -> Text(stringResource(R.string.erro_carregar_dados))
                is InformativoUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        item {
                            RenderizadorMarkdown(markdown = uiState.informativo.conteudo)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Informativo - Carregando")
@Composable
private fun InformativoScreenPreview_Loading() {
    InformativoScreenContent(uiState = InformativoUiState.Loading, onBackClick = {})
}

@Preview(showBackground = true, name = "Informativo - Erro")
@Composable
private fun InformativoScreenPreview_Error() {
    InformativoScreenContent(uiState = InformativoUiState.Error, onBackClick = {})
}

@Preview(showBackground = true, name = "Informativo - Sucesso")
@Composable
private fun InformativoScreenPreview_Success() {
    val informativoMock = Informativo(
        id = 1,
        chave = "preview",
        titulo = "Título do Informativo",
        conteudo = "# Olá, Mundo!\n\nEste é um texto em markdown com **negrito** e *itálico*.",
        imagem = null,
        atualizadoem = 0L
    )
    InformativoScreenContent(uiState = InformativoUiState.Success(informativoMock), onBackClick = {})
}
