/*
 *  Projeto: Catfeina
 *  Arquivo: ParserTextoFormatado.kt
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
 *  Arquivo: ParserTextoFormatado.kt
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
 * //  Arquivo: ParserTextoFormatado.kt
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

/*
 * Arquivo: com.marin.catfeina.ui.componentes.formatacao.parser.ParserTextoFormatado.kt
 * @project Catfeina
 * @description
 * Parser principal responsável por converter uma string de texto cru com tags
 * customizadas (formato {chave|conteúdo} ou {chave}) em uma lista de [ElementoConteudo].
 */
// ===================================================================================
// Arquivo: com.marin.catfeina.core.formatador.parser.ParserTextoFormatado.kt
//
// Descrição: Classe principal responsável por converter uma string de texto cru,
//            contendo tags customizadas, em uma estrutura de dados de UI.
//
// Propósito:
// Este parser é o motor central do sistema de texto formatado. Ele recebe uma lista
// de `ProcessadorTag` (injetada via Hilt) e uma string de entrada. Sua função é
// iterar sobre o texto, identificar as tags (ex: {b|texto}, {img|...}), delegar o
// processamento para o `ProcessadorTag` apropriado e construir uma lista ordenada
// de `ElementoConteudo`. Esta lista é então usada pela camada de UI para
// renderizar o texto de forma rica e interativa.
// ===================================================================================
package com.marin.catfeina.core.formatador.parser

import com.marin.catfeina.core.formatador.AplicacaoEmLinha
import com.marin.catfeina.core.formatador.ElementoConteudo
// Se AplicacaoSpanStyle, AplicacaoAnotacaoLink, etc. estiverem em um subpacote ou arquivo diferente,
// importe-os explicitamente aqui. Por exemplo:
// import com.marin.catfeina.ui.componentes.formatacao.model.AplicacaoSpanStyle

import timber.log.Timber
import javax.inject.Inject

