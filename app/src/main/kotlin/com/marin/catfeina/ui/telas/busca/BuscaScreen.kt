/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/busca/BuscaScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela de Busca.
*
*/
package com.marin.catfeina.ui.telas.busca

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.marin.catfeina.R
import com.marin.catfeina.Screen
import com.marin.catfeina.data.models.Poesia
import com.marin.core.ui.AnimatedAsset

@Composable
fun BuscaScreen(
    viewModel: BuscaViewModel = hiltViewModel(),
    navController: NavController
) {
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = { viewModel.onSearchTermChange(it) },
            label = { Text(stringResource(R.string.busca_placeholder)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is BuscaUiState.Idle -> {
                CenteredMessage(assetName = "cat_soneca.lottie", message = stringResource(R.string.busca_ociosa))
            }
            is BuscaUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is BuscaUiState.Error -> {
                CenteredMessage(assetName = "cat_erro.lottie", message = stringResource(R.string.busca_erro))
            }
            is BuscaUiState.Success -> {
                if (state.resultados.isEmpty()) {
                    CenteredMessage(assetName = "cat_triste.lottie", message = stringResource(R.string.busca_sem_resultados))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.resultados) { poesia ->
                            PoesiaSearchResultItem(poesia = poesia, onClick = { navController.navigate(Screen.LeitorPoesia.createRoute(poesia.id)) })
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PoesiaSearchResultItem(poesia: Poesia, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(poesia.titulo) },
        supportingContent = { Text(poesia.texto, maxLines = 2) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun CenteredMessage(assetName: String, message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedAsset(
            modifier = Modifier.size(150.dp),
            assetName = assetName
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message)
    }
}
