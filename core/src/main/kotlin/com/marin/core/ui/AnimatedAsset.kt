/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/ui/AnimatedAsset.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Componente genérico e reutilizável para exibir animações Lottie.
 *
 */

package com.marin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Um Composable genérico para exibir uma animação Lottie a partir da pasta 'assets'.
 *
 * @param assetName O nome do arquivo .json na pasta 'assets' (ex: "gato_carregando.json").
 * @param modifier O modificador a ser aplicado ao Composable.
 * @param iterations O número de vezes que a animação deve se repetir.
 *                   Use LottieConstants.IterateForever para um loop infinito.
 */
@Composable
fun AnimatedAsset(
    assetName: String,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset(assetName)
    )

    LottieAnimation(
        composition = composition,
        iterations = iterations,
        modifier = modifier
    )
}
