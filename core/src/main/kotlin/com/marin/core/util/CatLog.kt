/*
*  Projeto: Catfeina/Catverso
*  Arquivo: core/src/main/kotlin/com/marin/core/util/CatLog.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Wrapper de log customizado que garante que os logs sejam registrados apenas
*  em builds de depuração (debug) e que a TAG reflita o flavor atual do app.
*
*/

package com.marin.core.util

import android.util.Log
import com.marin.core.BuildConfig

object CatLog {

    private const val TAG = BuildConfig.LOG_TAG

    /**
     * Registra uma mensagem de log no nível DEBUG (d).
     * A chamada é executada apenas em builds de depuração.
     *
     * @param message A mensagem a ser registrada.
     */
    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    /**
     * Registra uma mensagem de log no nível INFO (i).
     * A chamada é executada apenas em builds de depuração.
     *
     * @param message A mensagem a ser registrada.
     */
    fun i(message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, message)
        }
    }

    /**
     * Registra uma mensagem de log no nível WARNING (w).
     * A chamada é executada apenas em builds de depuração.
     *
     * @param message A mensagem a ser registrada.
     */
    fun w(message: String) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, message)
        }
    }

    /**
     * Registra uma mensagem de log no nível ERROR (e).
     * A chamada é executada apenas em builds de depuração.
     *
     * @param message A mensagem a ser registrada.
     * @param throwable Uma exceção opcional para registrar junto com a mensagem.
     */
    fun e(message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message, throwable)
        }
    }
}
