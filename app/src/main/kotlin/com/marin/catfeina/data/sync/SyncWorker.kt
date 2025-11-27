/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/sync/SyncWorker.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Implementação do Worker para realizar a sincronização de dados em background.
 *
 */
package com.marin.catfeina.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.marin.catfeina.data.repositories.SyncRepository
import com.marin.core.util.CatLog
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker do WorkManager responsável por executar a lógica de sincronização de dados em background.
 *
 * Este worker é injetado pelo Hilt e delega a lógica de negócio para o [SyncRepository].
 * Ele é projetado para ser robusto, tratando os resultados da sincronização:
 * - [Result.success] se a sincronização for bem-sucedida.
 * - [Result.retry] se ocorrer uma falha controlada (ex: problema de rede), para que o WorkManager tente novamente.
 * - [Result.failure] se uma exceção inesperada ocorrer, para evitar novas tentativas desnecessárias.
 *
 * @param context O contexto da aplicação.
 * @param workerParams Parâmetros de configuração do Worker.
 * @param syncRepository O repositório que contém a lógica de sincronização.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        CatLog.d("SyncWorker: Iniciando trabalho de sincronização em background.")

        return try {
            val syncResult = syncRepository.syncAll()

            if (syncResult.isSuccess) {
                CatLog.d("SyncWorker: Sincronização em background concluída com sucesso.")
                Result.success()
            } else {
                CatLog.w("SyncWorker: Sincronização em background falhou. Tentando novamente mais tarde.")
                Result.retry()
            }
        } catch (e: Exception) {
            CatLog.e("SyncWorker: Exceção catastrófica durante a sincronização. O trabalho não será repetido.", e)
            Result.failure()
        }
    }
}
