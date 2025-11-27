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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.marin.catfeina.R
import com.marin.catfeina.data.models.Atelier
import com.marin.core.ui.Icones

@Composable
fun AtelierScreen(
    viewModel: AtelierViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
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
            when (val state = uiState) {
                is AtelierUiState.Loading -> CircularProgressIndicator()
                is AtelierUiState.Error -> Text(stringResource(R.string.atelier_erro))
                is AtelierUiState.Success -> {
                    if (state.notas.isEmpty()) {
                        Text(stringResource(R.string.atelier_sem_notas))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.notas) { nota ->
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
            onDismiss = { showDialog = false },
            onSave = {
                viewModel.salvarNota(it.first, it.second)
                showDialog = false
            }
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
