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

import java.util.Locale

val PT_BR_LOCALE: Locale = Locale.Builder().setLanguage("pt").setRegion("BR").build()
val USER_PREFERENCES_NAME = "catfeina_user_preferences"


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
