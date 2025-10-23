/*
 *  Projeto: Catfeina
 *  Arquivo: DateTimeUtils.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota:
 *
 */


package com.marin.catfeina.core.utils

import android.text.format.DateUtils

/**
 * Formata um timestamp (em milissegundos) como uma string de tempo relativo.
 * Ex: "há 5 minutos", "ontem", "em 2 horas".
 */
fun Long.formatRelativeTime(): String {
    val now = System.currentTimeMillis()
    return DateUtils.getRelativeTimeSpanString(
        this,
        now,
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}
