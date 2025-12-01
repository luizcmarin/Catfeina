/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/inicio/InicioScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela inicial do aplicativo.
*
*/
package com.marin.catfeina.ui.telas.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.marin.catfeina.Screen
import com.marin.core.util.cliqueSeguro

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is InicioUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is InicioUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message)
                }
            }
            is InicioUiState.Success -> {
                InicioScreenContent(
                    uiState = state,
                    onPoesiaClick = { id -> navController.navigate(Screen.LeitorPoesia.createRoute(id)) }
                )
            }
        }
    }
}

@Composable
private fun InicioScreenContent(
    uiState: InicioUiState.Success,
    onPoesiaClick: (Long) -> Unit
) {
    val outrasPoesias = uiState.outrasPoesias.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        uiState.poesiaDestaque?.let {
            PoesiaDestaque(poesia = it, onClick = { onPoesiaClick(it.id) })
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.poesiasFavoritas.isNotEmpty()) {
            PoesiaFavoritosList(
                titulo = "Favoritas",
                poesias = uiState.poesiasFavoritas,
                onPoesiaClick = onPoesiaClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        PoesiaPaginadaList(
            titulo = "Recentes",
            poesias = outrasPoesias,
            onPoesiaClick = onPoesiaClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoesiaDestaque(poesia: PoesiaUiModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .cliqueSeguro(onClick = onClick)
    ) {
        Column {
            poesia.imagem?.let {
                AsyncImage(
                    request = ImageRequest(LocalContext.current, it),
                    contentDescription = poesia.titulo,
                    modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = poesia.titulo, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun PoesiaFavoritosList(
    titulo: String,
    poesias: List<PoesiaUiModel>,
    onPoesiaClick: (Long) -> Unit
) {
    Column {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(poesias.size) { index ->
                val poesia = poesias[index]
                PoesiaListItem(poesia = poesia, onClick = { onPoesiaClick(poesia.id) })
            }
        }
    }
}

@Composable
fun PoesiaPaginadaList(
    titulo: String,
    poesias: LazyPagingItems<PoesiaUiModel>,
    onPoesiaClick: (Long) -> Unit
) {
    Column {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(poesias.itemCount) { index ->
                val poesia = poesias[index]
                poesia?.let {
                    PoesiaListItem(poesia = it, onClick = { onPoesiaClick(it.id) })
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoesiaListItem(poesia: PoesiaUiModel, onClick: () -> Unit) {
    Card(modifier = Modifier.width(200.dp).cliqueSeguro(onClick = onClick)) {
        Column {
            poesia.imagem?.let {
                AsyncImage(
                    request = ImageRequest(LocalContext.current, it),
                    contentDescription = poesia.titulo,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = poesia.titulo, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = poesia.resumo, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