class ParserTextoFormatado @Inject constructor(
    private val processadoresTag: List<ProcessadorTag>
) {

    private val mapaProcessadores: Map<String, ProcessadorTag> by lazy {
        Timber.d("[ParserTF] CONSTRUINDO MAPAPROCESSADORES...")
        Timber.d("[ParserTF] Total de ProcessadorTag recebidos: ${processadoresTag.size}")
        processadoresTag.forEachIndexed { index, proc ->
            Timber.d("[ParserTF]  > Processador[${index}]: ${proc::class.java.simpleName}, Palavras-chave originais: ${proc.palavrasChave.joinToString()}")
        }

        // Armazenar chaves em minúsculas para busca case-insensitive
        val mapaConstruido = processadoresTag.flatMap { processador ->
            processador.palavrasChave.map { palavraChaveOriginal ->
                val chaveProcessada = palavraChaveOriginal.lowercase()
                Timber.v("[ParserTF]    Mapeando: '${chaveProcessada}' (de '${palavraChaveOriginal}') -> ${processador::class.java.simpleName}")
                chaveProcessada to processador
            }
        }.toMap()

        // Verificar duplicatas usando as chaves processadas (lowercase)
        val todasChavesProcessadas = processadoresTag.flatMap { it.palavrasChave.map { pk -> pk.lowercase() } }
        if (todasChavesProcessadas.size != todasChavesProcessadas.toSet().size) {
            Timber.w("[ParserTF] ATENÇÃO: Palavras-chave DUPLICADAS (case-insensitive) detectadas! A última definição no módulo Hilt prevalecerá.")
            val contagemChaves = todasChavesProcessadas.groupingBy { it }.eachCount()
            contagemChaves.filter { it.value > 1 }.forEach { (chave, contagem) ->
                Timber.w("[ParserTF]    Chave duplicada (lowercase): '$chave' (aparece $contagem vezes).")
            }
        }
        Timber.i("[ParserTF] mapaProcessadores CONSTRUÍDO. Total de mapeamentos: ${mapaConstruido.size}")
        if (mapaConstruido.isEmpty() && processadoresTag.isNotEmpty()) {
            Timber.e("[ParserTF] ERRO GRAVE: mapaProcessadores está VAZIO, mas processadoresTag foram fornecidos! Verifique as palavras-chave e o ParserModule.")
        }
        mapaConstruido
    }

    /**
     * Função utilitária para limpar o texto de todas as tags de formatação,
     * retornando uma única string legível para Text-to-Speech (TTS).
     *
     * Esta função opera em dois passos:
     * 1. Remove todas as tags do tipo {chave|...} e {chave}, mantendo o conteúdo interno
     *    das tags que o possuem (ex: {t1|Título} -> "Título").
     * 2. Troca quebras de linha múltiplas por um único espaço para melhorar a fluidez da fala.
     *
     * @param textoCruInput O texto original com as tags de formatação.
     * @return Uma string de texto puro, pronta para ser lida por um motor TTS.
     */
    fun extrairTextoPuroParaTTS(textoCruInput: String): String {
        Timber.d("[ParserTF-TTS] Iniciando extração de texto puro para TTS.")
        if (textoCruInput.isBlank()) {
            return ""
        }

        // Regex para encontrar todas as tags, sejam com conteúdo ou sem.
        // Grupo 1: A chave (ex: "t1", "img")
        // Grupo 2 (opcional): O conteúdo dentro da tag (ex: "Meu Título")
        val regexTag = """\{([a-zA-Z0-9_]+)(?:\|([^}]+))?\}""".toRegex()

        // Substitui cada tag encontrada.
        // Se a tag tem conteúdo (match.groupValues[2] não está vazio), usa esse conteúdo.
        // Se não tem (ex: {hr}), substitui por uma string vazia.
        val textoSemTags = regexTag.replace(textoCruInput) { matchResult ->
            matchResult.groupValues.getOrNull(2)?.trim() ?: ""
        }

        // Normaliza espaços e quebras de linha para uma leitura mais natural.
        // - Substitui múltiplas quebras de linha por um ponto e espaço.
        // - Substitui quebras de linha únicas por um espaço.
        // - Comprime múltiplos espaços em um só.
        val textoLimpo = textoSemTags
            .replace(Regex("[\r\n]{2,}"), ". ") // Múltiplas quebras de linha -> Ponto e espaço.
            .replace(Regex("[\r\n]"), " ")      // Quebra de linha única -> espaço.
            .replace(Regex(" +"), " ")       // Múltiplos espaços -> um espaço.
            .trim()

        Timber.d("[ParserTF-TTS] Texto limpo final: \"$textoLimpo\"")
        return textoLimpo
    }

    fun parse(textoCruInput: String): List<ElementoConteudo> {
        Timber.i("----------------- [ParserTF] INÍCIO PARSE (Sintaxe: {chave|conteúdo}) -----------------")
        val textoLimpo = textoCruInput.replace("\r\n", "\n") // Normalizar quebras de linha
        Timber.d("[ParserTF] Texto Cru para Parse (len ${textoLimpo.length}): \"${textoLimpo.replace("\n", "\\n").take(500)}${if (textoLimpo.length > 500) "..." else ""}\"")

        if (mapaProcessadores.isEmpty() && processadoresTag.isNotEmpty()) {
            Timber.e("[ParserTF] ERRO GRAVE ao parsear: mapaProcessadores está VAZIO e processadores foram fornecidos. Tags não serão processadas.")
        } else if (mapaProcessadores.isEmpty()) {
            Timber.w("[ParserTF] AVISO ao parsear: mapaProcessadores está VAZIO (nenhum processador fornecido). Nenhuma tag customizada será processada.")
        }

        val elementosFinais = mutableListOf<ElementoConteudo>()
        val acumuladorTextoParagrafo = StringBuilder()
        val aplicacoesParaParagrafoAtual = mutableListOf<AplicacaoEmLinha>()

        fun finalizarParagrafoPendenteEAdicionar() {
            if (acumuladorTextoParagrafo.isNotEmpty() || aplicacoesParaParagrafoAtual.isNotEmpty()) {
                val textoDoParagrafo = acumuladorTextoParagrafo.toString()
                Timber.d("[ParserTF] Finalizando Parágrafo: Texto='${textoDoParagrafo.replace("\n", "\\n")}', Aplicações=${aplicacoesParaParagrafoAtual.size}")
                aplicacoesParaParagrafoAtual.forEach { aplic ->
                    Timber.v("[ParserTF]   -> Apl: ${aplic::class.simpleName}(orig='${aplic.textoOriginal.replace("\n", "\\n")}') @${aplic.intervalo}")
                }
                // Assumindo que ElementoConteudo.Paragrafo é uma data class ou tem construtor adequado
                elementosFinais.add(
                    ElementoConteudo.Paragrafo(
                        textoCru = textoDoParagrafo,
                        aplicacoesEmLinha = aplicacoesParaParagrafoAtual.toList()
                    )
                )
                acumuladorTextoParagrafo.clear()
                aplicacoesParaParagrafoAtual.clear()
            }
        }

        var cursor = 0
        while (cursor < textoLimpo.length) {
            // Timber.v("[ParserTF] Loop - Cursor: $cursor")
            val proximaTagMatch = encontrarProximaTag(textoLimpo, cursor)

            if (proximaTagMatch != null) {
                val rangeTagNoTextoOriginal = proximaTagMatch.rangeNoTextoOriginal
                val palavraChaveOriginalDetectada = proximaTagMatch.palavraChave
                val conteudoInternoTag = proximaTagMatch.conteudoInterno

                // Adiciona texto ANTES da tag ao acumulador do parágrafo atual
                if (rangeTagNoTextoOriginal.first > cursor) {
                    val textoAntesDaTag = textoLimpo.substring(cursor, rangeTagNoTextoOriginal.first)
                    acumuladorTextoParagrafo.append(textoAntesDaTag)
                    Timber.v("[ParserTF] Texto ANTES da tag '{${palavraChaveOriginalDetectada}}' adicionado: \"${textoAntesDaTag.replace("\n", "\\n")}\"")
                }

                // Busca o processador usando a chave em minúsculas
                val processador = mapaProcessadores[palavraChaveOriginalDetectada.lowercase()]
                // Timber.v("[ParserTF] Processador para chave '${palavraChaveOriginalDetectada.lowercase()}': ${processador?.let { it::class.java.simpleName } ?: "NÃO ENCONTRADO"}")

                if (processador != null) {
                    val contexto = ContextoParsing(
                        textoCompleto = textoLimpo,
                        indiceAtual = rangeTagNoTextoOriginal.first
                    )
                    // Passa a palavra-chave original para o processador, ele pode precisar dela
                    val resultado = processador.processar(palavraChaveOriginalDetectada, conteudoInternoTag, contexto)
                    Timber.d("[ParserTF] Resultado de '${processador::class.java.simpleName}' para '{${palavraChaveOriginalDetectada}|${conteudoInternoTag.replace("\n", "\\n")}}': Tipo=${resultado::class.simpleName}")

                    when (resultado) {
                        is ResultadoProcessamentoTag.ElementoBloco -> {
                            finalizarParagrafoPendenteEAdicionar() // Finaliza parágrafo anterior
                            elementosFinais.add(resultado.elemento)
                            Timber.d("[ParserTF]  > ElementoBloco '${resultado.elemento::class.simpleName}' adicionado.")
                        }
                        is ResultadoProcessamentoTag.AplicacaoTagEmLinha -> {
                            val aplicacaoOriginal = resultado.aplicacao
                            val textoParaInserirNoAcumulador = aplicacaoOriginal.textoOriginal

                            val inicioNoAcumulador = acumuladorTextoParagrafo.length
                            acumuladorTextoParagrafo.append(textoParaInserirNoAcumulador)
                            val fimNoAcumulador = acumuladorTextoParagrafo.length // Exclusivo

                            val intervaloAjustado = if (textoParaInserirNoAcumulador.isNotEmpty()) {
                                inicioNoAcumulador until fimNoAcumulador
                            } else {
                                Timber.w("[ParserTF]  > AplicacaoTagEmLinha para '{${palavraChaveOriginalDetectada}}' tem textoOriginal VAZIO. Intervalo: $inicioNoAcumulador..$inicioNoAcumulador")
                                inicioNoAcumulador until inicioNoAcumulador
                            }

                            // Timber.v("[ParserTF]  > AplicacaoTagEmLinha para '{${palavraChaveOriginalDetectada}}': Texto='${textoParaInserirNoAcumulador.replace("\n", "\\n")}'. Intervalo no acumulador: $intervaloAjustado")

                            // ATUALIZA o intervalo na instância da aplicação (requer 'var intervalo' em AplicacaoEmLinha)
                            aplicacaoOriginal.intervalo = intervaloAjustado

                            if (intervaloAjustado.isEmpty() && textoParaInserirNoAcumulador.isNotEmpty()) {
                                Timber.e("[ParserTF] ERRO CRÍTICO: Intervalo para '${aplicacaoOriginal::class.simpleName}' é VAZIO, mas textoOriginal ('$textoParaInserirNoAcumulador') não é.")
                            }
                            aplicacoesParaParagrafoAtual.add(aplicacaoOriginal)
                        }
                        is ResultadoProcessamentoTag.NaoConsumido -> {
                            val tagCompletaOriginal = textoLimpo.substring(rangeTagNoTextoOriginal.first, rangeTagNoTextoOriginal.last + 1)
                            acumuladorTextoParagrafo.append(tagCompletaOriginal)
                            Timber.w("[ParserTF] Tag '{${palavraChaveOriginalDetectada}}' NÃO CONSUMIDA por ${processador::class.java.simpleName}. Adicionando literal: \"${tagCompletaOriginal.replace("\n", "\\n")}\"")
                        }
                        is ResultadoProcessamentoTag.Erro -> {
                            Timber.e("[ParserTF] ERRO no parsing da tag '{${palavraChaveOriginalDetectada}}' por ${processador::class.java.simpleName}: ${resultado.mensagem}")
                            val tagCompletaOriginalComErro = textoLimpo.substring(rangeTagNoTextoOriginal.first, rangeTagNoTextoOriginal.last + 1)
                            acumuladorTextoParagrafo.append(tagCompletaOriginalComErro)
                        }
                    }
                } else { // processador == null
                    val tagCompletaSemProcessador = textoLimpo.substring(rangeTagNoTextoOriginal.first, rangeTagNoTextoOriginal.last + 1)
                    acumuladorTextoParagrafo.append(tagCompletaSemProcessador)
                    Timber.e("[ParserTF] PROCESSADOR NÃO ENCONTRADO para chave (lowercase) '${palavraChaveOriginalDetectada.lowercase()}'. Adicionando tag literal: \"${tagCompletaSemProcessador.replace("\n", "\\n")}\"")
                }
                cursor = rangeTagNoTextoOriginal.last + 1 // Avança cursor para depois da tag inteira {chave...}
            } else { // proximaTagMatch == null (nenhuma tag mais encontrada)
                // Timber.v("[ParserTF] Nenhuma nova tag encontrada. Adicionando restante do texto.")
                if (cursor < textoLimpo.length) {
                    val textoRestante = textoLimpo.substring(cursor)
                    acumuladorTextoParagrafo.append(textoRestante)
                    // Timber.d("[ParserTF] Texto restante adicionado ao acumulador: \"${textoRestante.replace("\n", "\\n")}\"")
                }
                break // Sai do loop while
            }
            // Timber.v("[ParserTF] Fim da iteração do loop. Cursor atualizado para: $cursor")
        }

        finalizarParagrafoPendenteEAdicionar() // Garante que o último parágrafo seja processado
        Timber.i("[ParserTF] Parseamento concluído. Total de ElementoConteudo: ${elementosFinais.size}")
        Timber.i("----------------- [ParserTF] FIM PARSE -----------------")
        return elementosFinais.toList()
    }

    // Estrutura para informações da tag encontrada
    private data class TagInfo(
        val rangeNoTextoOriginal: IntRange,
        val palavraChave: String,
        val conteudoInterno: String
    )

    // Nova encontrarProximaTag para a sintaxe {palavraChave|conteúdo} ou {palavraChave}
    private fun encontrarProximaTag(texto: String, inicioBusca: Int): TagInfo? {
        val logPrefix = "[ParserTF][encontrarProximaTag]"

        val inicioTag = texto.indexOf('{', inicioBusca)
        if (inicioTag == -1) {
            return null // Nenhuma tag de início encontrada
        }

        val fimTag = texto.indexOf('}', inicioTag + 1)
        if (fimTag == -1) {
            Timber.w("$logPrefix: '{' encontrado em $inicioTag, mas '}' de fechamento não encontrado. Substring: '${texto.substring(inicioTag).take(30)}'")
            return null // Tag não fechada
        }

        val conteudoEntreChaves = texto.substring(inicioTag + 1, fimTag)
        if (conteudoEntreChaves.isBlank()) {
            Timber.w("$logPrefix: Conteúdo entre chaves { } está vazio. Tag: '${texto.substring(inicioTag, fimTag + 1)}'")
            return null
        }

        var palavraChaveExtraida: String
        var conteudoInternoDaTag: String

        val separadorPipeIndex = conteudoEntreChaves.indexOf('|')

        if (separadorPipeIndex != -1) {
            palavraChaveExtraida = conteudoEntreChaves.substring(0, separadorPipeIndex)
            conteudoInternoDaTag = conteudoEntreChaves.substring(separadorPipeIndex + 1)
        } else {
            palavraChaveExtraida = conteudoEntreChaves
            conteudoInternoDaTag = ""
        }

        if (palavraChaveExtraida.isBlank()) {
            Timber.w("$logPrefix: Palavra-chave extraída de '$conteudoEntreChaves' está vazia.")
            return null
        }

        // Importante: Adiciona uma validação para caracteres proibidos na palavra-chave
        if (palavraChaveExtraida.contains(Regex("\\s|\\{|\\}|\\|"))) {
            Timber.w("$logPrefix: Palavra-chave inválida '$palavraChaveExtraida' (contém caracteres proibidos). Tag: '${texto.substring(inicioTag, fimTag + 1)}'")
            return null
        }

        val rangeCompletoDaTag = inicioTag..fimTag
        Timber.v("$logPrefix: Tag VÁLIDA: Chave='${palavraChaveExtraida}', Conteúdo='${conteudoInternoDaTag.replace("\n", "\\n")}', Range=${rangeCompletoDaTag}")
        return TagInfo(rangeCompletoDaTag, palavraChaveExtraida, conteudoInternoDaTag)
    }
}
