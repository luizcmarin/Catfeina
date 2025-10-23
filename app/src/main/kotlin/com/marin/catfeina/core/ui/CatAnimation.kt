/*
 *  Projeto: Catfeina
 *  Arquivo: CatAnimation.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Componente reutilizável para a criação de animação com lottie.
 *
 */

package com.marin.catfeina.core.ui

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
    assetName: String
) {
    // Carrega a animação a partir da pasta 'assets' usando o nome do arquivo
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset(assetName)
    )

    // Renderiza a animação
    LottieAnimation(
        composition = composition,
        // Faz a animação repetir infinitamente
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}
