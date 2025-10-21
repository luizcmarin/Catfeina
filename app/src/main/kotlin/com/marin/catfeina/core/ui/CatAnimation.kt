/*
 *  Projeto: Catfeina
 *  Arquivo: CatAnimation.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Componente reutilizável para a criação de animação com lottie.
 *
 */

package com.marin.catfeina.core.ui

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun CatAnimation(
    modifier: Modifier = Modifier,
    // 1. Adicionamos um parâmetro para o ID do recurso da animação
    @RawRes animationResId: Int
) {
    // Carrega a animação a partir do ID do recurso fornecido
    val composition by rememberLottieComposition(
        // 2. Usamos o parâmetro aqui em vez de um valor fixo
        spec = LottieCompositionSpec.RawRes(animationResId)
    )

    // Renderiza a animação
    LottieAnimation(
        composition = composition,
        // Faz a animação repetir infinitamente
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}
