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
 *  Nota: Define o esquema generativo para o tema Outono, baseado em cores centrais
 *  e modificadores de tema.
 *
 */

package com.marin.core.tema

import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.marin.core.tema.CoresBase.CinzaBordaClara
import com.marin.core.tema.CoresBase.CinzaBordaEscura
import com.marin.core.tema.CoresBase.CinzaFundoClaro
import com.marin.core.tema.CoresBase.CinzaFundoEscuro
import com.marin.core.tema.CoresBase.CinzaSuperficieClara
import com.marin.core.tema.CoresBase.CinzaSuperficieEscura
import com.marin.core.tema.CoresBase.CinzaVerde
import com.marin.core.tema.CoresBase.Erro
import com.marin.core.tema.CoresBase.LaranjaOutono
import com.marin.core.tema.CoresBase.Preto
import com.marin.core.tema.CoresBase.VerdeAgua
import com.marin.core.tema.CoresBase.Branco
import com.marin.core.tema.TemaConstantes.OUTONO_D_CONTAINER_DARKEN
import com.marin.core.tema.TemaConstantes.OUTONO_D_PRIMARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.OUTONO_D_SECONDARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.OUTONO_D_TERTIARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.OUTONO_L_CONTAINER_LIGHTEN
import com.marin.core.tema.TemaConstantes.OUTONO_L_PRIMARY_DARKEN
import com.marin.core.tema.TemaConstantes.OUTONO_L_SECONDARY_DARKEN
import com.marin.core.tema.TemaConstantes.OUTONO_L_TERTIARY_DARKEN

// Objeto que implementa o EsquemaDoTema para Outono
object OutonoEsquema : EsquemaDoTema {

    override val lightModeColors = lightColorScheme(
        primary = LaranjaOutono.darken(OUTONO_L_PRIMARY_DARKEN),
        onPrimary = Branco,
        primaryContainer = LaranjaOutono.lighten(OUTONO_L_CONTAINER_LIGHTEN),
        onPrimaryContainer = LaranjaOutono.darken(0.3f),
        secondary = CinzaVerde.darken(OUTONO_L_SECONDARY_DARKEN),
        onSecondary = Branco,
        secondaryContainer = CinzaVerde.lighten(OUTONO_L_CONTAINER_LIGHTEN),
        onSecondaryContainer = CinzaVerde.darken(0.3f),
        tertiary = VerdeAgua.darken(OUTONO_L_TERTIARY_DARKEN),
        onTertiary = Branco,
        tertiaryContainer = VerdeAgua.lighten(OUTONO_L_CONTAINER_LIGHTEN),
        onTertiaryContainer = VerdeAgua.darken(0.3f),
        error = Erro,
        onError = Branco,
        errorContainer = Erro.lighten(0.8f),
        onErrorContainer = Erro.darken(0.4f),
        background = CinzaFundoClaro,
        onBackground = CinzaFundoEscuro,
        surface = CinzaFundoClaro,
        onSurface = CinzaFundoEscuro,
        surfaceVariant = CinzaSuperficieClara,
        onSurfaceVariant = CinzaSuperficieEscura,
        outline = CinzaBordaClara,
        scrim = Preto,
    )

    override val darkModeColors = darkColorScheme(
        primary = LaranjaOutono.lighten(OUTONO_D_PRIMARY_LIGHTEN),
        onPrimary = LaranjaOutono.darken(OUTONO_D_CONTAINER_DARKEN + 0.5f),
        primaryContainer = LaranjaOutono.darken(OUTONO_D_CONTAINER_DARKEN),
        onPrimaryContainer = LaranjaOutono.lighten(OUTONO_D_PRIMARY_LIGHTEN + 0.3f),
        secondary = CinzaVerde.lighten(OUTONO_D_SECONDARY_LIGHTEN),
        onSecondary = CinzaVerde.darken(OUTONO_D_CONTAINER_DARKEN + 0.4f),
        secondaryContainer = CinzaVerde.darken(OUTONO_D_CONTAINER_DARKEN),
        onSecondaryContainer = CinzaVerde.lighten(OUTONO_D_SECONDARY_LIGHTEN + 0.3f),
        tertiary = VerdeAgua.lighten(OUTONO_D_TERTIARY_LIGHTEN),
        onTertiary = VerdeAgua.darken(OUTONO_D_CONTAINER_DARKEN + 0.4f),
        tertiaryContainer = VerdeAgua.darken(OUTONO_D_CONTAINER_DARKEN),
        onTertiaryContainer = VerdeAgua.lighten(OUTONO_D_TERTIARY_LIGHTEN + 0.3f),
        error = Erro.lighten(0.3f),
        onError = Erro.darken(0.5f),
        errorContainer = Erro.darken(0.4f),
        onErrorContainer = Erro.lighten(0.8f),
        background = CinzaFundoEscuro,
        onBackground = CinzaSuperficieClara,
        surface = CinzaFundoEscuro,
        onSurface = CinzaSuperficieClara,
        surfaceVariant = CinzaSuperficieEscura,
        onSurfaceVariant = CinzaBordaClara,
        outline = CinzaBordaEscura,
        scrim = Preto,
    )

    // Tipografia específica para o tema Outono
    override val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        )
    )
}
