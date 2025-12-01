/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tema/Inverno.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Define o esquema de cores e tipografia para o tema Inverno.
 *
 */

package com.marin.core.tema

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// Cores definidas para o tema Inverno, modo claro.
private val LightColors = lightColorScheme(
    primary = inverno_theme_light_primary,
    onPrimary = inverno_theme_light_onPrimary,
    primaryContainer = inverno_theme_light_primaryContainer,
    onPrimaryContainer = inverno_theme_light_onPrimaryContainer,
    secondary = inverno_theme_light_secondary,
    onSecondary = inverno_theme_light_onSecondary,
    secondaryContainer = inverno_theme_light_secondaryContainer,
    onSecondaryContainer = inverno_theme_light_onSecondaryContainer,
    tertiary = inverno_theme_light_tertiary,
    onTertiary = inverno_theme_light_onTertiary,
    tertiaryContainer = inverno_theme_light_tertiaryContainer,
    onTertiaryContainer = inverno_theme_light_onTertiaryContainer,
    error = inverno_theme_light_error,
    onError = inverno_theme_light_onError,
    errorContainer = inverno_theme_light_errorContainer,
    onErrorContainer = inverno_theme_light_onErrorContainer,
    background = inverno_theme_light_background,
    onBackground = inverno_theme_light_onBackground,
    surface = inverno_theme_light_surface,
    onSurface = inverno_theme_light_onSurface,
    surfaceVariant = inverno_theme_light_surfaceVariant,
    onSurfaceVariant = inverno_theme_light_onSurfaceVariant,
    outline = inverno_theme_light_outline,
)

// Cores definidas para o tema Inverno, modo escuro.
private val DarkColors = darkColorScheme(
    primary = inverno_theme_dark_primary,
    onPrimary = inverno_theme_dark_onPrimary,
    primaryContainer = inverno_theme_dark_primaryContainer,
    onPrimaryContainer = inverno_theme_dark_onPrimaryContainer,
    secondary = inverno_theme_dark_secondary,
    onSecondary = inverno_theme_dark_onSecondary,
    secondaryContainer = inverno_theme_dark_secondaryContainer,
    onSecondaryContainer = inverno_theme_dark_onSecondaryContainer,
    tertiary = inverno_theme_dark_tertiary,
    onTertiary = inverno_theme_dark_onTertiary,
    tertiaryContainer = inverno_theme_dark_tertiaryContainer,
    onTertiaryContainer = inverno_theme_dark_onTertiaryContainer,
    error = inverno_theme_dark_error,
    onError = inverno_theme_dark_onError,
    errorContainer = inverno_theme_dark_errorContainer,
    onErrorContainer = inverno_theme_dark_onErrorContainer,
    background = inverno_theme_dark_background,
    onBackground = inverno_theme_dark_onBackground,
    surface = inverno_theme_dark_surface,
    onSurface = inverno_theme_dark_onSurface,
    surfaceVariant = inverno_theme_dark_surfaceVariant,
    onSurfaceVariant = inverno_theme_dark_onSurfaceVariant,
    outline = inverno_theme_dark_outline,
)

// Objeto que implementa o EsquemaDoTema para Inverno
object InvernoEsquema : EsquemaDoTema {
    override val lightModeColors = LightColors
    override val darkModeColors = DarkColors
    override val typography = CatfeinaTypography
}
