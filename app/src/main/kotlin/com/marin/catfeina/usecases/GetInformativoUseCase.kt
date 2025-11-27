/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/GetInformativoUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para obter um informativo específico por sua chave.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.models.Informativo
import com.marin.catfeina.data.repositories.InformativoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInformativoUseCase @Inject constructor(
    private val repository: InformativoRepository
) {
    operator fun invoke(key: String): Flow<Informativo?> {
        return repository.getInformativo(key)
    }
}
