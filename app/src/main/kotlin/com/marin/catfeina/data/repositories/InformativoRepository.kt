/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/repositories/InformativoRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Repositório que gerencia os dados dos informativos.
*
*/
package com.marin.catfeina.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.marin.catfeina.data.models.Informativo
import com.marin.catfeina.data.models.toDomain
import com.marin.catfeina.data.models.toEntity
import com.marin.catfeina.sqldelight.Tbl_informativoQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface InformativoRepository {
    suspend fun upsertInformativos(informativos: List<Informativo>)
    fun getInformativo(key: String): Flow<Informativo?>
    fun countInformativos(): Flow<Long>
}

class InformativoRepositoryImpl @Inject constructor(
    private val informativoQueries: Tbl_informativoQueries,
    private val ioDispatcher: CoroutineDispatcher
) : InformativoRepository {

    override suspend fun upsertInformativos(informativos: List<Informativo>) {
        withContext(ioDispatcher) {
            informativoQueries.transaction {
                informativos.forEach { informativo ->
                    val entity = informativo.toEntity()
                    informativoQueries.upsertInformativo(
                        chave = entity.chave,
                        titulo = entity.titulo,
                        conteudo = entity.conteudo,
                        imagem = entity.imagem,
                        atualizadoem = entity.atualizadoem
                    )
                }
            }
        }
    }

    override fun getInformativo(key: String): Flow<Informativo?> {
        return informativoQueries.getInformativo(key)
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.toDomain() }
    }

    override fun countInformativos(): Flow<Long> {
        return informativoQueries.countInformativos().asFlow().mapToOne(ioDispatcher)
    }
}
