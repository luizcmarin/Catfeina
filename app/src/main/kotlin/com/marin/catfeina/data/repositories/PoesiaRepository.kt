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
import com.marin.catfeina.sqldelight.PoesiaView
import com.marin.catfeina.sqldelight.Tbl_poesiaQueries
import com.marin.catfeina.sqldelight.Tbl_poesianota
import com.marin.catfeina.sqldelight.Tbl_poesianotaQueries
import com.marin.core.ui.UiState
import com.marin.core.util.safeFlowQuery
import com.marin.core.util.safeQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PoesiaRepository {
    fun getPoesiaById(id: Long): Flow<UiState<Poesia?>>
    fun getPoesias(): Flow<UiState<List<Poesia>>>
    fun getPoesiaAleatoria(): Flow<UiState<Poesia?>>
    fun getPoesiasFavoritas(): Flow<UiState<List<Poesia>>>
    fun buscarPoesias(termo: String): Flow<UiState<List<Poesia>>>
    fun countPoesias(): Flow<UiState<Long>>
    fun getPoesiasPaginadas(): Flow<PagingData<Poesia>>

    // Funções de extração de metadados
    fun extrairTitulo(poesia: Poesia): String
    fun extrairImagemPrincipal(poesia: Poesia): String?
    fun extrairResumo(poesia: Poesia): String
    fun limparMarkdownParaTts(poesia: Poesia): String

    // Ações do usuário
    suspend fun updateFavorita(id: Long, favorita: Boolean): UiState<Unit>
    suspend fun updateLida(id: Long, lida: Boolean): UiState<Unit>
    suspend fun updateNotaUsuario(id: Long, nota: String?): UiState<Unit>

    // Sincronização
    suspend fun upsertPoesias(poesias: List<Poesia>): UiState<Unit>
}

