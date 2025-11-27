/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/repositories/PersonagemRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Repositório que gerencia os dados dos personagens.
*
*/
package com.marin.catfeina.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.marin.catfeina.data.models.Personagem
import com.marin.catfeina.data.models.toDomain
import com.marin.catfeina.data.models.toEntity
import com.marin.catfeina.sqldelight.Tbl_personagemQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PersonagemRepository {
    fun getPersonagens(): Flow<List<Personagem>>
    fun getPersonagem(id: Long): Flow<Personagem?>
    suspend fun upsertPersonagens(personagens: List<Personagem>)
    fun countPersonagens(): Flow<Long>
}

class PersonagemRepositoryImpl @Inject constructor(
    private val personagemQueries: Tbl_personagemQueries,
    private val ioDispatcher: CoroutineDispatcher
) : PersonagemRepository {

    override fun getPersonagens(): Flow<List<Personagem>> {
        return personagemQueries.getPersonagens()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { it.map { personagem -> personagem.toDomain() } }
    }

    override fun getPersonagem(id: Long): Flow<Personagem?> {
        return personagemQueries.getPersonagem(id)
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.toDomain() }
    }

    override suspend fun upsertPersonagens(personagens: List<Personagem>) {
        withContext(ioDispatcher) {
            personagemQueries.transaction {
                personagens.forEach { personagem ->
                    val entity = personagem.toEntity()
                    personagemQueries.upsertPersonagem(
                        id = entity.id,
                        nome = entity.nome,
                        biografia = entity.biografia,
                        imagem = entity.imagem,
                        atualizadoem = entity.atualizadoem
                    )
                }
            }
        }
    }

    override fun countPersonagens(): Flow<Long> {
        return personagemQueries.countPersonagens().asFlow().mapToOne(ioDispatcher)
    }
}
