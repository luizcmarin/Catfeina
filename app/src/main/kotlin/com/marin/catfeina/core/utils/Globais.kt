/*
 *  Projeto: Catfeina
 *  Arquivo: Globais.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Arquivo que centraliza constantes, enums e outros valores globais
 *        utilizados em todo o código compartilhado do projeto.
 *
 */

package com.marin.catfeina.core.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val PT_BR_LOCALE: Locale = Locale.Builder().setLanguage("pt").setRegion("BR").build()

/**
 * Enum para categorizar os tipos de poesia.
 * Facilita a filtragem e a lógica de negócios sem usar strings "mágicas".
 */
enum class CategoriaPoesiaEnum {
    POESIA,
    CONTO,
    CRONICA,
    VERSO,
    PENSAMENTO,
    SONETO;
}

/**
 * Enum para representar os diferentes tipos de conteúdo que podem ser visitados no histórico.
 */
enum class TipoConteudoEnum {
    POESIA,
    PERSONAGEM,
    INFORMATIVO
}

/**
 * Converte um timestamp (Long) em uma string de data formatada.
 *
 * Transforma um valor de tempo Unix (em milissegundos) em uma data legível no
 * formato "dd/MM/yyyy". Se o timestamp for inválido, retorna "Data inválida".
 *
 * @receiver Long O timestamp em milissegundos desde a época Unix.
 * @return A data formatada como String ou "Data inválida" em caso de erro.
 */
fun Long.formataComoData(): String {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val netDate = Date(this)
        sdf.format(netDate)
    } catch (e: Exception) {
        "Data inválida"
    }
}

/**
 * Formata um timestamp (em milissegundos) como uma string de tempo relativo.
 * Ex: "há 5 minutos", "ontem", "em 2 horas".
 */
fun Long.formataTempoRelativo(): String {
    val now = System.currentTimeMillis()
    return DateUtils.getRelativeTimeSpanString(
        this,
        now,
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}

