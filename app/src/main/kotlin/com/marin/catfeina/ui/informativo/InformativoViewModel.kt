/*
 *  Projeto: Catfeina
 *  Arquivo: InformativoViewModel.kt
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

package com.marin.catfeina.ui.informativo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.marin.catfeina.AppDestinationsArgs
import com.marin.catfeina.core.ui.UiState
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import com.marin.catfeina.sqldelight.Informativos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformativoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dbQueries: CatfeinaDatabaseQueries
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<Informativos>>(UiState.Loading)
    val uiState: StateFlow<UiState<Informativos>> = _uiState

    init {
        val chaveInformativo: String? = savedStateHandle[AppDestinationsArgs.INFORMATIVO_CHAVE_ARG]
        if (chaveInformativo == null) {
            _uiState.value = UiState.Error("Chave do informativo não encontrada.")
        } else {
            viewModelScope.launch {
                val informativo = dbQueries.getInformativoByChave(chaveInformativo)
                    .asFlow()
                    .mapToOneOrNull(Dispatchers.IO)
                    .firstOrNull()

                if (informativo != null) {
                    _uiState.value = UiState.Success(informativo)
                } else {
                    _uiState.value = UiState.Error("Informativo '$chaveInformativo' não encontrado.")
                }
            }
        }
    }
}