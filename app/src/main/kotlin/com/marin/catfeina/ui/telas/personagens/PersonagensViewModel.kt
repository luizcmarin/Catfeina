/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/personagens/PersonagensViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela de Personagens.
*
*/

package com.marin.catfeina.ui.telas.personagens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.models.Personagem
import com.marin.catfeina.usecases.GetPersonagensUseCase
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface PersonagensUiState {
    data class Success(val personagens: List<Personagem>) : PersonagensUiState
    data object Error : PersonagensUiState
    data object Loading : PersonagensUiState
}

@HiltViewModel
class PersonagensViewModel @Inject constructor(
    private val getPersonagensUseCase: GetPersonagensUseCase,
) : ViewModel() {

    val uiState: StateFlow<PersonagensUiState> = getPersonagensUseCase()
        .map { result ->
            when (result) {
                is UiState.Success -> PersonagensUiState.Success(result.data)
                is UiState.Error -> PersonagensUiState.Error
                is UiState.Loading -> PersonagensUiState.Loading
                is UiState.Idle -> PersonagensUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = PersonagensUiState.Loading
        )
}
