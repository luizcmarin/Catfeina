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
*  Nota: Composable para a tela de detalhes e leitura da poesia.
*
*/
package com.marin.catfeina.ui.telas.leitorpoesia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.marin.catfeina.R
import com.marin.catfeina.data.models.Poesia
import com.marin.core.ui.MarkdownRenderer
import com.marin.core.tts.TtsEstado

@Composable
fun PoesiaReaderScreen(
    modifier: Modifier = Modifier,
    viewModel: PoesiaReaderViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PoesiaReaderScreenContent(
        modifier = modifier,
        uiState = uiState,
        onPlayPauseClick = viewModel::onPlayPauseClick,
        onStopClick = viewModel::onStopClick,
        onBackClick = { navController.popBackStack() },
        onFavoritaClick = { viewModel.toggleFavorita() },
        onLidaClick = { viewModel.toggleLida() },
        onSalvarNota = { nota -> viewModel.salvarNota(nota) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PoesiaReaderScreenContent(
    modifier: Modifier = Modifier,
    uiState: PoesiaReaderUiState,
    onPlayPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onBackClick: () -> Unit,
    onFavoritaClick: () -> Unit,
    onLidaClick: () -> Unit,
    onSalvarNota: (String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is PoesiaReaderUiState.Success) {
                        Text(uiState.titulo, maxLines = 1)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.voltar))
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState is PoesiaReaderUiState.Success) {
                TtsControls(ttsState = uiState.ttsState, onPlayPause = onPlayPauseClick, onStop = onStopClick)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is PoesiaReaderUiState.Loading -> CircularProgressIndicator()
                is PoesiaReaderUiState.Error -> Text(stringResource(R.string.leitor_poesia_erro))
                is PoesiaReaderUiState.Success -> {
                    PoesiaContent(
                        poesia = uiState.poesia,
                        onFavoritaClick = onFavoritaClick,
                        onLidaClick = onLidaClick,
                        onSalvarNota = onSalvarNota
                    )
                }
            }
        }
    }
}

@Composable
private fun PoesiaContent(
    poesia: Poesia,
    onFavoritaClick: () -> Unit,
    onLidaClick: () -> Unit,
    onSalvarNota: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var notaState by remember(poesia.notausuario) { mutableStateOf(poesia.notausuario ?: "") }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)
    ) {
        // Renderizador Markdown
        MarkdownRenderer(markdown = poesia.texto) { description, url ->
            AsyncImage(
                request = ImageRequest(LocalContext.current, url),
                contentDescription = description,
                modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f).padding(vertical = 8.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Controles de Interação
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = onFavoritaClick) {
                Icon(
                    imageVector = if (poesia.favorita) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favoritar",
                    tint = if (poesia.favorita) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
            IconButton(onClick = onLidaClick) {
                Icon(
                    imageVector = Icons.Default.RemoveRedEye,
                    contentDescription = "Marcar como lida",
                    tint = if (poesia.lida) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }

        // Campo de Nota do Usuário
        OutlinedTextField(
            value = notaState,
            onValueChange = { notaState = it },
            label = { Text("Sua nota pessoal") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Button(onClick = { onSalvarNota(notaState) }, modifier = Modifier.align(Alignment.End).padding(top = 8.dp)) {
            Text("Salvar Nota")
        }
    }
}


@Composable
private fun TtsControls(ttsState: TtsEstado, onPlayPause: () -> Unit, onStop: () -> Unit) {
    Row {
        FloatingActionButton(
            onClick = onPlayPause,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            val icon = if (ttsState == TtsEstado.REPRODUZINDO) Icons.Default.Pause else Icons.Default.PlayArrow
            Icon(icon, contentDescription = stringResource(R.string.leitor_poesia_falar))
        }
        if (ttsState == TtsEstado.REPRODUZINDO || ttsState == TtsEstado.PAUSADO) {
            FloatingActionButton(onClick = onStop) {
                Icon(Icons.Default.Stop, contentDescription = stringResource(R.string.leitor_poesia_parar))
            }
        }
    }
}
