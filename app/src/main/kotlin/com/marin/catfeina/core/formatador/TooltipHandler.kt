/*
 *  Projeto: Catfeina
 *  Arquivo: TooltipHandler.kt
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
 *  Arquivo: TooltipHandler.kt
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
 * //  Arquivo: TooltipHandler.kt
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
// Arquivo: com.marin.catfeina.core.formatador.TooltipHandler.kt
//
// Descrição: Define a interface para desacoplar a lógica de exibição de tooltips
//            da lógica de análise de texto.
//
// Propósito:
// Esta interface serve como um contrato que permite ao `ParserTextoFormatado`
// solicitar a exibição de um tooltip sem conhecer os detalhes de implementação da UI.
// Qualquer Composable ou componente de UI que renderiza texto formatado e precisa
// exibir tooltips pode implementar esta interface, garantindo um design flexível
// e desacoplado entre a camada de lógica de parsing e a camada de UI.
// ===================================================================================

package com.marin.catfeina.core.formatador

fun interface TooltipHandler {
    fun mostrarTooltip(textoTooltip: String)
}