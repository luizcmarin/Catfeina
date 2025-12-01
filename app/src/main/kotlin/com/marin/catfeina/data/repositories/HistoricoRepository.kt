/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/repositories/HistoricoRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Repositório que gerencia os dados do histórico de visualização.
*
*/
package com.marin.catfeina.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.marin.catfeina.data.models.Historico
import com.marin.catfeina.data.models.toDomain
import com.marin.catfeina.data.models.toEntity
import com.marin.catfeina.sqldelight.Tbl_historicoQueries
import com.marin.core.ui.UiState
import com.marin.core.util.safeFlowQuery
import com.marin.core.util.safeQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface HistoricoRepository {
    fun getHistoricos(): Flow<UiState<List<Historico>>>
    fun countHistoricos(): Flow<UiState<Long>>
    suspend fun insertHistorico(historico: Historico): UiState<Unit>
}

class HistoricoRepositoryImpl @Inject constructor(
    private val historicoQueries: Tbl_historicoQueries,
    private val ioDispatcher: CoroutineDispatcher
) : HistoricoRepository {

    override fun getHistoricos(): Flow<UiState<List<Historico>>> = safeFlowQuery(ioDispatcher) {
        historicoQueries.getHistoricos()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { it.map { historico -> historico.toDomain() } }
    }

    override fun countHistoricos(): Flow<UiState<Long>> = safeFlowQuery(ioDispatcher) {
        historicoQueries.countHistoricos().asFlow().mapToOne(ioDispatcher)
    }

    override suspend fun insertHistorico(historico: Historico): UiState<Unit> = safeQuery(dispatcher = ioDispatcher) {
        val entity = historico.toEntity()
        historicoQueries.insertHistorico(
            nometabela = entity.nometabela,
            conteudoid = entity.conteudoid,
            titulo = entity.titulo,
            vistoem = entity.vistoem
        )
    }
}
