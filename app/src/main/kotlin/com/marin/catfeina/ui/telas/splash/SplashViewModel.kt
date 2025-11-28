/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/splash/SplashViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a SplashScreen, responsável por verificar atualizações na inicialização.
*
*/
package com.marin.catfeina.ui.telas.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.repositories.UserPreferencesRepository
import com.marin.catfeina.data.sync.AppUpdateDto
import com.marin.catfeina.ui.GlobalUiEventManager
import com.marin.catfeina.usecases.VerificarAtualizacoesUseCase
import com.marin.core.ui.GlobalUiEvent
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val verificarAtualizacoesUseCase: VerificarAtualizacoesUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val globalUiEventManager: GlobalUiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Loading())
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    private val _appUpdateInfo = MutableStateFlow<AppUpdateDto?>(null)
    val appUpdateInfo: StateFlow<AppUpdateDto?> = _appUpdateInfo.asStateFlow()

    init {
        viewModelScope.launch {
            val resultado = verificarAtualizacoesUseCase()

            if (resultado.atualizacaoDeDadosDisponivel) {
                globalUiEventManager.sendEvent(GlobalUiEvent.Notificacao.AtualizacaoDisponivel)
            }
            
            if (resultado.atualizacaoDeAppDisponivel) {
                 userPreferencesRepository.appUpdateInfo.collect { updateInfo ->
                    _appUpdateInfo.value = updateInfo
                }
            }

            _uiState.value = UiState.Success(Unit)
        }
    }

    fun onUpdateDialogDismissed() {
        _appUpdateInfo.value = null
    }
}
