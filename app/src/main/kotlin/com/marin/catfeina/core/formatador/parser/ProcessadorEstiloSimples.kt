/*
 *  Projeto: Catfeina
 *  Arquivo: ProcessadorEstiloSimples.kt
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
 *  Arquivo: ProcessadorEstiloSimples.kt
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
 * //  Arquivo: ProcessadorEstiloSimples.kt
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
// Arquivo: com.marin.catfeina.core.formatador.parser.ProcessadorEstiloSimples.kt
//
// Descrição: Processador de tag que lida com múltiplas tags de formatação de
//            estilo em linha (negrito, itálico, sublinhado, destaque).
//
// Propósito:
// Esta classe agrega a lógica para várias tags simples que resultam em uma
// `AplicacaoSpanStyle`. Em vez de criar um arquivo para cada tag ({n|...},
// {i|...}, etc.), este processador centraliza a conversão, tornando o sistema
// mais conciso. Ele é registrado no `ParserModule` e converte o conteúdo da
// tag em uma formatação que será aplicada a um trecho do parágrafo.
// ===================================================================================
package com.marin.catfeina.core.formatador.parser

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.marin.catfeina.core.formatador.AplicacaoSpanStyle // VERIFIQUE ESTE CAMINHO
import timber.log.Timber

class ProcessadorEstiloSimples : ProcessadorTag {
    override val palavrasChave: Set<String> = setOf("n", "d", "i", "s", "ni")

    override fun processar(
        palavraChaveTag: String,
        conteudoTag: String,
        contexto: ContextoParsing
    ): ResultadoProcessamentoTag {
        Timber.d("[ProcessadorEstiloSimples] Recebido: Chave='${palavraChaveTag}', ConteúdoInterno='${conteudoTag.replace("\n", "\\n")}'")

        val aplicacaoStyle = when (palavraChaveTag.lowercase()) {
            "n" -> {
                AplicacaoSpanStyle(
                    textoOriginal = conteudoTag,
                    fontWeight = FontWeight.Bold // USA 'fontWeight'
                )
            }
            "d" -> {
                AplicacaoSpanStyle(
                    textoOriginal = conteudoTag,
                    isDestaque = true // USA 'isDestaque'
                )
            }
            "i" -> {
                AplicacaoSpanStyle(
                    textoOriginal = conteudoTag,
                    fontStyle = FontStyle.Italic // USA 'fontStyle'
                )
            }
            "s" -> {
                AplicacaoSpanStyle(
                    textoOriginal = conteudoTag,
                    textDecoration = TextDecoration.Underline // USA 'textDecoration'
                )
            }
            "ni" -> {
                AplicacaoSpanStyle(
                    textoOriginal = conteudoTag,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            }
            else -> {
                Timber.w("[ProcessadorEstiloSimples] Palavra-chave não reconhecida: '$palavraChaveTag'.")
                return ResultadoProcessamentoTag.NaoConsumido
            }
        }
        // Não passamos 'intervalo' aqui porque AplicacaoSpanStyle tem valor padrão.
        // Não usamos 'provedorSpanStyle' nesta abordagem.
        return ResultadoProcessamentoTag.AplicacaoTagEmLinha(aplicacaoStyle)
    }
}
