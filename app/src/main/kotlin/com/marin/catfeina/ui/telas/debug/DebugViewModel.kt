/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/debug/DebugViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela de depuração.
*
*/
package com.marin.catfeina.ui.telas.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.usecases.GetDbStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DebugUiState(
    val buildInfo: List<Pair<String, String>> = emptyList(),
    val dbStats: List<Pair<String, Long>> = emptyList()
)

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val getDbStatsUseCase: GetDbStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebugUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadBuildInfo()
        loadDbStats()
    }

    private fun loadBuildInfo() {
        _uiState.update {
            it.copy(
                buildInfo = listOf(
                    "Build Type" to BuildConfig.BUILD_TYPE,
                    "Version Name" to BuildConfig.VERSION_NAME,
                    "Version Code" to BuildConfig.VERSION_CODE.toString(),
                    "Build Time" to BuildConfig.BUILD_TIME,
                    "Sync URL" to BuildConfig.SYNC_URL
                )
            )
        }
    }

    private fun loadDbStats() {
        viewModelScope.launch {
            val stats = getDbStatsUseCase()
            _uiState.update { it.copy(dbStats = stats) }
        }
    }
}
