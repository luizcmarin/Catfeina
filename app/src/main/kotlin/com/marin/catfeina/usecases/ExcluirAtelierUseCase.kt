/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/ExcluirAtelierUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para excluir uma nota do Atelier.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.repositories.AtelierRepository
import com.marin.catfeina.ui.GlobalUiEventManager
import com.marin.core.ui.GlobalUiEvent
import com.marin.core.ui.UiState
import javax.inject.Inject

class ExcluirAtelierUseCase @Inject constructor(
    private val atelierRepository: AtelierRepository,
    private val globalUiEventManager: GlobalUiEventManager
) {
    suspend operator fun invoke(id: Long) {
        when (val result = atelierRepository.deleteAtelier(id)) {
            is UiState.Success -> {
                globalUiEventManager.sendEvent(
                    GlobalUiEvent.ShowSnackbar("Nota excluída com sucesso!")
                )
            }
            is UiState.Error -> {
                globalUiEventManager.sendEvent(
                    GlobalUiEvent.ShowSnackbar("Erro ao excluir a nota: ${result.message}")
                )
            }
            else -> Unit // Ignora os outros estados
        }
    }
}
