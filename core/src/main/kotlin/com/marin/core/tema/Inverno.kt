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
 *  Nota: Define o esquema generativo para o tema Inverno, baseado em cores centrais
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
import com.marin.core.tema.CoresBase.AzulInverno
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
import com.marin.core.tema.TemaConstantes.INVERNO_D_CONTAINER_DARKEN
import com.marin.core.tema.TemaConstantes.INVERNO_D_PRIMARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.INVERNO_D_SECONDARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.INVERNO_D_TERTIARY_LIGHTEN
import com.marin.core.tema.TemaConstantes.INVERNO_L_CONTAINER_LIGHTEN
import com.marin.core.tema.TemaConstantes.INVERNO_L_PRIMARY_DARKEN
import com.marin.core.tema.TemaConstantes.INVERNO_L_SECONDARY_DARKEN
import com.marin.core.tema.TemaConstantes.INVERNO_L_TERTIARY_DARKEN


// Objeto que implementa o EsquemaDoTema para Inverno
object InvernoEsquema : EsquemaDoTema {

    override val lightModeColors = lightColorScheme(
        primary = AzulInverno.darken(INVERNO_L_PRIMARY_DARKEN),
        onPrimary = Branco,
        primaryContainer = AzulInverno.lighten(INVERNO_L_CONTAINER_LIGHTEN),
        onPrimaryContainer = AzulInverno.darken(INVERNO_L_PRIMARY_DARKEN + 0.2f),
        secondary = CinzaVerde.darken(INVERNO_L_SECONDARY_DARKEN),
        onSecondary = Branco,
        secondaryContainer = CinzaVerde.lighten(INVERNO_L_CONTAINER_LIGHTEN),
        onSecondaryContainer = CinzaVerde.darken(INVERNO_L_SECONDARY_DARKEN + 0.2f),
        tertiary = VerdeAgua.darken(INVERNO_L_TERTIARY_DARKEN),
        onTertiary = Branco,
        tertiaryContainer = VerdeAgua.lighten(INVERNO_L_CONTAINER_LIGHTEN),
        onTertiaryContainer = VerdeAgua.darken(INVERNO_L_TERTIARY_DARKEN + 0.2f),
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
        primary = AzulInverno.lighten(INVERNO_D_PRIMARY_LIGHTEN),
        onPrimary = AzulInverno.darken(INVERNO_D_CONTAINER_DARKEN + 0.3f),
        primaryContainer = AzulInverno.darken(INVERNO_D_CONTAINER_DARKEN),
        onPrimaryContainer = AzulInverno.lighten(INVERNO_D_PRIMARY_LIGHTEN + 0.2f),
        secondary = CinzaVerde.lighten(INVERNO_D_SECONDARY_LIGHTEN),
        onSecondary = CinzaVerde.darken(INVERNO_D_CONTAINER_DARKEN + 0.3f),
        secondaryContainer = CinzaVerde.darken(INVERNO_D_CONTAINER_DARKEN),
        onSecondaryContainer = CinzaVerde.lighten(INVERNO_D_SECONDARY_LIGHTEN + 0.2f),
        tertiary = VerdeAgua.lighten(INVERNO_D_TERTIARY_LIGHTEN),
        onTertiary = VerdeAgua.darken(INVERNO_D_CONTAINER_DARKEN + 0.3f),
        tertiaryContainer = VerdeAgua.darken(INVERNO_D_CONTAINER_DARKEN),
        onTertiaryContainer = VerdeAgua.lighten(INVERNO_D_TERTIARY_LIGHTEN + 0.2f),
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

    // Tipografia específica para o tema Inverno
    override val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.75.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            letterSpacing = 1.sp
        )
    )
}
