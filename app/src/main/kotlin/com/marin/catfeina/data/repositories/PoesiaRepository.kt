/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/repositories/PoesiaRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Repositório que gerencia os dados das poesias, atuando como única fonte da verdade.
*
*/
package com.marin.catfeina.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.models.toDomain
import com.marin.catfeina.data.models.toPoesiaEntity
import com.marin.catfeina.data.models.toPoesiaNotaEntity
import com.marin.catfeina.sqldelight.GetPoesias
import com.marin.catfeina.sqldelight.Tbl_poesiaQueries
import com.marin.catfeina.sqldelight.Tbl_poesianotaQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PoesiaRepository {
    fun getPoesiaById(id: Long): Flow<Poesia?>
    fun countPoesias(): Flow<Long>
    suspend fun upsertPoesias(poesias: List<Poesia>)
    fun getPoesiaAleatoria(): Flow<Poesia?>
    fun getPoesiasFavoritas(): Flow<List<Poesia>>
    fun getPoesiasPaginadas(): Flow<PagingData<Poesia>>
    suspend fun updatePoesiaNota(poesia: Poesia)
    fun buscarPoesias(termo: String): Flow<List<Poesia>> // Adicionado
}

class PoesiaRepositoryImpl @Inject constructor(
    private val poesiaQueries: Tbl_poesiaQueries,
    private val poesiaNotaQueries: Tbl_poesianotaQueries,
    private val ioDispatcher: CoroutineDispatcher
) : PoesiaRepository {

    override fun getPoesiaById(id: Long): Flow<Poesia?> {
        return poesiaQueries.getPoesia(id)
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.toDomain() }
    }

    override fun countPoesias(): Flow<Long> {
        return poesiaQueries.countPoesias()
            .asFlow()
            .mapToOne(ioDispatcher)
    }

    override suspend fun upsertPoesias(poesias: List<Poesia>) {
        withContext(ioDispatcher) {
            poesiaQueries.transaction {
                poesias.forEach { poesia ->
                    val entity = poesia.toPoesiaEntity()
                    poesiaQueries.upsertPoesia(
                        id = entity.id,
                        titulo = entity.titulo,
                        textobase = entity.textobase,
                        texto = entity.texto,
                        textofinal = entity.textofinal,
                        imagem = entity.imagem,
                        autor = entity.autor,
                        nota = entity.nota,
                        anterior = entity.anterior,
                        proximo = entity.proximo,
                        atualizadoem = entity.atualizadoem
                    )
                }
            }
        }
    }

    override fun getPoesiaAleatoria(): Flow<Poesia?> {
        return poesiaQueries.getPoesiaAleatoria()
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.toDomain() }
    }

    override fun getPoesiasFavoritas(): Flow<List<Poesia>> {
        return poesiaQueries.getPoesiasFavoritas()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { it.map { getPoesiasFavoritas -> getPoesiasFavoritas.toDomain() } }
    }

    override fun getPoesiasPaginadas(): Flow<PagingData<Poesia>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { PoesiaPagingSource(poesiaQueries) }
        ).flow.map { pagingData ->
            pagingData.map { getPoesias -> getPoesias.toDomain() }
        }
    }

    override suspend fun updatePoesiaNota(poesia: Poesia) {
        withContext(ioDispatcher) {
            val notaEntity = poesia.toPoesiaNotaEntity()
            poesiaNotaQueries.upsertNota(
                poesiaid = notaEntity.poesiaid,
                favorita = notaEntity.favorita,
                lida = notaEntity.lida,
                dataleitura = notaEntity.dataleitura,
                notausuario = notaEntity.notausuario
            )
        }
    }

    override fun buscarPoesias(termo: String): Flow<List<Poesia>> { // Adicionado
        return poesiaQueries.buscarPoesias(termo)
            .asFlow()
            .mapToList(ioDispatcher)
            .map { it.map { buscarPoesias -> buscarPoesias.toDomain() } }
    }
}

class PoesiaPagingSource(
    private val poesiaQueries: Tbl_poesiaQueries
) : PagingSource<Int, GetPoesias>() {

    override fun getRefreshKey(state: PagingState<Int, GetPoesias>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetPoesias> {
        return try {
            val pageNumber = params.key ?: 0
            val pageSize = params.loadSize

            val poesias = withContext(Dispatchers.IO) {
                poesiaQueries.getPoesias(
                    limit = pageSize.toLong(),
                    offset = (pageNumber * pageSize).toLong()
                ).executeAsList()
            }

            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val nextKey = if (poesias.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(data = poesias, prevKey = prevKey, nextKey = nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
