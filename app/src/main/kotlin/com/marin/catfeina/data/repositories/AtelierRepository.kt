/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/repositories/AtelierRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Repositório que gerencia os dados do atelier.
*
*/
package com.marin.catfeina.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.marin.catfeina.data.models.Atelier
import com.marin.catfeina.data.models.toDomain
import com.marin.catfeina.data.models.toEntity
import com.marin.catfeina.sqldelight.Tbl_atelierQueries
import com.marin.core.ui.UiState
import com.marin.core.util.safeFlowQuery
import com.marin.core.util.safeQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface AtelierRepository {
    fun getAteliers(): Flow<UiState<List<Atelier>>>
    fun getAtelier(id: Long): Flow<UiState<Atelier?>>
    suspend fun upsertAtelier(atelier: Atelier): UiState<Unit>
    suspend fun deleteAtelier(id: Long): UiState<Unit>
    suspend fun upsertAteliers(ateliers: List<Atelier>): UiState<Unit>
    fun countAteliers(): Flow<UiState<Long>>
}

class AtelierRepositoryImpl @Inject constructor(
    private val atelierQueries: Tbl_atelierQueries,
    private val ioDispatcher: CoroutineDispatcher
) : AtelierRepository {

    override fun getAteliers(): Flow<UiState<List<Atelier>>> = safeFlowQuery(ioDispatcher) {
        atelierQueries.getAteliers()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { it.map { atelier -> atelier.toDomain() } }
    }

    override fun getAtelier(id: Long): Flow<UiState<Atelier?>> = safeFlowQuery(ioDispatcher) {
        atelierQueries.getAtelier(id)
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.toDomain() }
    }

    override suspend fun upsertAtelier(atelier: Atelier): UiState<Unit> = safeQuery(dispatcher = ioDispatcher) {
        val entity = atelier.toEntity()
        atelierQueries.upsertAtelier(
            id = entity.id,
            titulo = entity.titulo,
            texto = entity.texto,
            atualizadoem = entity.atualizadoem,
            fixada = entity.fixada
        )
    }

    override suspend fun deleteAtelier(id: Long): UiState<Unit> = safeQuery(dispatcher = ioDispatcher) {
        atelierQueries.deleteAtelier(id)
    }

    override suspend fun upsertAteliers(ateliers: List<Atelier>): UiState<Unit> = safeQuery(dispatcher = ioDispatcher) {
        atelierQueries.transaction {
            ateliers.forEach { atelier ->
                val entity = atelier.toEntity()
                atelierQueries.upsertAtelier(
                    id = entity.id,
                    titulo = entity.titulo,
                    texto = entity.texto,
                    atualizadoem = entity.atualizadoem,
                    fixada = entity.fixada
                )
            }
        }
    }

    override fun countAteliers(): Flow<UiState<Long>> = safeFlowQuery(ioDispatcher) {
        atelierQueries.countAteliers().asFlow().mapToOne(ioDispatcher)
    }
}
