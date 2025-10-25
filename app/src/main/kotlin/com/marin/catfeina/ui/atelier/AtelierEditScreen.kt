/*
 *  Projeto: Catfeina
 *  Arquivo: AtelierEditScreen.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota:
 *
 */

package com.marin.catfeina.ui.atelier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.marin.catfeina.core.utils.Icones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtelierEditScreen(
    viewModel: AtelierEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                AtelierEditEvent.NavigateBack -> onNavigateBack()
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.nota?.id == 0L) "Nova Nota" else "Editar Nota") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icones.Voltar, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (uiState.nota?.id != 0L) {
                        IconButton(onClick = { viewModel.onEvent(
                            AtelierEditEvent.DeletarNota) }) {
                            Icon(Icones.Deletar, contentDescription = "Deletar")
                        }
                    }
                    IconButton(
                        onClick = { viewModel.onEvent(AtelierEditEvent.SalvarNota) },
                        enabled = uiState.podeSalvar
                    ) {
                        Icon(Icones.Salvar, contentDescription = "Salvar")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                uiState.nota?.let { nota ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = nota.titulo,
                            onValueChange = { viewModel.onEvent(AtelierEditEvent.OnTituloChange(it)) },
                            label = { Text("Título") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = nota.conteudo,
                            onValueChange = { viewModel.onEvent(AtelierEditEvent.OnConteudoChange(it)) },
                            label = { Text("Conteúdo") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}
