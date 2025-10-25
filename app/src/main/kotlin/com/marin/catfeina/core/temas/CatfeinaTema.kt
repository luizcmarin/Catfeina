/*
 *  Projeto: Catfeina
 *  Arquivo: CatfeinaTema.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Define o tema principal do aplicativo Catfeina.
 *
 */

package com.marin.catfeina.core.temas

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView

@Composable
fun CatfeinaTema(
    gerenciadorTemas: GerenciadorTemas,
    content: @Composable () -> Unit
) {
    // Coleta o ColorScheme reativo do GerenciadorTemas.
    val colorScheme by gerenciadorTemas.colorScheme.collectAsState(
        initial = gerenciadorTemas.getAvailableThemes().getValue(TemaModelKey.VERAO).colorPalette.lightModeColors
    )

    // Coleta o estado de tema escuro. isDarkMode = true (escuro), isDarkMode = false (claro).
    val isDarkMode by gerenciadorTemas.isDarkMode.collectAsState(initial = false)

    val view = LocalView.current
    if (!view.isInEditMode) {
        // Usamos DisposableEffect para garantir que o controle dos ícones seja
        // aplicado sempre que o tema mudar e limpo quando o Composable sair da tela.
        DisposableEffect(isDarkMode) {
            val activity = view.context as ComponentActivity

            activity.enableEdgeToEdge(
                statusBarStyle = if (isDarkMode) {
                    // Tema escuro -> Fundo escuro -> Ícones claros.
                    SystemBarStyle.dark(
                        scrim = Color.TRANSPARENT
                    )
                } else {
                    // Tema claro -> Fundo claro -> Ícones escuros.
                    SystemBarStyle.light(
                        scrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT
                    )
                },
                // Define o estilo dos ícones da barra de navegação (botões).
                navigationBarStyle = if (isDarkMode) {
                    // Tema escuro -> Fundo escuro -> Ícones claros.
                    SystemBarStyle.dark(
                        scrim = Color.TRANSPARENT
                    )
                } else {
                    // Tema claro -> Fundo claro -> Ícones escuros.
                    SystemBarStyle.light(
                        scrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT
                    )
                }
            )
            onDispose {}
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
//        typography = Typography,
        content = content
    )
}
