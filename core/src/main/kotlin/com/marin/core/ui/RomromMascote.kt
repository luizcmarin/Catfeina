/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/ui/RomromMascote.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Composable para o mascote animado Romrom.
 *
 */
package com.marin.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.marin.core.Constantes

@Composable
fun RomromMascote(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // Usa a constante centralizada para o nome do asset.
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(Constantes.ASSET_ROMROM))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
            .size(150.dp)
            .clickable(onClick = onClick)
    )
}
