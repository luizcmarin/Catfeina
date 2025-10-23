package com.marin.catfeina.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.state.DrawableStateImage
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.R
import com.marin.catfeina.core.ui.CatAnimation
import com.marin.catfeina.core.utils.Icones
import com.marin.catfeina.sqldelight.GetPoesiasPaginadas
import java.io.File

@Composable
fun PoesiaListScreen(
    viewModel: MainScreenViewModel,
    onPoesiaClick: (Long) -> Unit
) {
    val context = LocalContext.current
    // Colete o flow de paginação como itens para a LazyColumn
    val poesias: LazyPagingItems<GetPoesiasPaginadas> =
        viewModel.poesiasPaginadas.collectAsLazyPagingItems()

    // Lida com os estados de carregamento, erro e vazio
    when (poesias.loadState.refresh) {
        is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CatAnimation(modifier = Modifier.size(200.dp), assetName = "cat_carregando.lottie")
            }
        }
        is LoadState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ocorreu um erro ao carregar as poesias.")
            }
        }
        else -> {
            if (poesias.itemCount == 0) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Nenhuma poesia encontrada.")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Use o itemCount e o indexer do LazyPagingItems
                    items(
                        count = poesias.itemCount,
                        key = { index -> poesias.peek(index)?.id ?: index }
                    ) { index ->
                        val poesia = poesias[index]
                        if (poesia != null) {
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

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(poesia.titulo, fontWeight = FontWeight.Bold)
                                        if (poesia.textoBase.isNotBlank()) {
                                            Text(
                                                text = viewModel.extrairTextoPuro(poesia.textoBase),
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    Spacer(Modifier.width(16.dp))

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
                    // Adiciona indicadores de carregamento no final da lista
                    if (poesias.loadState.append is LoadState.Loading) {
                        item {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}