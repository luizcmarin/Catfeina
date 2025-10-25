/*
 *  Projeto: Catfeina
 *  Arquivo: SearchRepository.kt
 *
 *  Direitos autorais (c)2025 Marin. Todos os direitos reservados.
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

package com.marin.catfeina.core.data.repository

import com.marin.catfeina.core.data.AppDispatchers
import com.marin.catfeina.core.utils.TipoConteudoEnum
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Representa um único item unificado nos resultados da busca.
 * Renomeado para evitar conflito com a classe do Android.
 */
data class AppSearchResult(
    val id: Long,
    val tipoConteudo: TipoConteudoEnum,
    val titulo: String,
    val subtitulo: String,
    val imagem: String?
)

@Singleton
class SearchRepository @Inject constructor(
    private val queries: CatfeinaDatabaseQueries,
    private val dispatchers: AppDispatchers
) {

    /**
     * Realiza uma busca APENAS em poesias.
     *
     * @param query O termo de busca.
     * @return Um Flow que emite a lista de resultados da busca.
     */
    fun search(query: String): Flow<List<AppSearchResult>> = flow {
        if (query.isBlank()) {
            emit(emptyList())
            return@flow
        }

        // Executa a busca apenas em poesias
        val resultados = queries.searchPoesias(query, query).executeAsList().map {
            AppSearchResult(
                id = it.id,
                tipoConteudo = TipoConteudoEnum.POESIA,
                titulo = it.titulo,
                subtitulo = it.textoBase,
                imagem = it.imagem
            )
        }

        emit(resultados)

    }.flowOn(dispatchers.io) // Executa toda a lógica do flow na thread de IO
}