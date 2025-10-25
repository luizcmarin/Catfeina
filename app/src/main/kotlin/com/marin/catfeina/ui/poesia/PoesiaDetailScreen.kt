/*
 *  Projeto: Catfeina
 *  Arquivo: PoesiaDetailScreen.kt
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

package com.marin.catfeina.ui.poesia

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.core.formatador.ElementoConteudo
import com.marin.catfeina.core.formatador.RenderizarElementoConteudo
import com.marin.catfeina.core.formatador.TextoFormatadoViewModel
import com.marin.catfeina.core.formatador.TooltipHandler
import com.marin.catfeina.core.formatador.parser.ParserTextoFormatado
import com.marin.catfeina.core.temas.TemasViewModel
import com.marin.catfeina.core.ui.CatAnimation
import com.marin.catfeina.core.utils.Icones
import com.marin.catfeina.core.utils.PT_BR_LOCALE
import com.marin.catfeina.sqldelight.GetPoesiaCompletaById
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoesiaDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: PoesiaDetailViewModel = hiltViewModel(),
    temasViewModel: TemasViewModel = hiltViewModel(),
    textoFormatadoViewModel: TextoFormatadoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val temasUiState by temasViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    DisposableEffect(context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = PT_BR_LOCALE
            }
        }
        onDispose { tts?.shutdown() }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()
    val isScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    val showBars = !(temasUiState.isFullScreen && isScrolled)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedVisibility(
                visible = showBars,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                TopAppBar(
                    title = {
                        if (isScrolled) {
                            Text(
                                uiState.poesia?.titulo ?: "",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                        }
                    },
                    windowInsets = WindowInsets(0.dp),
                    modifier = Modifier.height(48.dp),

                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.8f
                        )
                    )
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBars,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomAppBar {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = {
                                val text = uiState.poesia?.let { p ->
                                    val ttsText = StringBuilder()
                                    ttsText.append(p.titulo).append(". ")
                                    ttsText.append(
                                        textoFormatadoViewModel.parser.extrairTextoPuroParaTTS(
                                            p.textoBase
                                        )
                                    ).append(". ")
                                    ttsText.append(
                                        textoFormatadoViewModel.parser.extrairTextoPuroParaTTS(
                                            p.texto
                                        )
                                    ).append(". ")
                                    p.textoFinal?.let { tf ->
                                        ttsText.append(
                                            "Para meditar: ${
                                                textoFormatadoViewModel.parser.extrairTextoPuroParaTTS(
                                                    tf
                                                )
                                            }"
                                        ).append(". ")
                                    }
                                    p.nota?.let { n ->
                                        ttsText.append(
                                            "Informação adicional: ${
                                                textoFormatadoViewModel.parser.extrairTextoPuroParaTTS(
                                                    n
                                                )
                                            }"
                                        ).append(". ")
                                    }
                                    ttsText.toString()
                                } ?: ""
                                tts?.speak(
                                    text,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    null
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ) { Icon(Icones.TocarTTS, "Ouvir") }
                        IconButton(
                            onClick = {
                                uiState.poesia?.let {
                                    sharePoesia(
                                        context,
                                        it,
                                        textoFormatadoViewModel.parser
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) { Icon(Icones.Compartilhar, "Compartilhar") }
                        IconButton(
                            onClick = { viewModel.onToggleFavorito() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (uiState.poesia?.isFavorito == 1L) Icones.FavoritoCheio else Icones.FavoritoVazio,
                                contentDescription = "Favoritar",
                                tint = if (uiState.poesia?.isFavorito == 1L) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { viewModel.onToggleLido() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (uiState.poesia?.isLido == 1L) Icones.JaLido else Icones.NaoLido,
                                contentDescription = "Marcar como lido",
                                tint = if (uiState.poesia?.isLido == 1L) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    CatAnimation(
                        modifier = Modifier.size(200.dp),
                        assetName = "cat_carregando.lottie",
                    )
                }

                uiState.error != null -> {
                    Text(uiState.error!!, Modifier.align(Alignment.Center))
                }

                uiState.poesia != null -> {
                    PoesiaDetailContent(
                        poesia = uiState.poesia!!,
                        lazyListState = listState,
                        textSizeMultiplier = temasUiState.textSize,
                        onNotaUsuarioChange = { viewModel.updateNotaUsuario(it) },
                        parser = textoFormatadoViewModel.parser
                    )
                }
            }
        }
    }
}

@Composable
private fun PoesiaDetailContent(
    poesia: GetPoesiaCompletaById,
    lazyListState: androidx.compose.foundation.lazy.LazyListState,
    textSizeMultiplier: Float,
    onNotaUsuarioChange: (String) -> Unit,
    parser: ParserTextoFormatado
) {
    val context = LocalContext.current
    val baseFontSize = 16.sp
    val finalFontSize = baseFontSize * (1 + textSizeMultiplier)

    val tooltipHandler = remember { TooltipHandler {} }

    var notaUsuario by remember(poesia.notaUsuario) {
        mutableStateOf(
            poesia.notaUsuario ?: ""
        )
    }

    val dateFormat =
        remember { SimpleDateFormat("dd/MM/yyyy HH:mm", PT_BR_LOCALE) }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            val imageFile = poesia.imagem?.let {
                File(
                    context.filesDir,
                    "${BuildConfig.SYNC_IMAGE_FOLDER_NAME}/$it"
                )
            }
            if (imageFile?.exists() == true) {
                AsyncImage(
                    request = ImageRequest(context, imageFile.path) {
                        crossfade(true)
                    },
                    contentDescription = "Imagem da Poesia",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        item {
            RenderizarElementoConteudo(
                parser.parse(poesia.titulo).first(),
                finalFontSize * 1.5f,
                tooltipHandler
            )
            Spacer(Modifier.height(8.dp))
        }

        if (poesia.textoBase.isNotBlank()) {
            items(parser.parse(poesia.textoBase)) {
                RenderizarElementoConteudo(
                    it,
                    finalFontSize * 1.2f,
                    tooltipHandler
                )
            }
        }

        items(parser.parse(poesia.texto)) {
            RenderizarElementoConteudo(
                it,
                finalFontSize,
                tooltipHandler
            )
        }

        if (poesia.textoFinal?.isNotBlank() == true) {
            item { Spacer(Modifier.height(16.dp)) }
            items(parser.parse(poesia.textoFinal)) {
                RenderizarElementoConteudo(
                    it,
                    finalFontSize,
                    tooltipHandler
                )
            }
        }

        if (poesia.nota?.isNotBlank() == true) {
            item { Spacer(Modifier.height(16.dp)) }
            items(parser.parse(poesia.nota)) {
                RenderizarElementoConteudo(
                    it,
                    finalFontSize,
                    tooltipHandler
                )
            }
        }

        item { Spacer(Modifier.height(16.dp)) }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Autor: ${poesia.autor}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Categoria: ${poesia.categoria}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Data: ${dateFormat.format(Date(poesia.dataCriacao))}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item { Spacer(Modifier.height(32.dp)) }

        item {
            OutlinedTextField(
                value = notaUsuario,
                onValueChange = {
                    notaUsuario = it
                    onNotaUsuarioChange(it)
                },
                label = { Text("Sua nota pessoal") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private val ElementoConteudo.textoParaLeitura: String
    get() = when (this) {
        is ElementoConteudo.Paragrafo -> this.textoCru
        is ElementoConteudo.Cabecalho -> this.texto
        is ElementoConteudo.Citacao -> this.texto
        is ElementoConteudo.ItemLista -> this.textoItem
        is ElementoConteudo.Imagem -> this.textoAlternativo ?: ""
        is ElementoConteudo.LinhaHorizontal -> ""
    }

private fun sharePoesia(
    context: Context,
    poesia: GetPoesiaCompletaById,
    parser: ParserTextoFormatado
) {
    val shareText = """
*${poesia.titulo}*

${
        parser.parse(poesia.textoBase)
            .joinToString(separator = "") { it.textoParaLeitura }
    }

${
        parser.parse(poesia.texto)
            .joinToString(separator = "") { it.textoParaLeitura }
    }

${
        poesia.textoFinal?.let {
            "" + parser.parse(it)
                .joinToString(separator = "") { item -> item.textoParaLeitura }
        } ?: ""
    }

Autor: ${poesia.autor}
Enviado com ❤️ pelo app Catfeina
""".trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Compartilhar Poesia"))
}
