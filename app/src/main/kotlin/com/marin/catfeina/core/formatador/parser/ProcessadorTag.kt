/*
 *  Projeto: Catfeina
 *  Arquivo: ProcessadorTag.kt
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

/*
 *
 *  Projeto: Catfeina
 *  Arquivo: ProcessadorTag.kt
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: ProcessadorTag.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
 *
 */

// ===================================================================================
// Arquivo: com.marin.catfeina.core.formatador.parser.ProcessadorTag.kt
//
// Descrição: Define a interface principal e as classes de dados de suporte para o
//            mecanismo de análise (parsing) de tags de formatação.
//
// Propósito:
// Este arquivo é o pilar da arquitetura do parser. A interface `ProcessadorTag`
// estabelece um contrato que permite a criação de processadores especializados
// para cada tipo de tag (ex: negrito, imagem, link). O `ParserTextoFormatado`
// utiliza uma coleção desses processadores para identificar e converter as tags
// do texto cru nos `ElementoConteudo` ou `AplicacaoEmLinha` correspondentes.
// ===================================================================================
package com.marin.catfeina.core.formatador.parser

import com.marin.catfeina.core.formatador.AplicacaoEmLinha
import com.marin.catfeina.core.formatador.ElementoConteudo

/**
 * Contexto fornecido a um [ProcessadorTag] durante o processamento.
 * Pode conter informações úteis como o texto completo original, o índice atual, etc.
 * (Ainda a ser detalhado conforme a necessidade do parser principal)
 */
data class ContextoParsing(
    val textoCompleto: String,
    val indiceAtual: Int
    // Outros campos podem ser adicionados, como uma referência ao parser principal
    // para processamento aninhado ou para registrar erros.
)

/**
 * Resultado do processamento de uma tag.
 * Pode ser um elemento de bloco, uma aplicação em linha, ou indicar que a tag não foi consumida.
 */
sealed interface ResultadoProcessamentoTag {
    /** Indica que a tag foi processada e resultou em um ElementoConteudo de bloco. */
    data class ElementoBloco(val elemento: ElementoConteudo) : ResultadoProcessamentoTag

    /** Indica que a tag foi processada e resultou em uma AplicaçãoEmLinha. */
    data class AplicacaoTagEmLinha(val aplicacao: AplicacaoEmLinha) : ResultadoProcessamentoTag

    /** Indica que a tag não foi reconhecida ou consumida por este processador. */
    object NaoConsumido : ResultadoProcessamentoTag

    /** Indica um erro no processamento da tag. */
    data class Erro(val mensagem: String) : ResultadoProcessamentoTag
}


/**
 * Interface para um processador que sabe como analisar uma ou mais tags específicas
 * e converter seu conteúdo em [ElementoConteudo] ou [AplicacaoEmLinha].
 */
interface ProcessadorTag {
    /**
     * A(s) palavra(s)-chave da(s) tag(s) que este processador manipula.
     * Ex: "n", "i", "t1", "imagem".
     */
    val palavrasChave: Set<String>

    /**
     * Tenta processar uma tag a partir de um determinado ponto no texto.
     *
     * @param palavraChaveTag A palavra-chave da tag identificada (ex: "n" para ::n:texto::).
     * @param conteudoTag O conteúdo interno da tag (ex: "texto" para ::n:texto::, ou "URL|TextoLink" para ::url:URL|TextoLink::).
     * @param contexto O contexto atual do parsing.
     * @return Um [ResultadoProcessamentoTag] indicando o sucesso ou falha.
     */
    fun processar(
        palavraChaveTag: String,
        conteudoTag: String,
        contexto: ContextoParsing
    ): ResultadoProcessamentoTag
}
