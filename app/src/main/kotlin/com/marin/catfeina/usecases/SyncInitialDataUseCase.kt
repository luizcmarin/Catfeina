/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/SyncInitialDataUseCase.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: OBSOLETO. Este caso de uso foi substituído pela verificação sob demanda.
 *  Mantido para evitar quebras de compilação até a refatoração completa.
 *
 */
package com.marin.catfeina.usecases

import com.marin.catfeina.data.repositories.SyncRepository
import com.marin.core.ui.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncInitialDataUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    operator fun invoke(): Flow<UiState<Unit>> {
        return syncRepository.executarSincronizacao()
    }
}
