/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tema/CatfeinaTema.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Composable principal que aplica o tema dinâmico à UI.
 *
 */
package com.marin.core.tema

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * O tema principal da aplicação Catfeina.
 * Este Composable envolve o conteúdo da UI e fornece os valores de cores, tipografia e formas
 * a partir do tema selecionado.
 *
 * @param chaveTema A chave do tema a ser aplicado (ex: ChaveTema.PRIMAVERA).
 * @param useDarkTheme Se deve usar o modo escuro do tema. Por padrão, segue a configuração do sistema.
 * @param escalaFonte O fator de escala a ser aplicado na tipografia. Padrão: 1.0f.
 * @param content O conteúdo Composable filho que receberá o tema.
 */
@Composable
fun CatfeinaTema(
    chaveTema: ChaveTema? = ChaveTema.PRIMAVERA,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    escalaFonte: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val tema = CatalogoTemas.obter(chaveTema)
    val colorScheme = if (useDarkTheme) tema.esquema.darkModeColors else tema.esquema.lightModeColors

    // Cria uma nova tipografia com a escala aplicada
    val typography = remember(tema.esquema.typography, escalaFonte) {
        val original = tema.esquema.typography
        Typography(
            displayLarge = original.displayLarge.copy(fontSize = original.displayLarge.fontSize * escalaFonte),
            displayMedium = original.displayMedium.copy(fontSize = original.displayMedium.fontSize * escalaFonte),
            displaySmall = original.displaySmall.copy(fontSize = original.displaySmall.fontSize * escalaFonte),
            headlineLarge = original.headlineLarge.copy(fontSize = original.headlineLarge.fontSize * escalaFonte),
            headlineMedium = original.headlineMedium.copy(fontSize = original.headlineMedium.fontSize * escalaFonte),
            headlineSmall = original.headlineSmall.copy(fontSize = original.headlineSmall.fontSize * escalaFonte),
            titleLarge = original.titleLarge.copy(fontSize = original.titleLarge.fontSize * escalaFonte),
            titleMedium = original.titleMedium.copy(fontSize = original.titleMedium.fontSize * escalaFonte),
            titleSmall = original.titleSmall.copy(fontSize = original.titleSmall.fontSize * escalaFonte),
            bodyLarge = original.bodyLarge.copy(fontSize = original.bodyLarge.fontSize * escalaFonte),
            bodyMedium = original.bodyMedium.copy(fontSize = original.bodyMedium.fontSize * escalaFonte),
            bodySmall = original.bodySmall.copy(fontSize = original.bodySmall.fontSize * escalaFonte),
            labelLarge = original.labelLarge.copy(fontSize = original.labelLarge.fontSize * escalaFonte),
            labelMedium = original.labelMedium.copy(fontSize = original.labelMedium.fontSize * escalaFonte),
            labelSmall = original.labelSmall.copy(fontSize = original.labelSmall.fontSize * escalaFonte)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
