/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/SyncAllDataUseCase.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Caso de uso para executar a sincronização completa de todos os dados.
 *
 */
package com.marin.catfeina.usecases

import com.marin.catfeina.data.repositories.SyncRepository
import javax.inject.Inject

/**
 * Caso de uso para acionar a sincronização completa de todos os dados em segundo plano.
 * Geralmente utilizado por um Worker.
 */
class SyncAllDataUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    suspend operator fun invoke() = syncRepository.syncAll()
}
