/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/GlobalUiEventManager.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Gerenciador Singleton para eventos de UI globais, permitindo a comunicação
*  desacoplada entre ViewModels e a UI principal.
*
*/
package com.marin.catfeina.ui

import com.marin.core.ui.GlobalUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Um gerenciador de eventos singleton para comunicação de UI em toda a aplicação.
 * Usa um SharedFlow para transmitir eventos que podem ser coletados pela UI principal.
 */
@Singleton
class GlobalUiEventManager @Inject constructor() {

    private val _events = MutableSharedFlow<GlobalUiEvent>()
    val events = _events.asSharedFlow()

    /**
     * Envia um novo evento de UI para o fluxo.
     * @param event O [GlobalUiEvent] a ser enviado.
     */
    suspend fun sendEvent(event: GlobalUiEvent) {
        _events.emit(event)
    }
}
