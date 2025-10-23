/*
 *  Projeto: Catfeina
 *  Arquivo: ProcessadorTooltip.kt
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
 *  Arquivo: ProcessadorTooltip.kt
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
 * //  Arquivo: ProcessadorTooltip.kt
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
// Arquivo: com.marin.catfeina.core.formatador.parser.ProcessadorTooltip.kt
//
// Descrição: Processador de tag especializado em converter tags de tooltip (dica)
//            (ex: {dica|texto base|texto que aparece na dica}) em uma anotação.
//
// Propósito:
// Esta classe isola a lógica para lidar com tags de tooltip. Registrada no
// `ParserModule`, ela é invocada pelo `ParserTextoFormatado` ao encontrar a
// palavra-chave "dica". Sua responsabilidade é extrair o texto que ficará no
// parágrafo e o texto que será exibido no tooltip, criando uma
// `AplicacaoAnotacaoTooltip` que o `TextoFormatadoRenderer` usará para
// implementar a interatividade na UI.
// ===================================================================================
package com.marin.catfeina.core.formatador.parser

import com.marin.catfeina.core.formatador.AplicacaoAnotacaoTooltip
import timber.log.Timber

class ProcessadorTooltip : ProcessadorTag {
    override val palavrasChave: Set<String> = setOf("tooltip")

    override fun processar(
        palavraChaveTag: String,
        conteudoTag: String, // Formato esperado: "texto do tooltip|texto que recebe o tooltip"
        contexto: ContextoParsing
    ): ResultadoProcessamentoTag {
        Timber.d("Processando TOOLTIP: Chave='${palavraChaveTag}', Conteúdo='${conteudoTag}'")

        // O resultado será uma lista. Se o delimitador não for encontrado, a lista terá um elemento.
        // Se for encontrado uma vez e o limite for 2, a lista terá dois elementos.
        val partes = conteudoTag.split("|", limit = 2)

        if (partes.size != 2) {
            Timber.w("Formato de conteúdo de tooltip inválido: '$conteudoTag'. Esperado 'tooltip|texto anotado'. Tratando como NaoConsumido.")
            return ResultadoProcessamentoTag.NaoConsumido
        }

        val textoTooltip = partes[0].trim()
        val textoAnotado = partes[1].trim() // Este é o texto que aparecerá no parágrafo

        if (textoTooltip.isBlank() || textoAnotado.isBlank()) {
            Timber.w("Texto do tooltip ou texto anotado está vazio. Tooltip='${textoTooltip}', Anotado='${textoAnotado}'. Tratando como NaoConsumido.")
            return ResultadoProcessamentoTag.NaoConsumido
        }

        return ResultadoProcessamentoTag.AplicacaoTagEmLinha(
            AplicacaoAnotacaoTooltip(
                intervalo = IntRange.EMPTY, // Será preenchido pelo parser principal
                textoOriginal = textoAnotado, // O texto que será inserido e anotado
                textoTooltip = textoTooltip,
                tagAnotacao = "tooltip_${textoTooltip.hashCode()}" // Tag única para a anotação
            )
        )
    }
}
