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
import kotlin.math.max
import kotlin.math.min

/**
 * Clareia a cor em uma determinada porcentagem.
 * @param percentage A porcentagem para clarear (0.0 a 1.0).
 */
fun Color.lighten(percentage: Float): Color {
    val hsl = this.toHsl()
    val newLightness = min(1f, hsl[2] * (1f + percentage))
    return Color.hsl(hsl[0], hsl[1], newLightness)
}

/**
 * Escurece a cor em uma determinada porcentagem.
 * @param percentage A porcentagem para escurecer (0.0 a 1.0).
 */
fun Color.darken(percentage: Float): Color {
    val hsl = this.toHsl()
    val newLightness = max(0f, hsl[2] * (1f - percentage))
    return Color.hsl(hsl[0], hsl[1], newLightness)
}

/**
 * Converte uma cor de RGB para HSL (Hue, Saturation, Lightness).
 * O array retornado contém [hue, saturation, lightness].
 * Baseado na conversão padrão de cores.
 */
private fun Color.toHsl(): FloatArray {
    val r = this.red
    val g = this.green
    val b = this.blue

    val max = max(r, max(g, b))
    val min = min(r, min(g, b))

    val l = (max + min) / 2f

    val h: Float
    val s: Float

    if (max == min) {
        h = 0f
        s = 0f
    } else {
        val d = max - min
        s = if (l > 0.5f) d / (2f - max - min) else d / (max + min)
        h = when (max) {
            r -> (g - b) / d + (if (g < b) 6f else 0f)
            g -> (b - r) / d + 2f
            else -> (r - g) / d + 4f
        } / 6f
    }

    return floatArrayOf(h * 360, s, l)
}
