/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/atelier/AtelierScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela do Atelier, onde o usuário pode ver e criar notas.
*
*/
package com.marin.catfeina.ui.telas.atelier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.R
import com.marin.catfeina.data.models.Atelier
import com.marin.core.ui.Icones
import com.marin.core.util.cliqueSeguro
import com.marin.core.util.rememberCliqueSeguro

@Composable
fun AtelierScreen(
    modifier: Modifier = Modifier,
    viewModel: AtelierViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var notaEmEdicao by remember { mutableStateOf<Atelier?>(null) }

    AtelierScreenContent(
        modifier = modifier,
        uiState = uiState,
        onFabClick = { notaEmEdicao = Atelier(id = 0, titulo = "", texto = "", atualizadoem = 0, fixada = false) },
        onNoteClick = { nota -> notaEmEdicao = nota }
    )

    notaEmEdicao?.let {
        NewNoteDialog(
            nota = it,
            onDismiss = { notaEmEdicao = null },
            onSave = {
                viewModel.salvarNota(it)
                notaEmEdicao = null
            },
            onDelete = {
                viewModel.excluirNota(it)
                notaEmEdicao = null
            }
        )
    }
}

@Composable
private fun AtelierScreenContent(
    modifier: Modifier = Modifier,
    uiState: AtelierUiState,
    onFabClick: () -> Unit,
    onNoteClick: (Atelier) -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = rememberCliqueSeguro(onClick = onFabClick)) {
                Icon(Icones.Mais, contentDescription = stringResource(R.string.atelier_adicionar_nota))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is AtelierUiState.Loading -> CircularProgressIndicator()
                is AtelierUiState.Error -> Text(stringResource(R.string.atelier_erro))
                is AtelierUiState.Success -> {
                    if (uiState.notas.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                           Text(stringResource(R.string.atelier_sem_notas))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(uiState.notas, key = { it.id }) { nota ->
                                AtelierNoteItem(nota = nota, onClick = { onNoteClick(nota) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AtelierNoteItem(nota: Atelier, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .cliqueSeguro(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(nota.titulo, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text(nota.texto, maxLines = 3, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun NewNoteDialog(
    nota: Atelier,
    onDismiss: () -> Unit,
    onSave: (Atelier) -> Unit,
    onDelete: (Atelier) -> Unit
) {
    var title by remember { mutableStateOf(nota.titulo) }
    var text by remember { mutableStateOf(nota.texto) }
    val isNewNote = nota.id == 0L
    val focusRequester = remember { FocusRequester() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(if (isNewNote) R.string.atelier_nova_nota_titulo else R.string.atelier_editar_nota_titulo)) },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.atelier_nova_nota_campo_titulo)) },
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(R.string.atelier_nova_nota_campo_texto)) },
                    modifier = Modifier.fillMaxWidth().height(150.dp) // Caixa de texto grande
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = rememberCliqueSeguro { 
                    val updatedNota = if(isNewNote) nota.copy(id = System.currentTimeMillis(), titulo = title, texto = text) else nota.copy(titulo = title, texto = text)
                    onSave(updatedNota) 
                },
                enabled = title.isNotBlank() && text.isNotBlank()
            ) {
                Text(stringResource(R.string.salvar))
            }
        },
        dismissButton = {
            Column {
                 if (!isNewNote) {
                    TextButton(onClick = rememberCliqueSeguro { onDelete(nota) }) {
                        Text("Excluir", color = MaterialTheme.colorScheme.error)
                    }
                }
                TextButton(onClick = rememberCliqueSeguro(onClick = onDismiss)) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
private fun AtelierScreenPreview_ComNotas() {
    val notas = listOf(
        Atelier(1, "Nota 1", "Conteúdo da nota 1", 0L, false),
        Atelier(2, "Nota 2", "Conteúdo da nota 2", 0L, false)
    )
    AtelierScreenContent(
        uiState = AtelierUiState.Success(notas),
        onFabClick = {},
        onNoteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AtelierScreenPreview_ComDialog() {
    NewNoteDialog(nota = Atelier(1, "Nota 1", "Conteúdo da nota 1", 0L, false), onDismiss = {}, onSave = {}, onDelete = {})
}
