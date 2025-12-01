/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/inicio/InicioViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela de início.
*
*/

package com.marin.catfeina.ui.telas.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.catfeina.usecases.GetPoesiasPaginadasUseCase
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Modelo de dados específico para a UI da tela de Início
data class PoesiaUiModel(
    val id: Long,
    val titulo: String,
    val resumo: String,
    val imagem: String?
)

sealed interface InicioUiState {
    data class Success(
        val poesiaDestaque: PoesiaUiModel?,
        val poesiasFavoritas: List<PoesiaUiModel>,
        val outrasPoesias: Flow<PagingData<PoesiaUiModel>> // Alterado para o tipo correto
    ) : InicioUiState

    data class Error(val message: String) : InicioUiState
    data object Loading : InicioUiState
}

@HiltViewModel
class InicioViewModel @Inject constructor(
    private val repository: PoesiaRepository,
    private val getPoesiasPaginadasUseCase: GetPoesiasPaginadasUseCase
) : ViewModel() {

    // Função auxiliar para converter o modelo de domínio em modelo de UI
    private fun Poesia.toUiModel(): PoesiaUiModel {
        return PoesiaUiModel(
            id = this.id,
            titulo = repository.extrairTitulo(this),
            resumo = repository.extrairResumo(this),
            imagem = repository.extrairImagemPrincipal(this)
        )
    }

    val uiState: StateFlow<InicioUiState> = combine(
        repository.getPoesiaAleatoria(),
        repository.getPoesiasFavoritas(),
    ) { aleatoriaResult, favoritasResult ->
        
        val error = (aleatoriaResult as? UiState.Error)?.message ?: (favoritasResult as? UiState.Error)?.message
        if(error != null) return@combine InicioUiState.Error(error)

        val aleatoriaState = (aleatoriaResult as? UiState.Success)?.data
        val favoritasState = (favoritasResult as? UiState.Success)?.data ?: emptyList()

        val favoritas = favoritasState.filter { it.id != aleatoriaState?.id }
        
        // O fluxo paginado é criado aqui e passado para o estado
        val outrasPaginadas = getPoesiasPaginadasUseCase()
            .map { pagingData -> 
                pagingData.map { it.toUiModel() } 
            }
            .cachedIn(viewModelScope)

        InicioUiState.Success(
            poesiaDestaque = aleatoriaState?.toUiModel(),
            poesiasFavoritas = favoritas.map { it.toUiModel() },
            outrasPoesias = outrasPaginadas
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = InicioUiState.Loading
    )
}
