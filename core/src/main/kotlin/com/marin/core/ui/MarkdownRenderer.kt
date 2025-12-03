/*
*  Projeto: Catfeina/Catverso
*  Arquivo: core/src/main/kotlin/com/marin/core/ui/MarkdownRenderer.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable que renderiza texto Markdown para componentes nativos do Jetpack Compose.
*
*/
package com.marin.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.commonmark.node.HardLineBreak
import org.commonmark.node.Heading
import org.commonmark.node.Image
import org.commonmark.node.Node
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.Text
import org.commonmark.parser.Parser
import java.io.File

/**
 * Renderiza uma string de Markdown em Composables.
 *
 * @param markdown O texto Markdown a ser renderizado.
 * @param modifier O modificador a ser aplicado ao layout da coluna.
 * @param imageComposable Um Composable customizado para renderizar nós de imagem.
 */
@Composable
fun MarkdownRenderer(
    markdown: String,
    modifier: Modifier = Modifier,
    imageComposable: @Composable (description: String, url: String) -> Unit = { _, _ -> /* Por padrão, não renderiza imagem */ },
) {
    val parser = Parser.builder().build()
    val root = parser.parse(markdown)
    val context = LocalContext.current

    Column(modifier = modifier) {
        var node = root.firstChild
        while (node != null) {
            when (node) {
                is Heading -> {
                    RenderHeading(node)
                }
                is Paragraph -> {
                    RenderParagraph(node, context, imageComposable)
                }
            }
            node = node.next
        }
    }
}

@Composable
private fun RenderHeading(heading: Heading) {
    val style = when (heading.level) {
        1 -> MaterialTheme.typography.headlineLarge
        2 -> MaterialTheme.typography.headlineMedium
        3 -> MaterialTheme.typography.headlineSmall
        else -> MaterialTheme.typography.titleLarge
    }
    val text = (heading.firstChild as? Text)?.literal ?: ""
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = style,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun RenderParagraph(
    paragraph: Paragraph,
    context: android.content.Context,
    imageComposable: @Composable (description: String, url: String) -> Unit
) {
    val textAccumulator = StringBuilder()

    @Composable
    fun flushText() {
        if (textAccumulator.isNotEmpty()) {
            Text(
                text = textAccumulator.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            textAccumulator.clear()
        }
    }

    fun extractText(node: Node?): String {
        if (node == null) return ""
        if (node is Text) return node.literal
        if (node is Image) return ""

        val sb = StringBuilder()
        var child = node.firstChild
        while (child != null) {
            sb.append(extractText(child))
            child = child.next
        }
        return sb.toString()
    }

    var pChild = paragraph.firstChild
    while (pChild != null) {
        when (pChild) {
            is Image -> {
                flushText()
                val destination = pChild.destination
                val url = if (destination.startsWith("http")) {
                    destination
                } else {
                    val imageFile = File(context.filesDir, "images/$destination")
                    imageFile.absolutePath
                }
                imageComposable(
                    (pChild.firstChild as? Text)?.literal ?: "",
                    url
                )
            }
            is Text -> {
                textAccumulator.append(pChild.literal)
            }
            is SoftLineBreak -> {
                textAccumulator.append('\n')
            }
            is HardLineBreak -> {
                textAccumulator.append("\n\n")
            }
            else -> {
                textAccumulator.append(extractText(pChild))
            }
        }
        pChild = pChild.next
    }
    flushText()
}
