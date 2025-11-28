/*
*  Projeto: Catfeina/Catverso
*  Arquivo: core/src/main/kotlin/com/marin/core/ui/GlobalUiEvent.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Define os eventos de UI globais que podem ser disparados de qualquer
*  parte da aplicação e observados pela UI principal.
*
*/
package com.marin.core.ui

/**
 * Representa eventos de UI que podem ser acionados em toda a aplicação.
 * Utilizado para comunicação desacoplada entre ViewModels e a UI principal (ex: Scaffold).
 */
sealed interface GlobalUiEvent {
    /**
     * Um evento para exibir uma mensagem em um Snackbar.
     * @param message A mensagem a ser exibida.
     * @param actionLabel O texto opcional para um botão de ação no Snackbar.
     */
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : GlobalUiEvent

    /**
     * Eventos relacionados a notificações e avisos importantes na UI.
     */
    sealed interface Notificacao : GlobalUiEvent {
        /**
         * Indica que há uma nova atualização de conteúdo (dados ou imagens) disponível para sincronização.
         */
        data object AtualizacaoDisponivel : Notificacao
    }
}
