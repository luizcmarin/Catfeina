package com.marin.catfeina.core.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import com.marin.catfeina.sqldelight.GetPoesiasPaginadas // <- Importe o novo tipo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Altere o tipo genérico para GetPoesiasPaginadas
class PoesiaPagingSource(
    private val poesiasQueries: CatfeinaDatabaseQueries
) : PagingSource<Int, GetPoesiasPaginadas>() {

    // Altere o tipo genérico aqui também
    override fun getRefreshKey(state: PagingState<Int, GetPoesiasPaginadas>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    // E aqui no tipo de retorno do LoadResult
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetPoesiasPaginadas> {
        return try {
            val pageNumber = params.key ?: 0
            val pageSize = params.loadSize

            val poesias = withContext(Dispatchers.IO) {
                poesiasQueries.getPoesiasPaginadas(
                    pageSize.toLong(),
                    (pageNumber * pageSize).toLong()
                ).executeAsList()
            }

            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val nextKey = if (poesias.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = poesias,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}