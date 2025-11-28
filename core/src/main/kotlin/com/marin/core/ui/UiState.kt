/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/ui/UiState.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Um wrapper genérico e unificado para estados de UI (MVI).
 *
 */

package com.marin.core.ui

/**
 * Um wrapper genérico e unificado para representar os estados da UI.
 * Cobre tanto o carregamento de dados quanto o estado de processos assíncronos.
 */
sealed interface UiState<out T> {
    /**
     * O estado inicial ou de repouso, antes de qualquer operação começar.
     */
    data object Idle : UiState<Nothing>

    /**
     * O estado de carregamento, opcionalmente com uma mensagem de progresso.
     * @param message Uma mensagem opcional que descreve a etapa atual (ex: "Carregando...").
     */
    data class Loading(val message: String? = null) : UiState<Nothing>

    /**
     * O estado de sucesso, contendo os dados prontos para serem exibidos.
     * @param data Os dados carregados.
     */
    data class Success<T>(val data: T) : UiState<T>

    /**
     * O estado de erro, contendo uma mensagem para ser exibida ao usuário.
     * @param message Uma mensagem de erro descritiva.
     */
    data class Error(val message: String) : UiState<Nothing>
}
