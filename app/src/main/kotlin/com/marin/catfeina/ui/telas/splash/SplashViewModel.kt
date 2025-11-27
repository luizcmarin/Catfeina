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
*  Nota: ViewModel para a SplashScreen, responsável por gerenciar o estado de
*  sincronização inicial e a verificação de atualização do app.
*
*/
package com.marin.catfeina.ui.telas.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catfeina.data.repositories.UserPreferencesRepository
import com.marin.catfeina.data.sync.AppUpdateDto
import com.marin.catfeina.usecases.SyncInitialDataUseCase
import com.marin.core.sync.SyncState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    syncInitialDataUseCase: SyncInitialDataUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Executando("Iniciando..."))
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _appUpdateInfo = MutableStateFlow<AppUpdateDto?>(null)
    val appUpdateInfo: StateFlow<AppUpdateDto?> = _appUpdateInfo.asStateFlow()

    init {
        viewModelScope.launch {
            syncInitialDataUseCase().collect { state ->
                _syncState.value = state
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.appUpdateInfo.collect { updateInfo ->
                _appUpdateInfo.value = updateInfo
            }
        }
    }

    fun onUpdateDialogDismissed() {
        _appUpdateInfo.value = null
    }
}
