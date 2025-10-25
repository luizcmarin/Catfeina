/*
 *  Projeto: Catfeina
 *  Arquivo: ProcessadorItemLista.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Processador de tag especializado em converter tags de item de lista
 *        (ex: {li|Texto do item com {n|negrito}}).
 *
 *  Propósito:
 *  Esta classe isola a lógica para lidar com tags de itens de lista. Ela tem a
 *  capacidade única de invocar uma instância do `ParserTextoFormatado`
 *  para analisar o conteúdo *dentro* da tag {li|...}. Isso permite que itens
 *  de lista contenham suas próprias formatações em linha (negrito, links, etc.),
 *  criando uma estrutura de dados rica que será corretamente renderizada na UI.
 *
 */

package com.marin.catfeina.core.formatador.parser

import com.marin.catfeina.core.formatador.ElementoConteudo
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

/**
 * Processador de tag que lida com a palavra-chave "li" para criar elementos de item de lista,
 * suportando formatação aninhada.
 */
class ProcessadorItemLista @Inject constructor(
    // Injeção LAZY com Provider para quebrar o ciclo de dependência:
    // O ParserTextoFormatado depende de uma lista de ProcessadorTag (incluindo este),
    // e este ProcessadorTag depende do ParserTextoFormatado.
    private val parserProvider: Provider<ParserTextoFormatado>
) : ProcessadorTag {

    // Acessa o parser de forma lazy na primeira vez que for necessário.
    private val parser: ParserTextoFormatado by lazy { parserProvider.get() }

    override val palavrasChave: Set<String> = setOf("li")

    override fun processar(
        palavraChaveTag: String,
        conteudoTag: String,
        contexto: ContextoParsing
    ): ResultadoProcessamentoTag {
        Timber.d("Processando ITEM DE LISTA (com sub-parsing): Conteúdo='${conteudoTag}'")

        if (conteudoTag.isBlank()) {
            Timber.w("Tag 'li' com conteúdo vazio. Criando item de lista vazio.")
            return ResultadoProcessamentoTag.ElementoBloco(
                ElementoConteudo.ItemLista(textoItem = "", aplicacoesEmLinha = emptyList())
            )
        }

        // Usa o parser injetado para analisar o conteúdo INTERNO do item de lista.
        val elementosInternos = parser.parse(conteudoTag)

        // Um item de lista deve ser, fundamentalmente, um único parágrafo.
        // Se o parser interno gerar múltiplos blocos (ex: {li|texto\n\ntexto2}),
        // pegamos o primeiro parágrafo, que é o comportamento mais esperado.
        val primeiroParagrafo = elementosInternos.filterIsInstance<ElementoConteudo.Paragrafo>().firstOrNull()

        return if (primeiroParagrafo != null) {
            // Se o conteúdo resultou em um parágrafo (com ou sem formatações), usamos ele.
            Timber.v("  > Conteúdo do 'li' resultou em um parágrafo com ${primeiroParagrafo.aplicacoesEmLinha.size} aplicações.")
            ResultadoProcessamentoTag.ElementoBloco(
                ElementoConteudo.ItemLista(
                    textoItem = primeiroParagrafo.textoCru,
                    aplicacoesEmLinha = primeiroParagrafo.aplicacoesEmLinha
                )
            )
        } else {
            // Caso de fallback: se o conteúdo do {li|...} não resultar em um parágrafo
            // (ex: continha apenas uma {imagem|...} ou era só texto sem quebra de linha no final).
            // O parser pode retornar uma lista vazia ou de outros elementos.
            // Tratamos o conteúdo como texto simples sem formatação.
            Timber.w("  > Conteúdo da tag 'li' ('$conteudoTag') não produziu um parágrafo. Tratando como texto simples.")
            ResultadoProcessamentoTag.ElementoBloco(
                ElementoConteudo.ItemLista(textoItem = conteudoTag)
            )
        }
    }
}
