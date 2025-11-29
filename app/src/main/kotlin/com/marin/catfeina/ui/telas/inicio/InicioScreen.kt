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
*  Nota: Tela principal do app, que exibe a poesia em destaque, favoritos e a lista paginada.
*
*/
package com.marin.catfeina.ui.telas.inicio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.state.DrawableStateImage
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.R
import com.marin.catfeina.Screen
import com.marin.catfeina.data.models.Poesia
import com.marin.core.ui.Icones
import com.marin.core.util.placeholder
import kotlinx.coroutines.flow.flowOf
import java.io.File

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val poesias = viewModel.poesiasPaginadas.collectAsLazyPagingItems()

    InicioScreenContent(
        modifier = modifier,
        uiState = uiState,
        poesias = poesias,
        onPoesiaClick = { poesiaId ->
            navController.navigate(Screen.LeitorPoesia.createRoute(poesiaId))
        }
    )
}

@Composable
private fun InicioScreenContent(
    modifier: Modifier = Modifier,
    uiState: InicioUiState,
    poesias: LazyPagingItems<Poesia>,
    onPoesiaClick: (id: Long) -> Unit
) {
    if (uiState.isLoading) {
        InicioScreenPlaceholder(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                uiState.poesiaAleatoria?.let { poesia ->
                    PoesiaDestaque(poesia) { onPoesiaClick(poesia.id) }
                }
            }
            if (uiState.poesiasFavoritas.isNotEmpty()) {
                item { BlocoFavoritos(uiState.poesiasFavoritas, onPoesiaClick) }
            }

            if (poesias.itemCount > 0) {
                item {
                    Text(
                        text = "Poesias",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }
            }

            items(
                count = poesias.itemCount,
                key = { index -> poesias.peek(index)?.id ?: index }
            ) { index ->
                val poesia = poesias[index]
                if (poesia != null) {
                    PoesiaListItem(poesia, onPoesiaClick)
                } else {
                    PoesiaListItemPlaceholder()
                }
            }

            if (poesias.loadState.append is LoadState.Loading) {
                item {
                    PoesiaListItemPlaceholder()
                }
            }

            item { BlocoNovidades() }
        }
    }
}

@Composable
private fun InicioScreenPlaceholder(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().placeholder(isLoading = true),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(28.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth(0.9f).height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(0.95f).height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(0.85f).height(16.dp))
                }
            }
        }
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .width(120.dp)
                        .height(24.dp)
                )
                Spacer(Modifier.height(16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(3) {
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .aspectRatio(4f / 3f)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .width(100.dp)
                    .height(24.dp)
            )
        }

        items(5) {
            PoesiaListItemPlaceholder()
        }
    }
}

@Composable
private fun PoesiaListItem(poesia: Poesia, onPoesiaClick: (id: Long) -> Unit) {
    val context = LocalContext.current
    Card(
        onClick = { onPoesiaClick(poesia.id) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageFile = poesia.imagem?.let {
                File(context.filesDir, "${BuildConfig.SYNC_IMAGE_FOLDER_NAME}/$it")
            }
            AsyncImage(
                request = ImageRequest(context, imageFile?.path) {
                    error(DrawableStateImage(R.drawable.ic_launcher_foreground))
                    crossfade(true)
                },
                contentDescription = poesia.titulo,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(poesia.titulo, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)

                if (poesia.textobase.isNotBlank()) {
                    Text(
                        text = poesia.textobase,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            if (poesia.favorita) {
                Icon(
                    imageVector = Icones.FavoritoCheio,
                    contentDescription = "Favorito",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
            }

            if (poesia.lida) {
                Icon(
                    imageVector = Icones.JaLido,
                    contentDescription = "Já Lido",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun PoesiaListItemPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .placeholder(isLoading = true)
            )
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(20.dp).placeholder(isLoading = true))
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(16.dp).placeholder(isLoading = true))
            }
        }
    }
}


@Composable
private fun PoesiaDestaque(poesia: Poesia, onPoesiaClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        onClick = onPoesiaClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageFile = poesia.imagem?.let {
                File(context.filesDir, "${BuildConfig.SYNC_IMAGE_FOLDER_NAME}/$it")
            }
            AsyncImage(
                request = ImageRequest(context, imageFile?.path) {
                    error(DrawableStateImage(R.drawable.ic_launcher_foreground))
                    crossfade(true)
                },
                contentDescription = poesia.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))
            Text(poesia.titulo, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                poesia.textobase,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BlocoFavoritos(favoritas: List<Poesia>, onPoesiaClick: (id: Long) -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Favoritos", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = favoritas, key = { it.id }) { poesia ->
                val imageFile = poesia.imagem?.let {
                    File(context.filesDir, "${BuildConfig.SYNC_IMAGE_FOLDER_NAME}/$it")
                }
                AsyncImage(
                    request = ImageRequest(context, imageFile?.path) {
                        error(DrawableStateImage(R.drawable.ic_launcher_foreground))
                        crossfade(true)
                    },
                    contentDescription = poesia.titulo,
                    modifier = Modifier
                        .width(200.dp)
                        .aspectRatio(4f / 3f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onPoesiaClick(poesia.id) },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun BlocoNovidades() {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Novidades",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(horizontal = 16.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Conteúdo de Novidades")
            }
        }
    }
}

@Preview(showBackground = true, name = "Início - Carregando")
@Composable
private fun InicioScreenPreview_Loading() {
    InicioScreenContent(
        uiState = InicioUiState(isLoading = true),
        poesias = flowOf(PagingData.empty<Poesia>()).collectAsLazyPagingItems(),
        onPoesiaClick = {}
    )
}

@Preview(showBackground = true, name = "Início - Com Conteúdo")
@Composable
private fun InicioScreenPreview_Success() {
    val poesiasMock = listOf(
        Poesia(1, "Poesia 1", "Texto 1", null, "Autor 1", "Nota 1", "Base 1", "Final 1", null, 2, 0L, false, true, 0L, null),
        Poesia(2, "Poesia 2", "Texto 2", null, "Autor 2", "Nota 2", "Base 2", "Final 2", 1, null, 0L, true, false, 0L, "Nota do usuário")
    )
    InicioScreenContent(
        uiState = InicioUiState(
            isLoading = false,
            poesiaAleatoria = poesiasMock[0],
            poesiasFavoritas = poesiasMock.filter { it.favorita }
        ),
        poesias = flowOf(PagingData.from(poesiasMock)).collectAsLazyPagingItems(),
        onPoesiaClick = {}
    )
}
