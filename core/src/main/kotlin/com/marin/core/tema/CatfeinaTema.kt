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
import androidx.compose.runtime.Composable

/**
 * O tema principal da aplicação Catfeina.
 * Este Composable envolve o conteúdo da UI e fornece os valores de cores, tipografia e formas
 * a partir do tema selecionado.
 *
 * @param chaveTema A chave do tema a ser aplicado (ex: ChaveTema.PRIMAVERA).
 * @param useDarkTheme Se deve usar o modo escuro do tema. Por padrão, segue a configuração do sistema.
 * @param content O conteúdo Composable filho que receberá o tema.
 */
@Composable
fun CatfeinaTema(
    chaveTema: ChaveTema? = ChaveTema.PRIMAVERA,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Obtém o esquema de tema completo (cores + tipografia) a partir do catálogo.
    val tema = CatalogoTemas.obter(chaveTema)

    // Seleciona a paleta de cores correta (claro ou escuro) com base no parâmetro.
    val colorScheme = if (useDarkTheme) {
        tema.esquema.darkModeColors
    } else {
        tema.esquema.lightModeColors
    }

    // Aplica o MaterialTheme com as cores e a tipografia do tema selecionado.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = tema.esquema.typography,
        content = content
    )
}
