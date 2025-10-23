/*
 *  Projeto: Catfeina
 *  Arquivo: PersonagensRepository.kt
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


package com.marin.catfeina.features.personagens.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.marin.catfeina.core.data.AppDispatchers
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import com.marin.catfeina.sqldelight.Personagens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório para gerenciar os dados dos Personagens.
 */
@Singleton
class PersonagensRepository @Inject constructor(
    private val queries: CatfeinaDatabaseQueries,
    private val dispatchers: AppDispatchers
) {

    /**
     * Retorna um Flow com a lista de todos os personagens, ordenados por nome.
     */
    fun getAllPersonagens(): Flow<List<Personagens>> {
        return queries.getAllPersonagens()
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }
}
