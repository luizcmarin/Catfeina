/*
 *  Projeto: Catfeina
 *  Arquivo: TtsPlayerController.kt
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
 *  Projeto: Catfeina
 *  Arquivo: TtsPlayerController.kt
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
package com.marin.catfeina.core.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.core.utils.Icones

@Composable
fun TtsPlayerController(
    textToPlay: String,
    modifier: Modifier = Modifier,
    viewModel: TtsControllerViewModel = hiltViewModel()
) {
    val status by viewModel.ttsStatus.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val isPlayingOrPaused = status == TtsStatus.SPEAKING || status == TtsStatus.PAUSED

    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isPlayingOrPaused,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    color = ProgressIndicatorDefaults.linearColor,
                    trackColor = ProgressIndicatorDefaults.linearTrackColor,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = isPlayingOrPaused,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    IconButton(onClick = { viewModel.restart() }) {
                        Icon(
                            imageVector = Icones.ReiniciarTTS,
                            contentDescription = "Reiniciar leitura"
                        )
                    }
                }

                FloatingActionButton(
                    onClick = {
                        when (status) {
                            TtsStatus.SPEAKING -> viewModel.pause()
                            TtsStatus.PAUSED -> viewModel.resume()
                            else -> viewModel.play(textToPlay)
                        }
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    AnimatedContent(
                        targetState = status,
                        transitionSpec = {
                            (fadeIn() + scaleIn()).togetherWith(fadeOut() + scaleOut())
                        },
                        label = "PlayPauseIconAnimation"
                    ) { targetStatus ->
                        when (targetStatus) {
                            TtsStatus.SPEAKING -> Icon(
                                imageVector = Icones.PausarTTS,
                                contentDescription = "Pausar leitura"
                            )

                            TtsStatus.PAUSED -> Icon(
                                imageVector = Icones.ContinuarTTS,
                                contentDescription = "Continuar leitura"
                            )

                            else -> Icon(
                                imageVector = Icones.TocarTTS,
                                contentDescription = "Tocar texto"
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isPlayingOrPaused,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    IconButton(onClick = { viewModel.stop() }) {
                        Icon(
                            imageVector = Icones.PararTTS,
                            contentDescription = "Parar leitura"
                        )
                    }
                }
            }
        }
    }
}