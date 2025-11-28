/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/VerificarAtualizacoesUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para verificar se existem novas atualizações de conteúdo ou de app no servidor.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.repositories.SyncRepository
import javax.inject.Inject

/**
 * Verifica o manifesto no servidor e o compara com as versões locais para determinar
 * se uma sincronização de dados ou uma atualização do app é necessária.
 */
class VerificarAtualizacoesUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    /**
     * Executa a verificação.
     * @return [ResultadoVerificacao] indicando o que precisa ser atualizado.
     */
    suspend operator fun invoke(): ResultadoVerificacao {
        return syncRepository.verificarAtualizacoes()
    }
}

/**
 * Representa o resultado da verificação de atualizações.
 */
data class ResultadoVerificacao(
    val atualizacaoDeDadosDisponivel: Boolean = false,
    val atualizacaoDeAppDisponivel: Boolean = false
) {
    val algumaAtualizacaoDisponivel: Boolean
        get() = atualizacaoDeDadosDisponivel || atualizacaoDeAppDisponivel
}
