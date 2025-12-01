/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/SalvarAtelierUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para salvar uma nota do Atelier.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.models.Atelier
import com.marin.catfeina.data.repositories.AtelierRepository
import com.marin.catfeina.ui.GlobalUiEventManager
import com.marin.core.ui.GlobalUiEvent
import com.marin.core.ui.UiState
import javax.inject.Inject

class SalvarAtelierUseCase @Inject constructor(
    private val atelierRepository: AtelierRepository,
    private val globalUiEventManager: GlobalUiEventManager
) {
    suspend operator fun invoke(nota: Atelier) {
        val isNewNote = nota.id == 0L
        val notaParaSalvar = if (isNewNote) {
            nota.copy(id = System.currentTimeMillis(), atualizadoem = System.currentTimeMillis())
        } else {
            nota.copy(atualizadoem = System.currentTimeMillis())
        }

        when (val result = atelierRepository.upsertAtelier(notaParaSalvar)) {
            is UiState.Success -> {
                globalUiEventManager.sendEvent(
                    GlobalUiEvent.ShowSnackbar("Nota salva com sucesso!")
                )
            }
            is UiState.Error -> {
                globalUiEventManager.sendEvent(
                    GlobalUiEvent.ShowSnackbar("Erro ao salvar a nota: ${result.message}")
                )
            }
            else -> Unit // Ignora os outros estados
        }
    }
}
