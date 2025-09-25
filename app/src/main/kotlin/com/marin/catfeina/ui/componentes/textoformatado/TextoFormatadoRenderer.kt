/*
 * Arquivo: com.marin.catfeina.ui.componentes.textoformatado.TextoFormatadoRenderer.kt
 * @project Catfeina
 * @description
 * Contém o Composable reutilizável para renderizar uma lista de ElementoConteudo,
 * convertendo-os em componentes visuais formatados.
 */
package com.marin.catfeina.ui.componentes.textoformatado

// Imports necessários para RenderizarElementoConteudo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.marin.catfeina.R // Certifique-se que seus drawables de placeholder/erro estão aqui

@Composable
fun RenderizarElementoConteudo( // Removido 'private'
    elemento: ElementoConteudo,
    fontSize: TextUnit,
    tooltipHandler: TooltipHandler, // Alterado para a interface
    // uriHandler é obtido via LocalUriHandler abaixo e não precisa ser parâmetro
) {
    val baseTextStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = fontSize)
    val localUriHandler = LocalUriHandler.current // Obtém o UriHandler localmente

    when (elemento) {
        is ElementoConteudo.Paragrafo, is ElementoConteudo.ItemLista -> {
            val textoCru =
                if (elemento is ElementoConteudo.Paragrafo) elemento.textoCru else (elemento as ElementoConteudo.ItemLista).textoItem
            val aplicacoesEmLinha =
                if (elemento is ElementoConteudo.Paragrafo) elemento.aplicacoesEmLinha else (elemento as ElementoConteudo.ItemLista).aplicacoesEmLinha

            if (textoCru.isNotBlank() || aplicacoesEmLinha.isNotEmpty()) {
                val annotatedString = buildAnnotatedString {
                    append(textoCru)
                    aplicacoesEmLinha.forEach { aplicacao ->
                        // Adicionada verificação de validade do intervalo
                        if (aplicacao.intervalo.first >= 0 && aplicacao.intervalo.last < textoCru.length && aplicacao.intervalo.first <= aplicacao.intervalo.last) {
                            val start = aplicacao.intervalo.first
                            val endExclusive = aplicacao.intervalo.endInclusive + 1
                            when (aplicacao) {
                                is AplicacaoSpanStyle -> {
                                    var currentStyle = SpanStyle()
                                    aplicacao.fontWeight?.let { currentStyle = currentStyle.copy(fontWeight = it) }
                                    aplicacao.fontStyle?.let { currentStyle = currentStyle.copy(fontStyle = it) }
                                    aplicacao.textDecoration?.let { currentStyle = currentStyle.copy(textDecoration = it) }
                                    if (aplicacao.isDestaque) {
                                        currentStyle = currentStyle.copy(
                                            background = MaterialTheme.colorScheme.tertiaryContainer,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                    }
                                    addStyle(style = currentStyle, start = start, end = endExclusive)
                                }
                                is AplicacaoAnotacaoLink -> {
                                    val link = LinkAnnotation.Url(
                                        url = aplicacao.url,
                                        linkInteractionListener = { /* Opcional */ }
                                    )
                                    addLink(link, start, endExclusive)
                                    addStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            textDecoration = TextDecoration.Underline
                                        ), start, endExclusive
                                    )
                                }
                                is AplicacaoAnotacaoTooltip -> {
                                    val tooltipAnnotation = LinkAnnotation.Clickable(
                                        tag = aplicacao.tagAnotacao, // Usar a tag da aplicação
                                        linkInteractionListener = {
                                            tooltipHandler.mostrarTooltip(aplicacao.textoTooltip)
                                        }
                                    )
                                    addLink(tooltipAnnotation, start, endExclusive)
                                    addStyle(
                                        SpanStyle(
                                            textDecoration = TextDecoration.Underline,
                                            color = MaterialTheme.colorScheme.tertiary
                                        ), start, endExclusive
                                    )
                                }
                            }
                        }
                    }
                }

                val textContent = @Composable {
                    Text(
                        text = annotatedString,
                        style = baseTextStyle.copy(textAlign = TextAlign.Start),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (elemento is ElementoConteudo.ItemLista) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "• ", style = baseTextStyle)
                        Box(modifier = Modifier.weight(1f)) { textContent() }
                    }
                } else {
                    textContent()
                }
            }
        }

        is ElementoConteudo.Cabecalho -> {
            val style = when (elemento.nivel) {
                1 -> MaterialTheme.typography.headlineLarge
                2 -> MaterialTheme.typography.headlineMedium
                3 -> MaterialTheme.typography.headlineSmall
                4 -> MaterialTheme.typography.titleLarge
                5 -> MaterialTheme.typography.titleMedium
                6 -> MaterialTheme.typography.titleSmall
                else -> baseTextStyle.copy(fontWeight = FontWeight.Bold)
            }.copy(fontSize = fontSize *
                    when (elemento.nivel) {
                        1 -> 1.5f; 2 -> 1.3f; 3 -> 1.15f; 4 -> 1.1f; else -> 1.0f
                    }
            )
            Text(
                text = elemento.texto,
                style = style,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }

        is ElementoConteudo.Imagem -> {
            // Supondo que você tenha drawables genéricos de placeholder e erro.
            // Se não, remova .error e .placeholder ou substitua por seus drawables.
            val placeholderDrawable = R.drawable.ic_launcher_foreground // Substitua se necessário
            val errorDrawable = R.drawable.ic_launcher_background   // Substitua se necessário

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/catfeina/informativos/${elemento.nomeArquivo}")
                        .crossfade(true)
                        .placeholder(placeholderDrawable)
                        .error(errorDrawable)
                        .build()
                ),
                contentDescription = elemento.textoAlternativo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(vertical = 8.dp),
                contentScale = ContentScale.Crop
            )
        }

        is ElementoConteudo.Citacao -> {
            Text(
                text = "“${elemento.texto}”",
                style = baseTextStyle.copy(
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            )
        }

        ElementoConteudo.LinhaHorizontal -> {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}
