/*
 *  Projeto: Catfeina
 *  Arquivo: PersonagensViewModel.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota:
 *
 */

package com.marin.catfeina.ui.personagens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.core.data.repository.PersonagemRepository
import com.marin.catfeina.sqldelight.Personagens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class PersonagensUiState(
    val isLoading: Boolean = true,
    val personagens: List<Personagens> = emptyList()
)

@HiltViewModel
class PersonagensViewModel @Inject constructor(
    personagensRepository: PersonagemRepository
) : ViewModel() {

    val uiState: StateFlow<PersonagensUiState> = personagensRepository.getAllPersonagens()
        .map { personagens -> PersonagensUiState(isLoading = false, personagens = personagens) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PersonagensUiState(isLoading = true)
        )
}
