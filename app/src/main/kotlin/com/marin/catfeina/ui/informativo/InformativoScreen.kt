/*
 *  Projeto: Catfeina
 *  Arquivo: InformativoScreen.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota:
 *
 */

package com.marin.catfeina.ui.informativo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.core.ui.CatAnimation
import com.marin.catfeina.core.ui.TtsPlayerController
import com.marin.catfeina.core.ui.UiState
import com.marin.catfeina.core.utils.Icones
import com.marin.catfeina.core.utils.formataComoData
import com.marin.catfeina.sqldelight.Informativos

@Composable
fun InformativoScreen(
    onNavigateBack: () -> Unit,
    viewModel: InformativoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Loading -> {
            CatAnimation(
                modifier = Modifier.size(200.dp),
                assetName = "cat_carregando.lottie",
            )
        }

        is UiState.Success -> {
            InformativoSuccessScreen(
                informativo = state.data,
                onNavigateBack = onNavigateBack
            )
        }

        is UiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InformativoSuccessScreen(
    informativo: Informativos,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(informativo.titulo) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icones.Voltar, contentDescription = "Voltar")
                    }
                }
            )
        },
        // Adicionando o player na BottomBar >>>>>
        bottomBar = {
            TtsPlayerController(
                textToPlay = "${informativo.titulo}. ${informativo.conteudo}",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = informativo.conteudo,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Última atualização: ${informativo.dataAtualizacao.formataComoData()}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
