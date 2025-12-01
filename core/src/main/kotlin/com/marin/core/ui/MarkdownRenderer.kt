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
import androidx.compose.ui.unit.dp
import org.commonmark.node.Heading
import org.commonmark.node.Image
import org.commonmark.node.Node
import org.commonmark.node.Paragraph
import org.commonmark.parser.Parser

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

    Column(modifier = modifier) {
        var node = root.firstChild
        while (node != null) {
            when (node) {
                is Heading -> {
                    val style = when (node.level) {
                        1 -> MaterialTheme.typography.headlineLarge
                        2 -> MaterialTheme.typography.headlineMedium
                        3 -> MaterialTheme.typography.headlineSmall
                        else -> MaterialTheme.typography.titleLarge
                    }
                    val text = (node.firstChild as? org.commonmark.node.Text)?.literal ?: ""
                    if (text.isNotEmpty()) {
                        Text(
                            text = text,
                            style = style,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
                is Paragraph -> {
                    val firstChild = node.firstChild
                    if (firstChild is Image) {
                        imageComposable(
                            (firstChild.firstChild as? org.commonmark.node.Text)?.literal ?: "",
                            firstChild.destination
                        )
                    } else {
                        val text = (node.firstChild as? org.commonmark.node.Text)?.literal ?: ""
                        if (text.isNotEmpty()) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                }
            }
            node = node.next
        }
    }
}
