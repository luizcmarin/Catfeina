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
import com.marin.core.ui.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Encapsula a lógica de negócio para buscar poesias.
 */
class BuscarPoesiasUseCase @Inject constructor(
    private val poesiaRepository: PoesiaRepository
) {
    operator fun invoke(termo: String): Flow<UiState<List<Poesia>>> {
        // O repositório já lida com os wildcards da busca LIKE.
        // Apenas repassamos o termo diretamente.
        return poesiaRepository.buscarPoesias(termo)
    }
}
