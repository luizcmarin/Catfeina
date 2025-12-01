/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tema/Verao.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Define o esquema de cores e tipografia para o tema Verão.
 *
 */

package com.marin.core.tema

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// Cores definidas para o tema Verão, modo claro.
private val LightColors = lightColorScheme(
    primary = verao_theme_light_primary,
    onPrimary = verao_theme_light_onPrimary,
    primaryContainer = verao_theme_light_primaryContainer,
    onPrimaryContainer = verao_theme_light_onPrimaryContainer,
    secondary = verao_theme_light_secondary,
    onSecondary = verao_theme_light_onSecondary,
    secondaryContainer = verao_theme_light_secondaryContainer,
    onSecondaryContainer = verao_theme_light_onSecondaryContainer,
    tertiary = verao_theme_light_tertiary,
    onTertiary = verao_theme_light_onTertiary,
    tertiaryContainer = verao_theme_light_tertiaryContainer,
    onTertiaryContainer = verao_theme_light_onTertiaryContainer,
    error = verao_theme_light_error,
    onError = verao_theme_light_onError,
    errorContainer = verao_theme_light_errorContainer,
    onErrorContainer = verao_theme_light_onErrorContainer,
    background = verao_theme_light_background,
    onBackground = verao_theme_light_onBackground,
    surface = verao_theme_light_surface,
    onSurface = verao_theme_light_onSurface,
    surfaceVariant = verao_theme_light_surfaceVariant,
    onSurfaceVariant = verao_theme_light_onSurfaceVariant,
    outline = verao_theme_light_outline,
)

// Cores definidas para o tema Verão, modo escuro.
private val DarkColors = darkColorScheme(
    primary = verao_theme_dark_primary,
    onPrimary = verao_theme_dark_onPrimary,
    primaryContainer = verao_theme_dark_primaryContainer,
    onPrimaryContainer = verao_theme_dark_onPrimaryContainer,
    secondary = verao_theme_dark_secondary,
    onSecondary = verao_theme_dark_onSecondary,
    secondaryContainer = verao_theme_dark_secondaryContainer,
    onSecondaryContainer = verao_theme_dark_onSecondaryContainer,
    tertiary = verao_theme_dark_tertiary,
    onTertiary = verao_theme_dark_onTertiary,
    tertiaryContainer = verao_theme_dark_tertiaryContainer,
    onTertiaryContainer = verao_theme_dark_onTertiaryContainer,
    error = verao_theme_dark_error,
    onError = verao_theme_dark_onError,
    errorContainer = verao_theme_dark_errorContainer,
    onErrorContainer = verao_theme_dark_onErrorContainer,
    background = verao_theme_dark_background,
    onBackground = verao_theme_dark_onBackground,
    surface = verao_theme_dark_surface,
    onSurface = verao_theme_dark_onSurface,
    surfaceVariant = verao_theme_dark_surfaceVariant,
    onSurfaceVariant = verao_theme_dark_onSurfaceVariant,
    outline = verao_theme_dark_outline,
)

// Objeto que implementa o EsquemaDoTema para Verão
object VeraoEsquema : EsquemaDoTema {
    override val lightModeColors = LightColors
    override val darkModeColors = DarkColors
    override val typography = CatfeinaTypography
}
