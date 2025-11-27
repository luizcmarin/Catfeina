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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _uiState = MutableStateFlow<PersonagensUiState>(PersonagensUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        carregarPersonagens()
    }

    private fun carregarPersonagens() {
        getPersonagensUseCase()
            .onEach { personagens ->
                _uiState.value = PersonagensUiState.Success(personagens)
            }
            .catch { _uiState.value = PersonagensUiState.Error }
            .launchIn(viewModelScope)
    }
}
