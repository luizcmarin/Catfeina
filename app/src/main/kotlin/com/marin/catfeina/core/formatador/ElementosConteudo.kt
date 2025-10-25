/*
 *  Projeto: Catfeina
 *  Arquivo: ElementosConteudo.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Define as classes de dados e interfaces que representam a estrutura
 *        lógica de um texto formatado, após ser processado pelo parser.
 *
 * Propósito:
 * Este arquivo é fundamental para a arquitetura do parser. Ele desacopla a lógica
 * de análise da lógica de renderização. O `ParserTextoFormatado` converte o
 * texto cru em uma lista de `ElementoConteudo` (Parágrafo, Imagem, etc.).
 * O `TextoFormatadoRenderer` então consome essa lista para construir a UI em
 * Jetpack Compose, sem precisar entender a sintaxe das tags originais.
 *
 */

package com.marin.catfeina.core.formatador

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import java.util.UUID

/**
 * Representa um estilo ou anotação a ser aplicado a um trecho de texto dentro de um parágrafo.
 */
sealed interface AplicacaoEmLinha {
    /** O intervalo no texto original do parágrafo onde esta formatação se aplica. */
    var intervalo: IntRange

    /** O texto original que esta aplicação de formatação engloba (sem as tags). */
    val textoOriginal: String
}

/**
 * Representa a aplicação de um SpanStyle (ex: negrito, itálico, cor de fundo para destaque).
 */
data class AplicacaoSpanStyle(
    override val textoOriginal: String,
    override var intervalo: IntRange = IntRange.EMPTY,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val textDecoration: TextDecoration? = null,
    val isDestaque: Boolean = false
) : AplicacaoEmLinha

/**
 * Representa um link clicável a ser anotado no texto.
 */
data class AplicacaoAnotacaoLink(
    override var intervalo: IntRange,
    override val textoOriginal: String,
    val url: String,
    val tagAnotacao: String = "URL_${url.hashCode()}"
) : AplicacaoEmLinha

/**
 * Representa um tooltip (dica de contexto) a ser associado a um trecho de texto.
 */
data class AplicacaoAnotacaoTooltip(
    override var intervalo: IntRange,
    override val textoOriginal: String,
    val textoTooltip: String,
    val tagAnotacao: String = "TOOLTIP_${textoTooltip.hashCode()}"
) : AplicacaoEmLinha

/**
 * Representa um elemento de bloco no conteúdo estruturado.
 * O parser irá converter o texto cru em uma lista de [ElementoConteudo]s.
 */
sealed interface ElementoConteudo {
    val idUnico: String

    data class Paragrafo(
        val textoCru: String,
        val aplicacoesEmLinha: List<AplicacaoEmLinha>,
        override val idUnico: String = UUID.randomUUID().toString()
    ) : ElementoConteudo

    data class Cabecalho(
        val nivel: Int,
        val texto: String,
        override val idUnico: String = UUID.randomUUID().toString()
    ) : ElementoConteudo

    data class Imagem(
        val nomeArquivo: String,
        val textoAlternativo: String? = null,
        override val idUnico: String = UUID.randomUUID().toString()
    ) : ElementoConteudo

    data class Citacao(
        val texto: String,
        override val idUnico: String = UUID.randomUUID().toString()
    ) : ElementoConteudo

    object LinhaHorizontal : ElementoConteudo {
        override val idUnico: String = "ELEMENTO_LINHA_HORIZONTAL_SINGLETON"
    }

    data class ItemLista(
        val textoItem: String,
        val aplicacoesEmLinha: List<AplicacaoEmLinha> = emptyList(),
        override val idUnico: String = UUID.randomUUID().toString()
    ) : ElementoConteudo
}
