/*
 *  Projeto: Catfeina
 *  Arquivo: SearchRepository.kt
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


package com.marin.catfeina.features.search.data.repository

import com.marin.catfeina.core.data.AppDispatchers
import com.marin.catfeina.core.utils.TipoConteudoEnum
import com.marin.catfeina.features.search.domain.model.SearchResult
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val queries: CatfeinaDatabaseQueries,
    private val dispatchers: AppDispatchers
) {

    /**
     * Realiza uma busca global no app, pesquisando em Poesias, Personagens e Atelier.
     *
     * @param query O termo de busca.
     * @return Um Flow que emite a lista de resultados unificados.
     */
    fun search(query: String): Flow<List<SearchResult>> = flow {
        if (query.isBlank()) {
            emit(emptyList())
            return@flow
        }

        // Usa coroutineScope para executar as buscas em paralelo
        coroutineScope {
            val poesiasDeferred = async {
                queries.searchPoesias(query, query).executeAsList().map {
                    SearchResult(
                        id = it.id,
                        tipoConteudo = TipoConteudoEnum.POESIA,
                        titulo = it.titulo,
                        subtitulo = it.textoBase,
                        imagemUrl = it.imagem
                    )
                }
            }

            val personagensDeferred = async {
                queries.searchPersonagens(query, query).executeAsList().map {
                    SearchResult(
                        id = it.id,
                        tipoConteudo = TipoConteudoEnum.PERSONAGEM,
                        titulo = it.nome,
                        subtitulo = it.descricao,
                        imagemUrl = it.imagem_url
                    )
                }
            }

            val atelierDeferred = async {
                // Assumindo que a query 'searchAtelier' foi adicionada ao .sq
                queries.searchAtelier(query, query).executeAsList().map {
                    SearchResult(
                        id = it.id,
                        tipoConteudo = TipoConteudoEnum.ATELIER,
                        titulo = it.titulo,
                        subtitulo = it.conteudo,
                        imagemUrl = null // Atelier não tem imagem
                    )
                }
            }

            // Aguarda a conclusão de todas as buscas e combina os resultados
            val resultados = poesiasDeferred.await() + personagensDeferred.await() + atelierDeferred.await()

            // Ordena a lista combinada, por exemplo, por título (opcional)
            emit(resultados.sortedBy { it.titulo })
        }
    }.flowOn(dispatchers.io)
}
