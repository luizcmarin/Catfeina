/*
 *  Projeto: Catfeina
 *  Arquivo: PoesiaListScreen.kt
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.state.DrawableStateImage
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.R
import com.marin.catfeina.core.ui.CatAnimation
import com.marin.catfeina.core.utils.Icones
import java.io.File

@Composable
fun PoesiaListScreen(
    uiState: InicioUiState,
    onPoesiaClick: (Long) -> Unit
) {
    val context = LocalContext.current

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
    } else if (uiState.todasAsPoesias.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Nenhuma poesia encontrada.")
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(items = uiState.todasAsPoesias, key = { it.id }) { poesia ->
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
}