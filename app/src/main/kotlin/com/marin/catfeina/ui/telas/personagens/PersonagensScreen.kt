/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/personagens/PersonagensScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela de Personagens.
*
*/
package com.marin.catfeina.ui.telas.personagens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.data.models.Personagem
import com.marin.catfeina.ui.componentes.ErrorMessage
import com.marin.catfeina.ui.componentes.LoadingWheel
import java.io.File
import kotlin.math.absoluteValue

@Composable
fun PersonagensScreen(
    modifier: Modifier = Modifier,
    viewModel: PersonagensViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PersonagensScreenContent(modifier = modifier, uiState = uiState)
}

@Composable
private fun PersonagensScreenContent(
    modifier: Modifier = Modifier,
    uiState: PersonagensUiState
) {
    when (uiState) {
        is PersonagensUiState.Loading -> LoadingWheel()
        is PersonagensUiState.Error -> ErrorMessage()
        is PersonagensUiState.Success -> {
            if (uiState.personagens.isEmpty()) {
                // TODO: Adicionar um estado para quando a lista está vazia
            } else {
                PersonagensCarousel(modifier = modifier, personagens = uiState.personagens)
            }
        }
    }
}

@Composable
fun PersonagensCarousel(modifier: Modifier = Modifier, personagens: List<Personagem>) {
    val pagerState = rememberPagerState { personagens.size }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            PersonagemCard(
                personagem = personagens[page],
                modifier = Modifier.graphicsLayer {
                    // Efeito de transição de escala e alfa
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
                    scaleY = lerp(0.8f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
                }
            )
        }

        // Indicador de página manual
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) { 
            repeat(personagens.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun PersonagemCard(personagem: Personagem, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    Card(
        modifier = modifier.padding(16.dp).fillMaxSize(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
             val imageFile = personagem.imagem?.let {
                File(context.filesDir, "${BuildConfig.SYNC_IMAGE_FOLDER_NAME}/$it")
            }
            AsyncImage(
                request = ImageRequest(context, imageFile?.path),
                contentDescription = personagem.nome,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(text = personagem.nome, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = personagem.biografia, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true, name = "Personagens - Com Dados")
@Composable
private fun PersonagensScreenPreview_Success() {
    val personagensMock = listOf(
        Personagem(1, "Romrom", "Mascote oficial do Catfeina, um gato curioso e amigável que adora poesias e um bom café.", null, 0L),
        Personagem(2, "Siamês", "Um gato elegante e misterioso, guardião de segredos antigos e poesias esquecidas.", null, 0L)
    )
    PersonagensScreenContent(uiState = PersonagensUiState.Success(personagensMock))
}
