// ===================================================================================
// Arquivo: com.marin.catfeina.ui.composables.formatador.parser.ProcessadorLinhaHorizontal.kt
//
// Descrição: Processador de tag especializado em converter a tag de linha
//            horizontal (ex: {linha}) em um `ElementoConteudo.LinhaHorizontal`.
//
// Propósito:
// Esta classe simples isola a lógica para criar um divisor visual no texto.
// Registrada no `ParserModule` e chamada pelo `ParserTextoFormatado`, ela não
// possui conteúdo para processar, apenas identifica a tag e retorna o elemento
// de bloco correspondente que será renderizado como um `HorizontalDivider` na UI.
// ===================================================================================
package com.marin.catfeina.core.formatador.parser

import com.marin.catfeina.core.formatador.ElementoConteudo

class ProcessadorLinhaHorizontal : ProcessadorTag {
    override val palavrasChave: Set<String> = setOf("linha")

    override fun processar(
        palavraChaveTag: String,
        conteudoTag: String, // Conteúdo é ignorado para ::linha::
        contexto: ContextoParsing
    ): ResultadoProcessamentoTag {
        // Validação adicional: verificar se conteudoTag está vazio, como esperado.
        if (conteudoTag.isNotEmpty()) {
            // Logar um aviso, mas ainda assim processar como linha.
            println("Aviso: Tag 'linha' continha texto inesperado: '$conteudoTag'. Ignorando conteúdo.")
        }
        return ResultadoProcessamentoTag.ElementoBloco(
            ElementoConteudo.LinhaHorizontal
        )
    }
}
