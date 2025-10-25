/*
 *  Projeto: Catfeina
 *  Arquivo: SearchResult.kt
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

package com.marin.catfeina.ui.search

import com.marin.catfeina.core.utils.TipoConteudoEnum

/**
 * Representa um item único na lista de resultados da pesquisa global.
 * Este modelo unifica os dados de diferentes tabelas (Poesias, Personagens, Atelier)
 * para serem exibidos em uma única lista.
 *
 * @param id O ID original do item na sua tabela de origem.
 * @param tipoConteudo O tipo do conteúdo, para sabermos para qual tela de detalhe navegar.
 * @param titulo O texto principal a ser exibido no resultado.
 * @param subtitulo Um texto secundário ou um trecho do conteúdo.
 * @param imagem Uma URL ou caminho opcional para uma imagem a ser exibida.
 */
data class SearchResult(
    val id: Long,
    val tipoConteudo: TipoConteudoEnum,
    val titulo: String,
    val subtitulo: String,
    val imagem: String?
)