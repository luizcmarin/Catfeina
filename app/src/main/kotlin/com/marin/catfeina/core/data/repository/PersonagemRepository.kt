/*
 *  Projeto: Catfeina
 *  Arquivo: PersonagemRepository.kt
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

package com.marin.catfeina.core.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.marin.catfeina.core.data.AppDispatchers
import com.marin.catfeina.core.sync.PersonagemSync
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório para gerenciar os dados de personagens.
 */
@Singleton
class PersonagemRepository @Inject constructor(
    private val queries: CatfeinaDatabaseQueries,
    private val dispatchers: AppDispatchers
) {

    /**
     * Insere ou atualiza uma lista de personagens vindos da sincronização.
     *
     * @param personagens A lista de objetos PersonagemSync para salvar.
     */
    suspend fun upsertPersonagens(personagens: List<PersonagemSync>) = withContext(dispatchers.io) {
        queries.transaction {
            personagens.forEach { personagem ->
                queries.upsertPersonagem(
                    id = personagem.id,
                    nome = personagem.nome,
                    descricao = personagem.descricao,
                    imagem = personagem.imagem,
                    dataCriacao = personagem.dataCriacao
                )
            }
        }
    }

    /*** Retorna um Flow com a lista de todos os personagens,
     * ordenados por nome.
     */
    fun getAllPersonagens(): kotlinx.coroutines.flow.Flow<List<com.marin.catfeina.sqldelight.Personagens>> {
        return queries.getAllPersonagens()
            .asFlow()
            .mapToList(dispatchers.io)
    }

}