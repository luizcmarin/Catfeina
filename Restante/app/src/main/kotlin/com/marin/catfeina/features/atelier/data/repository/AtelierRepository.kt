/*
 *  Projeto: Catfeina
 *  Arquivo: AtelierRepository.kt
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


package com.marin.catfeina.features.atelier.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.marin.catfeina.core.data.AppDispatchers
import com.marin.catfeina.sqldelight.Atelier
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório para gerenciar as notas do Atelier.
 * Interage com o banco de dados através das queries do SQLDelight.
 */
@Singleton
class AtelierRepository @Inject constructor(
    private val queries: CatfeinaDatabaseQueries,
    private val dispatchers: AppDispatchers
) {

    /**
     * Retorna um Flow com a lista de todas as notas do atelier,
     * ordenadas pela data de atualização mais recente.
     */
    fun getAllAtelier(): Flow<List<Atelier>> {
        return queries.getAllAtelier()
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    /**
     * Busca uma nota específica do atelier pelo seu ID.
     *
     * @param id O ID da nota a ser buscada.
     * @return Um Flow que emite a nota encontrada ou null se não existir.
     */
    fun getAtelierById(id: Long): Flow<Atelier?> {
        // O ID 0 é considerado inválido, representa uma nova nota não salva.
        if (id == 0L) {
            return kotlinx.coroutines.flow.flowOf(null)
        }
        return queries.getAtelierById(id)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    /**
     * Salva (insere ou atualiza) uma nota no atelier.
     *
     * @param id O ID da nota. Se for null, uma nova nota será criada com um ID autoincrementado.
     * @param titulo O título da nota.
     * @param conteudo O conteúdo da nota.
     * @param fixada Se a nota deve ser fixada.
     */
    suspend fun saveAtelier(id: Long?, titulo: String, conteudo: String, fixada: Boolean) {
        withContext(dispatchers.io) {
            queries.upsertAtelier(
                id = id, // Passar null para o SQLDelight/SQLite autoincrementar o ID em uma nova inserção.
                titulo = titulo,
                conteudo = conteudo,
                fixada = fixada,
                dataAtualizacao = System.currentTimeMillis()
            )
        }
    }

    /**
     * Deleta uma nota do atelier.
     *
     * @param id O ID da nota a ser deletada.
     */
    suspend fun deleteAtelier(id: Long) {
        withContext(dispatchers.io) {
            queries.deleteAtelier(id)
        }
    }
}
