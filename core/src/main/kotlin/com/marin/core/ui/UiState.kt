/*
 *  Projeto: Catfeina/Catverso *  Arquivo: core/ui/UiState.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Um wrapper genérico para estados de UI que precisam carregar dados (MVI).
 *
 */

package com.marin.core.ui

/**
 * Um wrapper genérico para estados de UI que precisam carregar dados.
 * Utilizado no padrão MVI (Model-View-Intent) para representar o estado da tela.
 */
sealed interface UiState<out T> {
    /**
     * O estado de carregamento inicial ou durante uma atualização.
     */
    data object Loading : UiState<Nothing>

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
