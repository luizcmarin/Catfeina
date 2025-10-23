/*
 *  Projeto: Catfeina
 *  Arquivo: AtelierListViewModel.kt
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


package com.marin.catfeina.features.atelier.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.features.atelier.data.repository.AtelierRepository
import com.marin.catfeina.sqldelight.Atelier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * UiState para a tela de lista do Atelier.
 *
 * @param isLoading Indica se a lista está sendo carregada.
 * @param notas A lista de notas do atelier.
 */
data class AtelierListUiState(
    val isLoading: Boolean = true,
    val notas: List<Atelier> = emptyList()
)

/**
 * ViewModel para a tela que exibe a lista de notas do Atelier.
 */
@HiltViewModel
class AtelierListViewModel @Inject constructor(
    private val atelierRepository: AtelierRepository
) : ViewModel() {

    /**
     * StateFlow que emite o estado atual da UI para a lista de notas.
     */
    val uiState: StateFlow<AtelierListUiState> = atelierRepository.getAllAtelier()
        .map { notas -> AtelierListUiState(isLoading = false, notas = notas) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AtelierListUiState(isLoading = true)
        )
}

