/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/ui/CatfeinaLogoAnimation.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Exibe uma animação a partir de um AnimatedVectorDrawable (AVD).
 *
 */

package com.marin.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * Exibe uma animação a partir de um AnimatedVectorDrawable (AVD).
 * Este Composable foi desacoplado do módulo :app e agora pode ser usado em qualquer lugar.
 *
 * @param drawableId O ID do recurso AnimatedVectorDrawable (ex: R.drawable.meu_avd).
 * @param modifier O modificador a ser aplicado ao Composable.
 * @param contentDescription A descrição do conteúdo para acessibilidade.
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun CatfeinaLogoAnimation(
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    // 1. Carrega a DEFINIÇÃO do vetor animado a partir do ID fornecido.
    val animatedImageVector = AnimatedImageVector.animatedVectorResource(id = drawableId)

    // 2. Controla o estado da animação (início vs. fim).
    var atEnd by remember { mutableStateOf(true) }

    // 3. Obtém o PAINTER que será usado pelo Image Composable.
    val painter = rememberAnimatedVectorPainter(animatedImageVector, atEnd)

    // 4. Inicia a animação (uma única vez) quando o Composable entra na composição.
    LaunchedEffect(key1 = Unit) {
        atEnd = false
    }

    // 5. Exibe a animação.
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
    )
}
