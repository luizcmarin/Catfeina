/*
 *  Projeto: Catfeina
 *  Arquivo: MainScreenViewModel.kt
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

package com.marin.catfeina.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marin.catfeina.core.data.repository.PoesiaRepository
import com.marin.catfeina.core.formatador.parser.ParserTextoFormatado
import com.marin.catfeina.sqldelight.GetPoesiaAleatoria
import com.marin.catfeina.sqldelight.GetPoesiasCompletas
import com.marin.catfeina.sqldelight.GetPoesiasPaginadas
import com.marin.catfeina.sqldelight.GetPoesiasFavoritas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class InicioUiState(
    val isLoading: Boolean = true,
    val poesiaAleatoria: GetPoesiaAleatoria? = null,
    val poesiasFavoritas: List<GetPoesiasFavoritas> = emptyList(),
    val todasAsPoesias: List<GetPoesiasCompletas> = emptyList(),
    val poesiasCount: Long = 0
)

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    poesiaRepository: PoesiaRepository,
    private val parser: ParserTextoFormatado
) : ViewModel() {

    val poesiasPaginadas: Flow<PagingData<GetPoesiasPaginadas>> =
        poesiaRepository.getPoesiasPaginadas().cachedIn(viewModelScope)

    private val _poesiaAleatoria = poesiaRepository.getPoesiaAleatoria()
    private val _poesiasFavoritas = poesiaRepository.getPoesiasFavoritas()
    private val _poesiasCount = poesiaRepository.countPoesias()

    val uiState: StateFlow<InicioUiState> = combine(
        _poesiaAleatoria, _poesiasFavoritas, _poesiasCount
    ) { aleatoria, favoritas, count ->
        InicioUiState(
            isLoading = false,
            poesiaAleatoria = aleatoria,
            poesiasFavoritas = favoritas,
            poesiasCount = count
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InicioUiState(isLoading = true)
    )

    fun extrairTextoPuro(textoCru: String): String {
        return parser.extrairTextoPuroParaTTS(textoCru)
    }
}
