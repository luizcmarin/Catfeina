/*
 *  Projeto: Catfeina
 *  Arquivo: ProcessadorCabecalho.kt
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
 *  Arquivo: ProcessadorCabecalho.kt
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
 * //  Arquivo: ProcessadorCabecalho.kt
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
// Arquivo: com.marin.catfeina.core.formatador.parser.ProcessadorCabecalho.kt
//
// Descrição: Processador de tag especializado em converter tags de cabeçalho
//            (ex: {t1|Meu Título}) em um elemento de bloco `ElementoConteudo.Cabecalho`.
//
// Propósito:
// Esta classe isola a lógica para lidar especificamente com tags de título.
// Ela é registrada no `ParserModule` e chamada pelo `ParserTextoFormatado`
// sempre que uma de suas palavras-chave (`t1` a `t6`) é encontrada. Sua
// responsabilidade é extrair o nível do cabeçalho e o texto, criando
// um `ElementoConteudo.Cabecalho` que será posteriormente renderizado pela UI.
// ===================================================================================
package com.marin.catfeina.core.formatador.parser

import com.marin.catfeina.core.formatador.ElementoConteudo
import timber.log.Timber

class ProcessadorCabecalho : ProcessadorTag {
    // Suporta tags como t1, t2, t3, etc.
    override val palavrasChave: Set<String> = setOf("t1", "t2", "t3", "t4", "t5", "t6")

    override fun processar(
        palavraChaveTag: String,
        conteudoTag: String,
        contexto: ContextoParsing
    ): ResultadoProcessamentoTag {
        Timber.d("Processando CABEÇALHO: Chave='${palavraChaveTag}', Conteúdo='${conteudoTag}'")

        // Extrai o nível do cabeçalho da palavra-chave (ex: "t1" -> nível 1)
        val nivel = palavraChaveTag.removePrefix("t").toIntOrNull()
        if (nivel == null || nivel !in 1..6) {
            Timber.w("Nível de cabeçalho inválido para tag '$palavraChaveTag'. Tratando como NaoConsumido.")
            return ResultadoProcessamentoTag.NaoConsumido // Ou Erro, se preferir
        }

        return ResultadoProcessamentoTag.ElementoBloco(
            ElementoConteudo.Cabecalho(
                texto = conteudoTag,
                nivel = nivel
            )
        )
    }
}
