/*
 *  Projeto: Catfeina
 *  Arquivo: ProcessadorCitacao.kt
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
 *  Arquivo: ProcessadorCitacao.kt
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
 * //  Arquivo: ProcessadorCitacao.kt
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
// Arquivo: com.marin.catfeina.core.formatador.parser.ProcessadorCitacao.kt
//
// Descrição: Processador de tag especializado em converter tags de citação
//            (ex: {cit|Texto da citação}) em um `ElementoConteudo.Citacao`.
//
// Propósito:
// Esta classe isola a lógica para lidar com tags de citação. Registrada no
// `ParserModule`, ela é invocada pelo `ParserTextoFormatado` ao encontrar a
// palavra-chave "cit". Sua responsabilidade é criar um elemento de bloco
// `ElementoConteudo.Citacao`, que será posteriormente estilizado e
// renderizado pela camada de UI.
// ===================================================================================
package com.marin.catfeina.core.formatador.parser

import com.marin.catfeina.core.formatador.ElementoConteudo

class ProcessadorCitacao : ProcessadorTag {
    override val palavrasChave: Set<String> = setOf("cit")

    override fun processar(
        palavraChaveTag: String,
        conteudoTag: String,
        contexto: ContextoParsing
    ): ResultadoProcessamentoTag {
        if (conteudoTag.isBlank()) {
            // Pode ser permitido uma citação vazia ou retornar um erro/aviso
            // Por ora, vamos permitir, resultando em um bloco de citação vazio.
        }
        return ResultadoProcessamentoTag.ElementoBloco(
            ElementoConteudo.Citacao(texto = conteudoTag)
        )
    }
}