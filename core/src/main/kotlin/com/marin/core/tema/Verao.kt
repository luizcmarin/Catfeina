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
 *  Nota: Define o esquema generativo para o tema Verão, baseado em cores centrais
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
import com.marin.core.tema.CoresBase.AmareloVerao
import com.marin.core.tema.CoresBase.CinzaBordaClara
import com.marin.core.tema.CoresBase.CinzaBordaEscura
import com.marin.core.tema.CoresBase.CinzaFundoClaro
import com.marin.core.tema.CoresBase.CinzaFundoEscuro
import com.marin.core.tema.CoresBase.CinzaSuperficieClara
import com.marin.core.tema.CoresBase.CinzaSuperficieEscura
import com.marin.core.tema.CoresBase.CinzaVerde
import com.marin.core.tema.CoresBase.Erro
import com.marin.core.tema.CoresBase.Preto
import com.marin.core.tema.CoresBase.VerdeAgua
import com.marin.core.tema.CoresBase.Branco
import com.marin.core.tema.TemaConstantes.VERAO_D_CONTAINER_DARKEN
import com.marin.core.tema.TemaConstantes.VERAO_D_PRIMARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.VERAO_D_SECONDARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.VERAO_D_TERTIARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.VERAO_L_CONTAINER_LIGHTEN
import com.marin.core.tema.TemaConstantes.VERAO_L_PRIMARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.VERAO_L_SECONDARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.VERAO_L_TERTIARY_LIGHTEN

// Objeto que implementa o EsquemaDoTema para Verão
object VeraoEsquema : EsquemaDoTema {

    override val lightModeColors = lightColorScheme(
        primary = AmareloVerao.lighten(VERAO_L_PRIMARY_LIGHTEN),
        onPrimary = Branco,
        primaryContainer = AmareloVerao.lighten(VERAO_L_CONTAINER_LIGHTEN),
        onPrimaryContainer = AmareloVerao.darken(0.35f),
        secondary = CinzaVerde.lighten(VERAO_L_SECONDARY_LIGHTEN),
        onSecondary = Branco,
        secondaryContainer = CinzaVerde.lighten(VERAO_L_CONTAINER_LIGHTEN),
        onSecondaryContainer = CinzaVerde.darken(0.3f),
        tertiary = VerdeAgua.lighten(VERAO_L_TERTIARY_LIGHTEN),
        onTertiary = Branco,
        tertiaryContainer = VerdeAgua.lighten(VERAO_L_CONTAINER_LIGHTEN),
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
        primary = AmareloVerao.lighten(VERAO_D_PRIMARY_LIGHTEN),
        onPrimary = AmareloVerao.darken(VERAO_D_CONTAINER_DARKEN + 0.4f),
        primaryContainer = AmareloVerao.darken(VERAO_D_CONTAINER_DARKEN),
        onPrimaryContainer = AmareloVerao.lighten(VERAO_D_PRIMARY_LIGHTEN + 0.3f),
        secondary = CinzaVerde.lighten(VERAO_D_SECONDARY_LIGHTEN),
        onSecondary = CinzaVerde.darken(VERAO_D_CONTAINER_DARKEN + 0.4f),
        secondaryContainer = CinzaVerde.darken(VERAO_D_CONTAINER_DARKEN),
        onSecondaryContainer = CinzaVerde.lighten(VERAO_D_SECONDARY_LIGHTEN + 0.3f),
        tertiary = VerdeAgua.lighten(VERAO_D_TERTIARY_LIGHTEN),
        onTertiary = VerdeAgua.darken(VERAO_D_CONTAINER_DARKEN + 0.4f),
        tertiaryContainer = VerdeAgua.darken(VERAO_D_CONTAINER_DARKEN),
        onTertiaryContainer = VerdeAgua.lighten(VERAO_D_TERTIARY_LIGHTEN + 0.3f),
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

    // Tipografia específica para o tema Verão
    override val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        )
    )
}
