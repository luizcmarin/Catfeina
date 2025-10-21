/*
 *  Projeto: Catfeina
 *  Arquivo: Tooltip.kt
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
 *  Arquivo: Tooltip.kt
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
 * //  Arquivo: Tooltip.kt
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

// =============================================================================
// Arquivo: com.marin.catfeina.core.formatador.Tooltip.kt
// Descrição: Composable de UI para exibir um balão de dica (Tooltip) na tela.
// =============================================================================
package com.marin.catfeina.core.formatador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

/**
 * Exibe um balão de dica (Tooltip) na tela.
 *
 * Este Composable usa um `Popup` para sobrepor o conteúdo da tela. Ele é projetado
 * para ser exibido condicionalmente e descartado quando o usuário interage
 * fora dele.
 *
 * @param texto O conteúdo de texto a ser exibido dentro do balão de dica.
 * @param onDismissRequest Uma função a ser chamada quando o tooltip deve ser descartado.
 *                         Isso normalmente acontece quando o usuário clica fora da área do Popup.
 */
@Composable
fun Tooltip(
    texto: String,
    onDismissRequest: () -> Unit
) {
    // Popup é o componente ideal para exibir conteúdo flutuante sobre a UI principal.
    Popup(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp) // Adiciona um espaçamento das bordas da tela
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.inverseSurface)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = texto,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
