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
*  Nota: Caso de uso para executar a sincronização inicial de dados da aplicação.
*
*/
package com.marin.catfeina.usecases

import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.catfeina.data.repositories.SyncRepository
import com.marin.core.sync.SyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Caso de uso que encapsula a regra de negócio para a sincronização inicial.
 * A regra é: executar a sincronização somente se o banco de dados estiver vazio.
 * Caso contrário, emite um estado de sucesso imediato.
 */
class SyncInitialDataUseCase @Inject constructor(
    private val syncRepository: SyncRepository,
    private val poesiaRepository: PoesiaRepository
) {
    operator fun invoke(): Flow<SyncState> = flow {
        if (poesiaRepository.countPoesias().first() == 0L) {
            // Banco de dados vazio, executa a sincronização real
            syncRepository.executarSincronizacao().collect { emit(it) }
        } else {
            // Dados já existem, emite sucesso imediato
            emit(SyncState.Sucesso("Dados já carregados."))
        }
    }
}
