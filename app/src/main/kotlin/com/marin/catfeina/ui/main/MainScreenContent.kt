/*
 *  Projeto: Catfeina
 *  Arquivo: MainScreenContent.kt
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

package com.marin.catfeina.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.state.DrawableStateImage
import com.github.panpf.sketch.transform.CircleCropTransformation
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.R
import com.marin.catfeina.core.ui.CatAnimation
import com.marin.catfeina.core.utils.Icones
import com.marin.catfeina.sqldelight.GetPoesiaAleatoria
import com.marin.catfeina.sqldelight.GetPoesiasCompletas
import com.marin.catfeina.sqldelight.GetPoesiasFavoritas
import java.io.File


@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel(),
    onPoesiaClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CatAnimation(
                modifier = Modifier.size(200.dp),
                animationResId = R.raw.cat_aguarde
            )
        }
    } else {
        InicioScreenSuccess(
            modifier = modifier,
            uiState = uiState,
            onPoesiaClick = onPoesiaClick
        )
    }
}

@Composable
private fun InicioScreenSuccess(
    modifier: Modifier = Modifier,
    uiState: InicioUiState,
    onPoesiaClick: (Long) -> Unit
) {
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
        if (uiState.todasAsPoesias.isNotEmpty()) {
            item { BlocoPoesias(uiState.todasAsPoesias, onPoesiaClick) }
        }
        item { BlocoNovidades() }
    }
}

@Composable
private fun PoesiaDestaque(poesia: GetPoesiaAleatoria, onPoesiaClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        onClick = onPoesiaClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
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
                    transformations(CircleCropTransformation())
                    placeholder(DrawableStateImage(R.drawable.ic_launcher_foreground))
                    error(DrawableStateImage(R.drawable.ic_launcher_background))
                    crossfade(true)
                },
                contentDescription = poesia.titulo,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))
            Text(poesia.titulo, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                poesia.texto,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BlocoFavoritos(favoritas: List<GetPoesiasFavoritas>, onPoesiaClick: (Long) -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Favoritos", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))
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
                        placeholder(DrawableStateImage(R.drawable.ic_launcher_foreground))
                        error(DrawableStateImage(R.drawable.ic_launcher_background))
                        crossfade(true)
                    },
                    contentDescription = poesia.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onPoesiaClick(poesia.id) }
                )
            }
        }
    }
}

@Composable
private fun BlocoPoesias(poesias: List<GetPoesiasCompletas>, onPoesiaClick: (Long) -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Poesias", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))
        poesias.take(5).forEach { poesia ->
            Card(
                onClick = { onPoesiaClick(poesia.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
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
                            placeholder(DrawableStateImage(R.drawable.ic_launcher_foreground))
                            error(DrawableStateImage(R.drawable.ic_launcher_background))
                            crossfade(true)
                        },
                        contentDescription = poesia.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(Modifier.width(16.dp))

                    // Coluna para o título e subtítulo
                    Column(modifier = Modifier.weight(1f)) {
                        Text(poesia.titulo, fontWeight = FontWeight.Bold)
                        Text(
                            text = poesia.textoBase,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    // Ícones de estado
                    if (poesia.isFavorito == 1L) {
                        Icon(
                            imageVector = Icones.FavoritoCheio,
                            contentDescription = "Favorito",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                    }

                    if (poesia.isLido == 1L) {
                        Icon(
                            imageVector = Icones.JaLido,
                            contentDescription = "Já Lido",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                }
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
