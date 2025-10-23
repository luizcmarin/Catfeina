/*
 *  Projeto: Catfeina
 *  Arquivo: ProcessadorLink.kt
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
 *  Arquivo: ProcessadorLink.kt
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
 * //  Arquivo: ProcessadorLink.kt
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
// Arquivo: com.marin.catfeina.core.formatador.parser.ProcessadorLink.kt
//
// Descrição: Processador de tag especializado em converter tags de link/URL
//            (ex: {link|https://exemplo.com|Clique aqui}) em uma anotação em linha.
//
// Propósito:
// Esta classe isola a lógica para lidar com tags de hiperlink. Registrada no
// `ParserModule`, ela é invocada pelo `ParserTextoFormatado` ao encontrar as
// palavras-chave "link" ou "url". Sua responsabilidade é extrair a URL e o
// texto do link, criando uma `AplicacaoAnotacaoLink` que será usada pelo
// `TextoFormatadoRenderer` para criar um trecho de texto clicável na UI.
// ===================================================================================
package com.marin.catfeina.core.formatador.parser

import com.marin.catfeina.core.formatador.AplicacaoAnotacaoLink
import timber.log.Timber

class ProcessadorLink : ProcessadorTag {
    override val palavrasChave: Set<String> = setOf("link", "url")

    override fun processar(
        palavraChaveTag: String,
        conteudoTag: String, // Formato esperado: "url|texto do link"
        contexto: ContextoParsing
    ): ResultadoProcessamentoTag {
        Timber.d("Processando LINK: Chave='${palavraChaveTag}', Conteúdo='${conteudoTag}'")

        val partes = conteudoTag.split("|", limit = 2)

        if (partes.size != 2) {
            Timber.w("Formato de conteúdo de link inválido: '$conteudoTag'. Esperado 'url|texto'. Tratando como NaoConsumido.")
            return ResultadoProcessamentoTag.NaoConsumido
        }

        val url = partes[0].trim()
        val textoLink = partes[1].trim()

        if (url.isBlank() || textoLink.isBlank()) {
            Timber.w("URL ou texto do link está vazio. URL='${url}', Texto='${textoLink}'. Tratando como NaoConsumido.")
            return ResultadoProcessamentoTag.NaoConsumido
        }

        return ResultadoProcessamentoTag.AplicacaoTagEmLinha(
            AplicacaoAnotacaoLink(
                intervalo = IntRange.EMPTY,
                textoOriginal = textoLink,
                url = url
            )
        )
    }
}
