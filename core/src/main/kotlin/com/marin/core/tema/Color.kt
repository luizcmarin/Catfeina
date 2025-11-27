/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tema/Color.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Define a paleta de cores base para toda a aplicação. Os temas são gerados
 *  a partir destas cores, aplicando modificadores.
 *
 */

package com.marin.core.tema

import androidx.compose.ui.graphics.Color

/**
 * Objeto que centraliza as cores fundamentais e semânticas da aplicação.
 * Esta é a única fonte da verdade para as cores. Os temas são construídos
 * como variações (mais claras, mais escuras) destas cores base.
 */
object CoresBase {

    // --- Cores Primárias Representativas de Cada Tema ---
    val VerdePrimavera = Color(0xFF4C662B)
    val AmareloVerao = Color(0xFF6D5E0F)
    val LaranjaOutono = Color(0xFF8F4C38)
    val AzulInverno = Color(0xFF415F91)

    // --- Cores Secundárias e Terciárias Base ---
    // (Extraídas do tema Primavera como um padrão neutro)
    val CinzaVerde = Color(0xFF586249)
    val VerdeAgua = Color(0xFF386663)

    // --- Cores Funcionais ---
    val Erro = Color(0xFFBA1A1A)
    val Branco = Color(0xFFFFFFFF)
    val Preto = Color(0xFF000000)

    // --- Tons de Cinza Neutros ---
    val CinzaFundoClaro = Color(0xFFF9FAEF)
    val CinzaSuperficieClara = Color(0xFFE1E4D5)
    val CinzaBordaClara = Color(0xFF75796C)

    val CinzaFundoEscuro = Color(0xFF12140E)
    val CinzaSuperficieEscura = Color(0xFF44483D)
    val CinzaBordaEscura = Color(0xFF8F9285)
}
