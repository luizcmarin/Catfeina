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

/*
 *
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: PoesiaRepository.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
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
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoesiaRepository @Inject constructor(
    private val poesiasQueries: CatfeinaDatabaseQueries
) {

    /**
     * Retorna uma única poesia pelo seu ID.
     */
    fun getPoesiaById(id: Long): Flow<GetPoesiaCompletaById?> {
        return poesiasQueries.getPoesiaCompletaById(id).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    /**
     * Retorna uma única poesia aleatória do banco de dados.
     * Emite um novo item sempre que os dados da poesia mudam.
     */
    fun getPoesiaAleatoria(): Flow<GetPoesiaAleatoria?> {
        return poesiasQueries.getPoesiaAleatoria().asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    /**
     * Retorna uma lista de todas as poesias favoritas do usuário.
     * Emite uma nova lista sempre que os dados das poesias favoritas mudam.
     */
    fun getPoesiasFavoritas(): Flow<List<GetPoesiasFavoritas>> {
        return poesiasQueries.getPoesiasFavoritas().asFlow().mapToList(Dispatchers.IO)
    }

    /**
     * Retorna uma lista de todas as poesias no banco de dados.
     * Emite uma nova lista sempre que os dados das poesias mudam.
     */
    fun getPoesiasCompletas(): Flow<List<GetPoesiasCompletas>> {
        return poesiasQueries.getPoesiasCompletas().asFlow().mapToList(Dispatchers.IO)
    }

    /**
     * Retorna o número total de poesias no banco de dados.
     */
    fun countPoesias(): Flow<Long> {
        return poesiasQueries.countPoesias().asFlow().mapToOne(Dispatchers.IO)
    }

    /**
     * Insere ou atualiza uma lista de poesias no banco de dados.
     * Esta operação é executada em uma única transação para garantir a atomicidade.
     */
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
}