class PoesiaRepositoryImpl @Inject constructor(
    private val poesiaQueries: Tbl_poesiaQueries,
    private val poesiaNotaQueries: Tbl_poesianotaQueries,
    private val ioDispatcher: CoroutineDispatcher
) : PoesiaRepository {

    override fun getPoesiaById(id: Long): Flow<UiState<Poesia?>> = safeFlowQuery(ioDispatcher) {
        poesiaQueries.getPoesia(id).asFlow().mapToOneOrNull(ioDispatcher).map { it?.toDomain() }
    }

    override fun getPoesias(): Flow<UiState<List<Poesia>>> = safeFlowQuery(ioDispatcher) {
        poesiaQueries.getPoesias().asFlow().mapToList(ioDispatcher).map { list -> list.map { it.toDomain() } }
    }

    override fun getPoesiaAleatoria(): Flow<UiState<Poesia?>> = safeFlowQuery(ioDispatcher) {
        poesiaQueries.getPoesiaAleatoria().asFlow().mapToOneOrNull(ioDispatcher).map { it?.toDomain() }
    }

    override fun getPoesiasFavoritas(): Flow<UiState<List<Poesia>>> = safeFlowQuery(ioDispatcher) {
        poesiaQueries.getPoesiasFavoritas().asFlow().mapToList(ioDispatcher).map { list -> list.map { it.toDomain() } }
    }

    override fun buscarPoesias(termo: String): Flow<UiState<List<Poesia>>> = safeFlowQuery(ioDispatcher) {
        poesiaQueries.buscarPoesias(termo).asFlow().mapToList(ioDispatcher).map { list -> list.map { it.toDomain() } }
    }

    override fun countPoesias(): Flow<UiState<Long>> = safeFlowQuery(ioDispatcher) {
        poesiaQueries.countPoesias().asFlow().mapToOne(ioDispatcher)
    }

    override fun getPoesiasPaginadas(): Flow<PagingData<Poesia>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { PoesiaPagingSource(poesiaQueries, ioDispatcher) }
        ).flow.map { pagingData ->
            pagingData.map { poesiaView -> poesiaView.toDomain() }
        }
    }

    override fun extrairTitulo(poesia: Poesia): String {
        return poesia.texto.lines().firstOrNull { it.startsWith("# ") }?.removePrefix("# ")?.trim() ?: "Sem Título"
    }

    override fun extrairImagemPrincipal(poesia: Poesia): String? {
        val regex = Regex("""!\[.*?]\((.*?)\)""")
        return regex.find(poesia.texto)?.groupValues?.get(1)
    }

    override fun extrairResumo(poesia: Poesia): String {
        return poesia.texto.lines().firstOrNull { it.isNotBlank() && !it.startsWith("#") && !it.startsWith("!") }?.trim() ?: ""
    }

    override fun limparMarkdownParaTts(poesia: Poesia): String {
        return poesia.texto
            .replace(Regex("""!\[.*?]\(.*?\)"""), "") // Remove imagens
            .replace(Regex("""#?#?#?#?#? """), "") // Remove títulos
            .replace(Regex("""(\*\*|__)(?=\S)(.+?[*_]*)(?<=\S)\1"""), "$2") // Remove negrito
            .replace(Regex("""([*_])(?=\S)(.+?)(?<=\S)\1"""), "$2") // Remove itálico
            .replace(Regex("""(~~)(?=\S)(.+?)(?<=\S)\1"""), "$2") // Remove tachado
            .replace(Regex("""`(.+?)`"""), "$1") // Remove código inline
            .replace(Regex(""">"""), "") // Remove citações
            .trim()
    }

    override suspend fun updateFavorita(id: Long, favorita: Boolean): UiState<Unit> = safeQuery(ioDispatcher) {
        val notaExistente = poesiaNotaQueries.getNota(id).executeAsOneOrNull()
        val nota = notaExistente ?: Tbl_poesianota(
            poesiaid = id,
            favorita = 0, // O valor será sobrescrito pelo `copy`
            lida = 0,
            dataleitura = null,
            notausuario = null
        )

        val notaAtualizada = nota.copy(favorita = if (favorita) 1L else 0L)

        poesiaNotaQueries.upsertNota(
            poesiaid = notaAtualizada.poesiaid,
            favorita = notaAtualizada.favorita,
            lida = notaAtualizada.lida,
            dataleitura = notaAtualizada.dataleitura,
            notausuario = notaAtualizada.notausuario
        )
    }

    override suspend fun updateLida(id: Long, lida: Boolean): UiState<Unit> = safeQuery(ioDispatcher) {
        val notaExistente = poesiaNotaQueries.getNota(id).executeAsOneOrNull()
        val nota = notaExistente ?: Tbl_poesianota(
            poesiaid = id,
            favorita = 0,
            lida = 0, // O valor será sobrescrito pelo `copy`
            dataleitura = null, // O valor será sobrescrito pelo `copy`
            notausuario = null
        )

        val timestamp = if (lida) System.currentTimeMillis() else nota.dataleitura
        val notaAtualizada = nota.copy(
            lida = if (lida) 1L else 0L,
            dataleitura = timestamp
        )

        poesiaNotaQueries.upsertNota(
            poesiaid = notaAtualizada.poesiaid,
            favorita = notaAtualizada.favorita,
            lida = notaAtualizada.lida,
            dataleitura = notaAtualizada.dataleitura,
            notausuario = notaAtualizada.notausuario
        )
    }

    override suspend fun updateNotaUsuario(id: Long, nota: String?): UiState<Unit> = safeQuery(ioDispatcher) {
        val notaExistente = poesiaNotaQueries.getNota(id).executeAsOneOrNull()
        val baseNota = notaExistente ?: Tbl_poesianota(
            poesiaid = id,
            favorita = 0,
            lida = 0,
            dataleitura = null,
            notausuario = null // O valor será sobrescrito pelo `copy`
        )

        val notaAtualizada = baseNota.copy(notausuario = nota)

        poesiaNotaQueries.upsertNota(
            poesiaid = notaAtualizada.poesiaid,
            favorita = notaAtualizada.favorita,
            lida = notaAtualizada.lida,
            dataleitura = notaAtualizada.dataleitura,
            notausuario = notaAtualizada.notausuario
        )
    }

    override suspend fun upsertPoesias(poesias: List<Poesia>): UiState<Unit> = safeQuery(ioDispatcher) {
        poesiaQueries.transaction {
            poesias.forEach { poesia ->
                val entity = poesia.toPoesiaEntity()
                poesiaQueries.upsertPoesia(
                    id = entity.id,
                    texto = entity.texto,
                    anterior = entity.anterior,
                    proximo = entity.proximo,
                    atualizadoem = entity.atualizadoem
                )
            }
        }
    }
}

class PoesiaPagingSource(
    private val poesiaQueries: Tbl_poesiaQueries,
    private val ioDispatcher: CoroutineDispatcher
) : PagingSource<Int, PoesiaView>() {

    override fun getRefreshKey(state: PagingState<Int, PoesiaView>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PoesiaView> {
        return try {
            val pageNumber = params.key ?: 0
            val pageSize = params.loadSize.toLong()
            val offset = (pageNumber * pageSize)

            val poesias = withContext(ioDispatcher) {
                poesiaQueries.getPoesiasPaginadas(limit = pageSize, offset = offset).executeAsList()
            }

            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val nextKey = if (poesias.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(data = poesias, prevKey = prevKey, nextKey = nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
