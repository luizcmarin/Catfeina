/*
 *  Projeto: Catfeina
 *  Arquivo: AtelierEditViewModel.kt
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


package com.marin.catfeina.features.atelier.presentation.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.features.atelier.data.repository.AtelierRepository
import com.marin.catfeina.sqldelight.Atelier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Representa os possíveis eventos que a UI pode enviar para o ViewModel ou que o ViewModel
 * pode enviar de volta para a UI (como eventos de navegação).
 */
sealed interface AtelierEditEvent {
    data class OnTituloChange(val titulo: String) : AtelierEditEvent
    data class OnConteudoChange(val conteudo: String) : AtelierEditEvent
    data class OnFixadaChange(val fixada: Boolean) : AtelierEditEvent
    object SalvarNota : AtelierEditEvent
    object DeletarNota : AtelierEditEvent

    object NavigateBack : AtelierEditEvent
}

/**
 * O estado da UI para a tela de edição do Atelier.
 *
 * @param nota A nota atualmente sendo editada.
 * @param isLoading Indica se a nota inicial está sendo carregada.
 * @param podeSalvar Indica se o botão de salvar deve estar habilitado.
 */
data class AtelierEditUiState(
    val nota: Atelier? = null,
    val isLoading: Boolean = true,
    val podeSalvar: Boolean = false
)

@HiltViewModel
class AtelierEditViewModel @Inject constructor(
    private val atelierRepository: AtelierRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AtelierEditUiState())
    val uiState = _uiState.asStateFlow()

    // Usamos SharedFlow para eventos de navegação que devem ser consumidos apenas uma vez.
    private val _eventFlow = MutableSharedFlow<AtelierEditEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // O ID é pego do argumento de navegação. Se for 0, é uma nova nota.
    private val notaId: Long = savedStateHandle.get<Long>("noteId") ?: 0L

    init {
        carregarNota()
    }

    fun onEvent(event: AtelierEditEvent) {
        when (event) {
            is AtelierEditEvent.OnTituloChange -> atualizarTitulo(event.titulo)
            is AtelierEditEvent.OnConteudoChange -> atualizarConteudo(event.conteudo)
            is AtelierEditEvent.OnFixadaChange -> atualizarFixada(event.fixada)
            AtelierEditEvent.SalvarNota -> salvarNota()
            AtelierEditEvent.DeletarNota -> deletarNota()
            else -> {}
        }
    }

    private fun carregarNota() {
        if (notaId == 0L) {
            // É uma nota nova. Inicializa com valores padrão.
            _uiState.update {
                it.copy(
                    isLoading = false,
                    nota = Atelier(id = 0, titulo = "", conteudo = "", dataAtualizacao = 0, fixada = false)
                )
            }
        } else {
            // É uma nota existente. Busca no repositório.
            viewModelScope.launch {
                val nota = atelierRepository.getAtelierById(notaId).first()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nota = nota,
                        podeSalvar = nota?.titulo?.isNotBlank() ?: false
                    )
                }
            }
        }
    }

    private fun atualizarTitulo(titulo: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nota = currentState.nota?.copy(titulo = titulo),
                podeSalvar = titulo.isNotBlank()
            )
        }
    }

    private fun atualizarConteudo(conteudo: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nota = currentState.nota?.copy(conteudo = conteudo)
            )
        }
    }

    private fun atualizarFixada(fixada: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                nota = currentState.nota?.copy(fixada = fixada)
            )
        }
    }

    private fun salvarNota() {
        viewModelScope.launch {
            _uiState.value.nota?.let { nota ->
                if (nota.titulo.isNotBlank()) {
                    atelierRepository.saveAtelier(
                        id = if (nota.id == 0L) null else nota.id, // Passa null para autoincremento
                        titulo = nota.titulo,
                        conteudo = nota.conteudo,
                        fixada = nota.fixada
                    )
                    _eventFlow.emit(AtelierEditEvent.NavigateBack)
                }
            }
        }
    }

    private fun deletarNota() {
        viewModelScope.launch {
            if (notaId != 0L) {
                atelierRepository.deleteAtelier(notaId)
                _eventFlow.emit(AtelierEditEvent.NavigateBack)
            }
        }
    }
}
