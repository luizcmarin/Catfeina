/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/GetHistoricoUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para obter a lista de histórico de visualização.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.models.Historico
import com.marin.catfeina.data.repositories.HistoricoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Encapsula a lógica de negócio para obter o histórico de visualização.
 */
class GetHistoricoUseCase @Inject constructor(
    private val historicoRepository: HistoricoRepository
) {
    operator fun invoke(): Flow<List<Historico>> {
        return historicoRepository.getHistoricos()
    }
}
