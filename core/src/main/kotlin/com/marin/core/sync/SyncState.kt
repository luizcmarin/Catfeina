/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/sync/SyncState.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Um wrapper genérico para representar o estado de um processo de sincronização.
 *
 */

package com.marin.core.sync

/**
 * Um wrapper genérico para estados de processos assíncronos, como a sincronização de dados.
 */
sealed interface SyncState {
    /**
     * O estado de progresso, contendo uma mensagem para ser exibida na UI.
     * @param message A mensagem que descreve a etapa atual (ex: "Baixando dados...").
     */
    data class Executando(val message: String) : SyncState

    /**
     * O estado de sucesso, indicando que o processo foi concluído.
     * @param message Uma mensagem opcional de sucesso.
     */
    data class Sucesso(val message: String? = null) : SyncState

    /**
     * O estado de falha, contendo uma mensagem para ser exibida ao usuário.
     * @param message Uma mensagem de erro descritiva.
     */
    data class Falha(val message: String) : SyncState
}
