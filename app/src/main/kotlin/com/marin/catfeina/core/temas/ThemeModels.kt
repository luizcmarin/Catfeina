/*
 *  Projeto: Catfeina
 *  Arquivo: ThemeModels.kt
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
 *  Arquivo: ThemeModels.kt
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
 * //  Arquivo: ThemeModels.kt
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

import androidx.compose.material3.ColorScheme

/**
 * Enum para as chaves de identificação dos temas.
 * Garante segurança de tipo ao selecionar e salvar temas.
 */
enum class ThemeModelKey {
    VERAO,  // Tema padrão
    OUTONO,
    INVERNO,
    PRIMAVERA
}

/**
 * Representa um único tema, contendo suas paletas de cores.
 */
data class ThemeModel(
    val colorPalette: ColorPalette
)

/**
 * Contém as paletas de cores para os modos claro e escuro de um tema.
 */
data class ColorPalette(
    val lightModeColors: ColorScheme,
    val darkModeColors: ColorScheme
)

