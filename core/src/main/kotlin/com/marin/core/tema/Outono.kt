/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tema/Outono.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Define o esquema de cores e tipografia para o tema Outono.
 *
 */

package com.marin.core.tema

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// Cores definidas para o tema Outono, modo claro.
private val LightColors = lightColorScheme(
    primary = outono_theme_light_primary,
    onPrimary = outono_theme_light_onPrimary,
    primaryContainer = outono_theme_light_primaryContainer,
    onPrimaryContainer = outono_theme_light_onPrimaryContainer,
    secondary = outono_theme_light_secondary,
    onSecondary = outono_theme_light_onSecondary,
    secondaryContainer = outono_theme_light_secondaryContainer,
    onSecondaryContainer = outono_theme_light_onSecondaryContainer,
    tertiary = outono_theme_light_tertiary,
    onTertiary = outono_theme_light_onTertiary,
    tertiaryContainer = outono_theme_light_tertiaryContainer,
    onTertiaryContainer = outono_theme_light_onTertiaryContainer,
    error = outono_theme_light_error,
    onError = outono_theme_light_onError,
    errorContainer = outono_theme_light_errorContainer,
    onErrorContainer = outono_theme_light_onErrorContainer,
    background = outono_theme_light_background,
    onBackground = outono_theme_light_onBackground,
    surface = outono_theme_light_surface,
    onSurface = outono_theme_light_onSurface,
    surfaceVariant = outono_theme_light_surfaceVariant,
    onSurfaceVariant = outono_theme_light_onSurfaceVariant,
    outline = outono_theme_light_outline,
)

// Cores definidas para o tema Outono, modo escuro.
private val DarkColors = darkColorScheme(
    primary = outono_theme_dark_primary,
    onPrimary = outono_theme_dark_onPrimary,
    primaryContainer = outono_theme_dark_primaryContainer,
    onPrimaryContainer = outono_theme_dark_onPrimaryContainer,
    secondary = outono_theme_dark_secondary,
    onSecondary = outono_theme_dark_onSecondary,
    secondaryContainer = outono_theme_dark_secondaryContainer,
    onSecondaryContainer = outono_theme_dark_onSecondaryContainer,
    tertiary = outono_theme_dark_tertiary,
    onTertiary = outono_theme_dark_onTertiary,
    tertiaryContainer = outono_theme_dark_tertiaryContainer,
    onTertiaryContainer = outono_theme_dark_onTertiaryContainer,
    error = outono_theme_dark_error,
    onError = outono_theme_dark_onError,
    errorContainer = outono_theme_dark_errorContainer,
    onErrorContainer = outono_theme_dark_onErrorContainer,
    background = outono_theme_dark_background,
    onBackground = outono_theme_dark_onBackground,
    surface = outono_theme_dark_surface,
    onSurface = outono_theme_dark_onSurface,
    surfaceVariant = outono_theme_dark_surfaceVariant,
    onSurfaceVariant = outono_theme_dark_onSurfaceVariant,
    outline = outono_theme_dark_outline,
)

// Objeto que implementa o EsquemaDoTema para Outono
object OutonoEsquema : EsquemaDoTema {
    override val lightModeColors = LightColors
    override val darkModeColors = DarkColors
    override val typography = CatfeinaTypography
}
