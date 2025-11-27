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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
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
import java.io.File

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val poesias = viewModel.poesiasPaginadas.collectAsLazyPagingItems()

    if (uiState.isLoading) {
        InicioScreenPlaceholder(modifier = modifier)
    } else {
        InicioScreenSuccess(
            modifier = modifier,
            uiState = uiState,
            poesias = poesias,
            navController = navController
        )
    }
}

@Composable
private fun InicioScreenPlaceholder(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        // Placeholder for PoesiaDestaque
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
                        .placeholder(isLoading = true)
                )
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(28.dp)
                        .align(Alignment.CenterHorizontally)
                        .placeholder(isLoading = true)
                )
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth(0.9f).height(16.dp).placeholder(isLoading = true))
                    Box(modifier = Modifier.fillMaxWidth(0.95f).height(16.dp).placeholder(isLoading = true))
                    Box(modifier = Modifier.fillMaxWidth(0.85f).height(16.dp).placeholder(isLoading = true))
                }
            }
        }
        // Placeholder for BlocoFavoritos
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .width(120.dp)
                        .height(24.dp)
                        .placeholder(isLoading = true)
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
                                .placeholder(isLoading = true)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Placeholder for "Poesias" title
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .width(100.dp)
                    .height(24.dp)
                    .placeholder(isLoading = true)
            )
        }

        // Placeholders for list items
        items(5) {
            PoesiaListItemPlaceholder()
        }
    }
}


@Composable
private fun InicioScreenSuccess(
    modifier: Modifier = Modifier,
    uiState: InicioUiState,
    poesias: LazyPagingItems<Poesia>,
    navController: NavHostController
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            uiState.poesiaAleatoria?.let { poesia ->
                PoesiaDestaque(poesia) { navController.navigate(Screen.LeitorPoesia.createRoute(poesia.id)) }
            }
        }
        if (uiState.poesiasFavoritas.isNotEmpty()) {
            item { BlocoFavoritos(uiState.poesiasFavoritas, navController) }
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
                PoesiaListItem(poesia, navController)
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

@Composable
private fun PoesiaListItem(poesia: Poesia, navController: NavHostController) {
    val context = LocalContext.current
    Card(
        onClick = { navController.navigate(Screen.LeitorPoesia.createRoute(poesia.id)) },
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
private fun BlocoFavoritos(favoritas: List<Poesia>, navController: NavHostController) {
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
                        .clickable { navController.navigate(Screen.LeitorPoesia.createRoute(poesia.id)) },
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
