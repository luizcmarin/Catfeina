/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/GetPoesiaAleatoriaUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: UseCase para obter uma poesia aleatória.
*
*/

package com.marin.catfeina.usecases

import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.core.ui.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPoesiaAleatoriaUseCase @Inject constructor(
    private val poesiaRepository: PoesiaRepository
) {
    operator fun invoke(): Flow<UiState<Poesia?>> = poesiaRepository.getPoesiaAleatoria()
}
