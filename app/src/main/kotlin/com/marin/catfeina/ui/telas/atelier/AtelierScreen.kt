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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.marin.catfeina.R
import com.marin.catfeina.data.models.Atelier
import com.marin.core.ui.Icones
import com.marin.core.util.placeholder

@Composable
fun AtelierScreen(
    modifier: Modifier = Modifier,
    viewModel: AtelierViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    AtelierScreenContent(
        modifier = modifier,
        uiState = uiState,
        showDialog = showDialog,
        onFabClick = { showDialog = true },
        onDismissDialog = { showDialog = false },
        onSaveNote = {
            viewModel.salvarNota(it.first, it.second)
            showDialog = false
        }
    )
}

@Composable
private fun AtelierScreenContent(
    modifier: Modifier = Modifier,
    uiState: AtelierUiState,
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onSaveNote: (Pair<String, String>) -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
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
                        Text(stringResource(R.string.atelier_sem_notas))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(uiState.notas) { nota ->
                                AtelierNoteItem(nota = nota)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        NewNoteDialog(
            onDismiss = onDismissDialog,
            onSave = onSaveNote
        )
    }
}

@Composable
private fun AtelierNoteItem(nota: Atelier) {
    ListItem(
        headlineContent = { Text(nota.titulo, style = MaterialTheme.typography.titleMedium) },
        supportingContent = { Text(nota.texto, maxLines = 2, style = MaterialTheme.typography.bodySmall) }
    )
}

@Composable
private fun NewNoteDialog(
    onDismiss: () -> Unit,
    onSave: (Pair<String, String>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.atelier_nova_nota_titulo)) },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.atelier_nova_nota_campo_titulo)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(R.string.atelier_nova_nota_campo_texto)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title to text) },
                enabled = title.isNotBlank() && text.isNotBlank()
            ) {
                Text(stringResource(R.string.salvar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
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
        showDialog = false,
        onFabClick = {},
        onDismissDialog = {},
        onSaveNote = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AtelierScreenPreview_SemNotas() {
    AtelierScreenContent(
        uiState = AtelierUiState.Success(emptyList()),
        showDialog = false,
        onFabClick = {},
        onDismissDialog = {},
        onSaveNote = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AtelierScreenPreview_Loading() {
    AtelierScreenContent(
        uiState = AtelierUiState.Loading,
        showDialog = false,
        onFabClick = {},
        onDismissDialog = {},
        onSaveNote = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AtelierScreenPreview_ComDialog() {
    AtelierScreenContent(
        uiState = AtelierUiState.Success(emptyList()),
        showDialog = true,
        onFabClick = {},
        onDismissDialog = {},
        onSaveNote = {}
    )
}
