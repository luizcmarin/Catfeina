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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.R

@Composable
fun PoesiaReaderScreen(
    viewModel: PoesiaReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            if (uiState is PoesiaReaderUiState.Success) {
                FloatingActionButton(
                    onClick = { viewModel.falar((uiState as PoesiaReaderUiState.Success).poesia.texto) }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = stringResource(R.string.leitor_poesia_falar)
                    )
                }
            }
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is PoesiaReaderUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PoesiaReaderUiState.Success -> {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)) {
                    item {
                        Text(text = state.poesia.titulo)
                    }
                    item {
                        Text(text = state.poesia.texto)
                    }
                }
            }
            is PoesiaReaderUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.leitor_poesia_erro))
                }
            }
        }
    }
}
