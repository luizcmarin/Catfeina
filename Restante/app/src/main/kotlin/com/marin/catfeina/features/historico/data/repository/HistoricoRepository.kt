/*
 *  Projeto: Catfeina
 *  Arquivo: HistoricoRepository.kt
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


package com.marin.catfeina.features.historico.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.marin.catfeina.core.data.AppDispatchers
import com.marin.catfeina.core.utils.TipoConteudoEnum
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import com.marin.catfeina.sqldelight.Historico
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório para gerenciar o histórico de visitas do usuário.
 */
@Singleton
class HistoricoRepository @Inject constructor(
    private val queries: CatfeinaDatabaseQueries,
    private val dispatchers: AppDispatchers
) {

    /**
     * Adiciona um novo registro ao histórico. Se um item com o mesmo tipo e ID de conteúdo
     * já existe, ele será atualizado (efeito do INSERT OR REPLACE).
     *
     * @param tipoConteudo O tipo do conteúdo visitado (ex: POESIA, PERSONAGEM).
     * @param conteudoId O ID do conteúdo visitado.
     * @param tituloDisplay O título a ser exibido no histórico.
     * @param imagemDisplay Uma imagem opcional para exibição.
     */
    suspend fun addHistorico(
        tipoConteudo: TipoConteudoEnum,
        conteudoId: Long,
        tituloDisplay: String,
        imagemDisplay: String? = null
    ) {
        withContext(dispatchers.io) {
            // Para garantir que o item mais recente fique no topo, removemos o antigo se existir
            // e inserimos um novo. O upsert direto poderia manter a posição antiga na lista.
            // Uma alternativa seria buscar por `tipoConteudo` e `conteudoId`, pegar o `id` e usar no upsert.
            // Por simplicidade e para garantir a ordem, vamos fazer um upsert com ID nulo.
            queries.upsertHistorico(
                id = null, // Deixar null para autoincremento e garantir que seja um novo registro
                tipoConteudo = tipoConteudo,
                conteudoId = conteudoId,
                tituloDisplay = tituloDisplay,
                imagemDisplay = imagemDisplay,
                dataVisita = System.currentTimeMillis()
            )
        }
    }

    /**
     * Retorna um Flow com a lista completa do histórico, ordenada pela visita mais recente.
     */
    fun getHistoricoCompleto(): Flow<List<Historico>> {
        return queries.getHistoricoCompleto()
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    /**
     * Apaga todos os registros da tabela de histórico.
     */
    suspend fun limparHistorico() {
        withContext(dispatchers.io) {
            queries.limparHistorico()
        }
    }
}
