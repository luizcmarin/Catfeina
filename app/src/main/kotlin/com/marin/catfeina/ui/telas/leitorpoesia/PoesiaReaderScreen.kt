/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/leitorpoesia/PoesiaReaderScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela do leitor de poesia.
*
*/
package com.marin.catfeina.ui.telas.leitorpoesia

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.R
import com.marin.catfeina.data.models.Poesia

@Composable
fun PoesiaReaderScreen(
    modifier: Modifier = Modifier,
    viewModel: PoesiaReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PoesiaReaderScreenContent(
        modifier = modifier,
        uiState = uiState,
        onFalarClick = { texto -> viewModel.falar(texto) }
    )
}

@Composable
private fun PoesiaReaderScreenContent(
    modifier: Modifier = Modifier,
    uiState: PoesiaReaderUiState,
    onFalarClick: (String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (uiState is PoesiaReaderUiState.Success) {
                FloatingActionButton(
                    onClick = { onFalarClick(uiState.poesia.texto) }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = stringResource(R.string.leitor_poesia_falar)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is PoesiaReaderUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is PoesiaReaderUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Text(text = uiState.poesia.titulo)
                        }
                        item {
                            Text(text = uiState.poesia.texto)
                        }
                    }
                }
                is PoesiaReaderUiState.Error -> {
                    Text(text = stringResource(R.string.leitor_poesia_erro))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Leitor - Carregando")
@Composable
private fun PoesiaReaderScreenPreview_Loading() {
    PoesiaReaderScreenContent(
        uiState = PoesiaReaderUiState.Loading,
        onFalarClick = {}
    )
}

@Preview(showBackground = true, name = "Leitor - Erro")
@Composable
private fun PoesiaReaderScreenPreview_Error() {
    PoesiaReaderScreenContent(
        uiState = PoesiaReaderUiState.Error,
        onFalarClick = {}
    )
}

@Preview(showBackground = true, name = "Leitor - Sucesso")
@Composable
private fun PoesiaReaderScreenPreview_Success() {
    val poesiaMock = Poesia(
        id = 1,
        titulo = "O Gato Preto",
        texto = "Era uma vez um gato preto, que de tão teimoso, só comia macarrão com queijo.",
        imagem = null, autor = "Autor Desconhecido", nota = null, textobase = "",
        textofinal = null, anterior = null, proximo = null, atualizadoem = 0L,
        favorita = true, lida = false, dataleitura = null, notausuario = null
    )
    PoesiaReaderScreenContent(
        uiState = PoesiaReaderUiState.Success(poesiaMock),
        onFalarClick = {}
    )
}
