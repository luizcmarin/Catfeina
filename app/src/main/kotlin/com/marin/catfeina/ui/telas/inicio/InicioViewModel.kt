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
import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.usecases.GetPoesiasPaginadasUseCase
import com.marin.catfeina.usecases.GetPoesiaAleatoriaUseCase
import com.marin.catfeina.usecases.GetPoesiasFavoritasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

data class InicioUiState(
    val isLoading: Boolean = true,
    val poesiaAleatoria: Poesia? = null,
    val poesiasFavoritas: List<Poesia> = emptyList(),
)

@HiltViewModel
class InicioViewModel @Inject constructor(
    getPoesiasPaginadasUseCase: GetPoesiasPaginadasUseCase,
    getPoesiaAleatoriaUseCase: GetPoesiaAleatoriaUseCase, // a ser criado
    getPoesiasFavoritasUseCase: GetPoesiasFavoritasUseCase, // a ser criado
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState = _uiState.asStateFlow()

    val poesiasPaginadas: Flow<PagingData<Poesia>> = getPoesiasPaginadasUseCase()
        .cachedIn(viewModelScope)

    init {
        combine(
            getPoesiaAleatoriaUseCase(),
            getPoesiasFavoritasUseCase()
        ) { aleatoria, favoritas ->
            _uiState.value = InicioUiState(
                isLoading = false,
                poesiaAleatoria = aleatoria,
                poesiasFavoritas = favoritas
            )
        }.launchIn(viewModelScope)
    }
}
