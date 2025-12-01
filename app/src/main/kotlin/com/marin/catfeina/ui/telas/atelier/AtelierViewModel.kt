/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/atelier/AtelierViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela do Atelier.
*
*/
package com.marin.catfeina.ui.telas.atelier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.models.Atelier
import com.marin.catfeina.data.repositories.AtelierRepository
import com.marin.catfeina.usecases.ExcluirAtelierUseCase
import com.marin.catfeina.usecases.SalvarAtelierUseCase
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AtelierUiState {
    data class Success(val notas: List<Atelier>) : AtelierUiState
    data object Error : AtelierUiState
    data object Loading : AtelierUiState
}

@HiltViewModel
class AtelierViewModel @Inject constructor(
    atelierRepository: AtelierRepository,
    private val salvarAtelierUseCase: SalvarAtelierUseCase,
    private val excluirAtelierUseCase: ExcluirAtelierUseCase
) : ViewModel() {

    val uiState: StateFlow<AtelierUiState> = atelierRepository.getAteliers()
        .map { result ->
            when (result) {
                is UiState.Loading -> AtelierUiState.Loading
                is UiState.Success -> AtelierUiState.Success(result.data)
                is UiState.Error -> AtelierUiState.Error
                is UiState.Idle -> AtelierUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AtelierUiState.Loading
        )

    fun salvarNota(nota: Atelier) {
        viewModelScope.launch {
            salvarAtelierUseCase(nota)
        }
    }

    fun excluirNota(nota: Atelier) {
        viewModelScope.launch {
            excluirAtelierUseCase(nota.id)
        }
    }
}
