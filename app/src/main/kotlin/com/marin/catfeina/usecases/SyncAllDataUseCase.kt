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
*  Nota: Caso de uso para iniciar a sincronização completa de todos os dados sob demanda.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.repositories.SyncRepository
import javax.inject.Inject

/**
 * Caso de uso para disparar uma sincronização de dados completa manualmente.
 * Encapsula a lógica de negócio de sincronização, sendo a única porta de entrada
 * para a UI ou outras partes do domínio iniciarem este processo.
 */
class SyncAllDataUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    /**
     * Executa a sincronização de dados e retorna o resultado final.
     */
    suspend operator fun invoke(): Result<Unit> {
        return syncRepository.syncAll()
    }
}
