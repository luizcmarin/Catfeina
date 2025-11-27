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
import com.marin.catfeina.usecases.SalvarAtelierUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AtelierUiState {
    data class Success(val notas: List<Atelier>) : AtelierUiState
    data object Error : AtelierUiState
    data object Loading : AtelierUiState
}

@HiltViewModel
class AtelierViewModel @Inject constructor(
    private val atelierRepository: AtelierRepository,
    private val salvarAtelierUseCase: SalvarAtelierUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AtelierUiState>(AtelierUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            atelierRepository.getAteliers()
                .catch { _uiState.value = AtelierUiState.Error }
                .collect { notas ->
                    _uiState.value = AtelierUiState.Success(notas)
                }
        }
    }

    fun salvarNota(titulo: String, texto: String) {
        viewModelScope.launch {
            salvarAtelierUseCase(titulo, texto)
        }
    }
}
