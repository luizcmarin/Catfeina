/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/GetPoesiasPaginadasUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para obter o fluxo de dados paginados de poesias.
*
*/
package com.marin.catfeina.usecases

import androidx.paging.PagingData
import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.repositories.PoesiaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso que encapsula a lógica para obter um fluxo de poesias paginadas.
 * Atua como uma ponte entre o ViewModel e o repositório, seguindo a Clean Architecture.
 */
class GetPoesiasPaginadasUseCase @Inject constructor(
    private val poesiaRepository: PoesiaRepository
) {
    operator fun invoke(): Flow<PagingData<Poesia>> {
        return poesiaRepository.getPoesiasPaginadas()
    }
}
