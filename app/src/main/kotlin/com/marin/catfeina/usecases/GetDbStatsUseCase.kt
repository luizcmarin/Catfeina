/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/GetDbStatsUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para obter estatísticas do banco de dados.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.repositories.AtelierRepository
import com.marin.catfeina.data.repositories.HistoricoRepository
import com.marin.catfeina.data.repositories.InformativoRepository
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.core.ui.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class DbStats(
    val totalPoesias: Long = 0,
    val totalAteliers: Long = 0,
    val totalHistoricos: Long = 0,
    val totalInformativos: Long = 0
)

class GetDbStatsUseCase @Inject constructor(
    private val poesiaRepository: PoesiaRepository,
    private val atelierRepository: AtelierRepository,
    private val historicoRepository: HistoricoRepository,
    private val informativoRepository: InformativoRepository
) {
    operator fun invoke(): Flow<UiState<DbStats>> {
        return combine(
            poesiaRepository.countPoesias(),
            atelierRepository.countAteliers(),
            historicoRepository.countHistoricos(),
            informativoRepository.countInformativos()
        ) { poesiaCount, atelierCount, historicoCount, informativoCount ->
            val pCount = (poesiaCount as? UiState.Success)?.data ?: 0L
            val aCount = (atelierCount as? UiState.Success)?.data ?: 0L
            val hCount = (historicoCount as? UiState.Success)?.data ?: 0L
            val iCount = (informativoCount as? UiState.Success)?.data ?: 0L

            UiState.Success(DbStats(
                totalPoesias = pCount,
                totalAteliers = aCount,
                totalHistoricos = hCount,
                totalInformativos = iCount
            ))
        }
    }
}
