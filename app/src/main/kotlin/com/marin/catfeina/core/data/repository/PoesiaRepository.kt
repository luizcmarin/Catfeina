/*
 *  Projeto: Catfeina
 *  Arquivo: PoesiaRepository.kt
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
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.marin.catfeina.core.sync.PoesiaSync
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import com.marin.catfeina.sqldelight.GetPoesiaAleatoria
import com.marin.catfeina.sqldelight.GetPoesiaCompletaById
import com.marin.catfeina.sqldelight.GetPoesiasCompletas
import com.marin.catfeina.sqldelight.GetPoesiasFavoritas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoesiaRepository @Inject constructor(
    private val poesiasQueries: CatfeinaDatabaseQueries
) {

    fun getPoesiaById(id: Long): Flow<GetPoesiaCompletaById?> {
        return poesiasQueries.getPoesiaCompletaById(id).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    fun getPoesiaAleatoria(): Flow<GetPoesiaAleatoria?> {
        return poesiasQueries.getPoesiaAleatoria().asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    fun getPoesiasFavoritas(): Flow<List<GetPoesiasFavoritas>> {
        return poesiasQueries.getPoesiasFavoritas().asFlow().mapToList(Dispatchers.IO)
    }

    fun getPoesiasCompletas(): Flow<List<GetPoesiasCompletas>> {
        return poesiasQueries.getPoesiasCompletas().asFlow().mapToList(Dispatchers.IO)
    }

    fun countPoesias(): Flow<Long> {
        return poesiasQueries.countPoesias().asFlow().mapToOne(Dispatchers.IO)
    }

    suspend fun upsertPoesias(poesias: List<PoesiaSync>) = withContext(Dispatchers.IO) {
        poesiasQueries.transaction {
            poesias.forEach { poesia ->
                poesiasQueries.upsertPoesia(
                    id = poesia.id,
                    categoria = poesia.categoria,
                    titulo = poesia.titulo,
                    textoBase = poesia.textoBase,
                    texto = poesia.texto,
                    textoFinal = poesia.textoFinal,
                    imagem = poesia.imagem,
                    autor = poesia.autor,
                    nota = poesia.nota,
                    campoUrl = poesia.campoUrl,
                    dataCriacao = poesia.dataCriacao
                )
            }
        }
    }

    suspend fun toggleFavorito(poesia: GetPoesiaCompletaById) = withContext(Dispatchers.IO) {
        val isFavorito = poesia.isFavorito != 1L
        poesiasQueries.upsertNota(
            poesiaId = poesia.id,
            ehFavorita = isFavorito,
            foiLida = poesia.isLido == 1L,
            dataFavoritado = if (isFavorito) System.currentTimeMillis() else null,
            dataLeitura = poesia.dataLeitura,
            notaUsuario = poesia.notaUsuario
        )
    }

    suspend fun toggleLido(poesia: GetPoesiaCompletaById) = withContext(Dispatchers.IO) {
        val isLido = poesia.isLido != 1L
        poesiasQueries.upsertNota(
            poesiaId = poesia.id,
            ehFavorita = poesia.isFavorito == 1L,
            foiLida = isLido,
            dataFavoritado = poesia.dataFavoritado,
            dataLeitura = if (isLido) System.currentTimeMillis() else null,
            notaUsuario = poesia.notaUsuario
        )
    }

    suspend fun updateNotaUsuario(id: Long, nota: String) = withContext(Dispatchers.IO) {
        val poesia = getPoesiaById(id).first() ?: return@withContext
        poesiasQueries.upsertNota(
            poesiaId = poesia.id,
            ehFavorita = poesia.isFavorito == 1L,
            foiLida = poesia.isLido == 1L,
            dataFavoritado = poesia.dataFavoritado,
            dataLeitura = poesia.dataLeitura,
            notaUsuario = nota
        )
    }
}