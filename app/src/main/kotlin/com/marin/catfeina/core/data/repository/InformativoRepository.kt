/*
 *  Projeto: Catfeina
 *  Arquivo: PersonagemRepository.kt
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

import com.marin.catfeina.core.data.AppDispatchers
import com.marin.catfeina.core.sync.InformativoSync
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório para gerenciar os dados de informativos.
 */
@Singleton
class InformativoRepository @Inject constructor(
    private val queries: CatfeinaDatabaseQueries,
    private val dispatchers: AppDispatchers
) {

    /**
     * Insere ou atualiza uma lista de informativos vindos da sincronização.
     *
     * @param informativos A lista de objetos InformativoSync para salvar.
     */
    suspend fun upsertInformativos(informativos: List<InformativoSync>) = withContext(dispatchers.io) {
        queries.transaction {
            informativos.forEach { informativo ->
                queries.upsertInformativo(
                    chave = informativo.chave,
                    titulo = informativo.titulo,
                    conteudo = informativo.conteudo,
                    dataAtualizacao = informativo.dataAtualizacao
                )
            }
        }
    }
}