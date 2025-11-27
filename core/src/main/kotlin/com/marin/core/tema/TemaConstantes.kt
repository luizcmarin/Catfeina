/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tema/TemaConstantes.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Centraliza os fatores de modificação (clarear, escurecer) para cada tema.
 *  Isso permite o ajuste fino da aparência de cada tema em um único lugar.
 *
 */

package com.marin.core.tema

object TemaConstantes {

    // --- FATORES PRIMAVERA (Vibrante e Leve) ---
    // Light Mode
    const val PRIMAVERA_L_PRIMARY_LIGHTEN = 0.05f
    const val PRIMAVERA_L_SECONDARY_LIGHTEN = 0.03f
    const val PRIMAVERA_L_TERTIARY_LIGHTEN = 0.05f
    const val PRIMAVERA_L_CONTAINER_LIGHTEN = 0.85f
    // Dark Mode
    const val PRIMAVERA_D_PRIMARY_LIGHTEN = 0.10f
    const val PRIMAVERA_D_SECONDARY_LIGHTEN = 0.08f
    const val PRIMAVERA_D_TERTIARY_LIGHTEN = 0.10f
    const val PRIMAVERA_D_CONTAINER_DARKEN = 0.25f


    // --- FATORES VERÃO (Quente e Brilhante) ---
    // Light Mode
    const val VERAO_L_PRIMARY_LIGHTEN = 0.10f
    const val VERAO_L_SECONDARY_LIGHTEN = 0.08f
    const val VERAO_L_TERTIARY_LIGHTEN = 0.10f
    const val VERAO_L_CONTAINER_LIGHTEN = 0.90f
    // Dark Mode
    const val VERAO_D_PRIMARY_LIGHTEN = 0.15f
    const val VERAO_D_SECONDARY_LIGHTEN = 0.12f
    const val VERAO_D_TERTIARY_LIGHTEN = 0.15f
    const val VERAO_D_CONTAINER_DARKEN = 0.20f


    // --- FATORES OUTONO (Terroso e Aconchegante) ---
    // Light Mode
    const val OUTONO_L_PRIMARY_DARKEN = 0.05f
    const val OUTONO_L_SECONDARY_DARKEN = 0.03f
    const val OUTONO_L_TERTIARY_DARKEN = 0.05f
    const val OUTONO_L_CONTAINER_LIGHTEN = 0.80f
    // Dark Mode
    const val OUTONO_D_PRIMARY_LIGHTEN = 0.05f
    const val OUTONO_D_SECONDARY_LIGHTEN = 0.03f
    const val OUTONO_D_TERTIARY_LIGHTEN = 0.05f
    const val OUTONO_D_CONTAINER_DARKEN = 0.30f


    // --- FATORES INVERNO (Frio e Sóbrio) ---
    // Light Mode
    const val INVERNO_L_PRIMARY_DARKEN = 0.10f
    const val INVERNO_L_SECONDARY_DARKEN = 0.08f
    const val INVERNO_L_TERTIARY_DARKEN = 0.10f
    const val INVERNO_L_CONTAINER_LIGHTEN = 0.88f
    // Dark Mode
    const val INVERNO_D_PRIMARY_LIGHTEN = 0.20f
    const val INVERNO_D_SECONDARY_LIGHTEN = 0.15f
    const val INVERNO_D_TERTIARY_LIGHTEN = 0.20f
    const val INVERNO_D_CONTAINER_DARKEN = 0.22f
}
