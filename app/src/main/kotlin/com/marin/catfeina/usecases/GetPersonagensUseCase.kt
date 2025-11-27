/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/GetPersonagensUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: UseCase para obter a lista de personagens.
*
*/

package com.marin.catfeina.usecases

import com.marin.catfeina.data.models.Personagem
import com.marin.catfeina.data.repositories.PersonagemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonagensUseCase @Inject constructor(
    private val personagemRepository: PersonagemRepository
) {
    operator fun invoke(): Flow<List<Personagem>> = personagemRepository.getPersonagens()
}
