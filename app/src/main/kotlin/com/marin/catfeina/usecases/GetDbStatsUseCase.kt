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
*  Nota: Caso de uso para obter estatísticas do banco de dados para a tela de depuração.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.repositories.AtelierRepository
import com.marin.catfeina.data.repositories.HistoricoRepository
import com.marin.catfeina.data.repositories.InformativoRepository
import com.marin.catfeina.data.repositories.MeowRepository
import com.marin.catfeina.data.repositories.PersonagemRepository
import com.marin.catfeina.data.repositories.PoesiaRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Coleta estatísticas de contagem de registros de todas as tabelas do banco de dados.
 */
class GetDbStatsUseCase @Inject constructor(
    private val poesiaRepository: PoesiaRepository,
    private val personagemRepository: PersonagemRepository,
    private val atelierRepository: AtelierRepository,
    private val historicoRepository: HistoricoRepository,
    private val informativoRepository: InformativoRepository,
    private val meowRepository: MeowRepository
) {
    suspend operator fun invoke(): List<Pair<String, Long>> {
        return listOf(
            "Poesias" to poesiaRepository.countPoesias().first(),
            "Personagens" to personagemRepository.countPersonagens().first(),
            "Atelier" to atelierRepository.countAteliers().first(),
            "Histórico" to historicoRepository.countHistoricos().first(),
            "Informativos" to informativoRepository.countInformativos().first(),
            "Meows" to meowRepository.countMeows().first()
        )
    }
}
