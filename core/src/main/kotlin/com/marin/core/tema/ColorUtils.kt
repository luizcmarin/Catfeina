/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tema/ColorUtils.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Funções de extensão para manipular objetos Color, permitindo a criação de
 *  temas generativos a partir de uma paleta base.
 *
 */

package com.marin.core.tema

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.max
import kotlin.math.min

/**
 * Clareia a cor em uma determinada porcentagem.
 * @param percentage A porcentagem para clarear (0.0 a 1.0).
 */
fun Color.lighten(percentage: Float): Color {
    val hsl = FloatArray(3)
    android.graphics.Color.colorToHSV(this.toArgb(), hsl)
    hsl[2] = min(1f, hsl[2] * (1f + percentage))
    return Color.hsv(hsl[0], hsl[1], hsl[2])
}

/**
 * Escurece a cor em uma determinada porcentagem.
 * @param percentage A porcentagem para escurecer (0.0 a 1.0).
 */
fun Color.darken(percentage: Float): Color {
    val hsl = FloatArray(3)
    android.graphics.Color.colorToHSV(this.toArgb(), hsl)
    hsl[2] = max(0f, hsl[2] * (1f - percentage))
    return Color.hsv(hsl[0], hsl[1], hsl[2])
}
