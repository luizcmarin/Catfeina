/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/repositories/MeowRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Interface e implementação do repositório para gerenciar os dados da entidade Meow.
*
*/
package com.marin.catfeina.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.marin.catfeina.data.models.Meow
import com.marin.catfeina.data.models.toDomain
import com.marin.catfeina.data.models.toEntity
import com.marin.catfeina.sqldelight.Tbl_meowQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MeowRepository {
    fun getMeows(): Flow<List<Meow>>
    suspend fun upsertMeows(meows: List<Meow>)
    suspend fun deleteMeows(meows: List<Meow>)
    fun countMeows(): Flow<Long>
}

class MeowRepositoryImpl @Inject constructor(
    private val meowQueries: Tbl_meowQueries,
    private val ioDispatcher: CoroutineDispatcher
) : MeowRepository {

    override fun getMeows(): Flow<List<Meow>> {
        return meowQueries.getMeows()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { it.map { meow -> meow.toDomain() } }
    }

    override suspend fun upsertMeows(meows: List<Meow>) {
        withContext(ioDispatcher) {
            meowQueries.transaction {
                meows.forEach { meow ->
                    val entity = meow.toEntity()
                    meowQueries.upsertMeow(
                        id = entity.id,
                        texto = entity.texto,
                        atualizadoem = entity.atualizadoem
                    )
                }
            }
        }
    }

    override suspend fun deleteMeows(meows: List<Meow>) {
        withContext(ioDispatcher) {
            meowQueries.transaction {
                meows.forEach { meow ->
                    meowQueries.deleteMeow(meow.id)
                }
            }
        }
    }

    override fun countMeows(): Flow<Long> {
        return meowQueries.countMeows().asFlow().mapToOne(ioDispatcher)
    }
}
