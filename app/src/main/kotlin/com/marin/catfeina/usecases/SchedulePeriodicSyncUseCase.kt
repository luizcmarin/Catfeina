/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/usecases/SchedulePeriodicSyncUseCase.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Caso de uso para agendar o trabalho periódico de sincronização de dados.
*
*/
package com.marin.catfeina.usecases

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.marin.catfeina.data.sync.SyncWorker
import com.marin.core.Constantes
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Encapsula a lógica de negócio para agendar a tarefa de sincronização periódica.
 * A classe Application apenas invoca este caso de uso, sem conhecer os detalhes
 * da implementação do WorkManager.
 */
class SchedulePeriodicSyncUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            Constantes.SYNC_INTERVAL_HOURS,
            TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            Constantes.SYNC_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }
}
