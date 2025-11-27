/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/BuscarPoesiasUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para realizar uma busca por poesias com base em um termo.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.repositories.PoesiaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Encapsula a lógica de negócio para buscar poesias.
 */
class BuscarPoesiasUseCase @Inject constructor(
    private val poesiaRepository: PoesiaRepository
) {
    operator fun invoke(termo: String): Flow<List<Poesia>> {
        // Adiciona os wildcards aqui para manter a query limpa
        val termoBusca = if (termo.isBlank()) "" else "%${termo}%"
        return poesiaRepository.buscarPoesias(termoBusca)
    }
}
