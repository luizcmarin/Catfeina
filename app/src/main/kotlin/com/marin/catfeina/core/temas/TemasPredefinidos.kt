/*
 *  Projeto: Catfeina
 *  Arquivo: TemasPredefinidos.kt
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

/*
 *
 *  Projeto: Catfeina
 *  Arquivo: TemasPredefinidos.kt
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: TemasPredefinidos.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
 *
 */

package com.marin.catfeina.core.temas

import com.marin.catfeina.core.temas.inverno.invernoDarkScheme
import com.marin.catfeina.core.temas.inverno.invernoLightScheme
import com.marin.catfeina.core.temas.outono.outonoDarkScheme
import com.marin.catfeina.core.temas.outono.outonoLightScheme
import com.marin.catfeina.core.temas.primavera.primaveraDarkScheme
import com.marin.catfeina.core.temas.primavera.primaveraLightScheme
import com.marin.catfeina.core.temas.verao.veraoDarkScheme
import com.marin.catfeina.core.temas.verao.veraoLightScheme

/**
 * Objeto Singleton que fornece um mapa de todos os temas disponíveis na aplicação.
 * Centraliza a definição dos temas, facilitando a manutenção.
 */
object TemasPredefinidos {

    fun get(): Map<ThemeModelKey, ThemeModel> {
        return mapOf(
            ThemeModelKey.VERAO to ThemeModel(
                colorPalette = ColorPalette(
                    lightModeColors = veraoLightScheme,
                    darkModeColors = veraoDarkScheme
                )
            ),
            ThemeModelKey.OUTONO to ThemeModel(
                colorPalette = ColorPalette(
                    lightModeColors = outonoLightScheme,
                    darkModeColors = outonoDarkScheme
                )
            ),
            ThemeModelKey.INVERNO to ThemeModel(
                colorPalette = ColorPalette(
                    lightModeColors = invernoLightScheme,
                    darkModeColors = invernoDarkScheme
                )
            ),
            ThemeModelKey.PRIMAVERA to ThemeModel(
                colorPalette = ColorPalette(
                    lightModeColors = primaveraLightScheme,
                    darkModeColors = primaveraDarkScheme
                )
            )
        )
    }
}
