/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/GetPoesiaUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para obter uma única poesia pelo seu identificador único (ID).
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.core.ui.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso que encapsula a lógica de negócio para buscar uma poesia específica por seu ID.
 * Serve como uma ponte clara entre a camada de apresentação (ViewModel) e a camada de dados (Repository),
 * garantindo que a lógica de busca seja reutilizável e isolada.
 */
class GetPoesiaUseCase @Inject constructor(
    private val repository: PoesiaRepository
) {
    /**
     * Executa o caso de uso.
     * @param id O identificador único da poesia a ser buscada.
     * @return Um Flow que emite a [Poesia] encontrada ou `null` se não existir.
     */
    operator fun invoke(id: Long): Flow<UiState<Poesia?>> {
        return repository.getPoesiaById(id)
    }
}
