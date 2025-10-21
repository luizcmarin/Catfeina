/*
 *  Projeto: Catfeina
 *  Arquivo: TextoFormatado.kt
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
 *  Arquivo: TextoFormatado.kt
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
 * //  Arquivo: TextoFormatado.kt
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

// ===================================================================================
// Arquivo: com.marin.catfeina.core.formatador.TextoFormatado.kt
//
// Descrição: Composable de alto nível que orquestra o parsing e a renderização
//            de texto formatado.
//
// Propósito:
// Este é o ponto de entrada principal para exibir texto formatado na UI. Ele
// recebe o texto cru, utiliza o `ParserTextoFormatado` para dividi-lo em uma
// lista de `ElementoConteudo` e, em seguida, itera sobre essa lista, delegando
// a renderização de cada elemento individual para o `RenderizarElementoConteudo`.
// ===================================================================================
package com.marin.catfeina.core.formatador

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun TextoFormatado(
    textoCru: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp, // Valor padrão
    viewModel: TextoFormatadoViewModel = hiltViewModel()
) {
    var tooltipText by remember { mutableStateOf<String?>(null) }

    val tooltipHandler = remember { 
        TooltipHandler { texto ->
            tooltipText = texto
        }
    }

    // Efeito para parsear o texto quando o textoCru mudar
    LaunchedEffect(textoCru) {
        viewModel.parsearTexto(textoCru)
    }

    val elementos by viewModel.elementos

    // Itera sobre a lista de elementos e renderiza o Composable correto para cada um.
    Column(modifier = modifier) {
        elementos.forEach { elemento ->
            RenderizarElementoConteudo(
                elemento = elemento,
                fontSize = fontSize,
                tooltipHandler = tooltipHandler
            )
        }
    }

    // Exibe o Tooltip customizado se houver um texto de tooltip.
    tooltipText?.let {
        Tooltip(
            texto = it,
            onDismissRequest = { tooltipText = null }
        )
    }
}
