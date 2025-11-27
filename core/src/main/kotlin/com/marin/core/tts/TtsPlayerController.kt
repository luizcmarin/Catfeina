/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tts/TtsPlayerController.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Composable que apresenta os controles de UI para o Text-to-Speech (TTS).
 *
 */

package com.marin.core.tts

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
import com.marin.core.ui.Icones

@Composable
fun TtsPlayerController(
    textoParaTocar: String,
    modifier: Modifier = Modifier,
    viewModel: TtsControllerViewModel = hiltViewModel()
) {
    // Coleta os estados do ViewModel, alinhado com a nomenclatura em português
    val status by viewModel.status.collectAsStateWithLifecycle()
    val progresso by viewModel.progresso.collectAsStateWithLifecycle()
    val estaTocandoOuPausado = status == TtsStatus.FALANDO || status == TtsStatus.PAUSADO

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
                visible = estaTocandoOuPausado,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LinearProgressIndicator(
                    progress = { progresso }, // Usa a variável em português
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
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
                    visible = estaTocandoOuPausado,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    IconButton(onClick = { viewModel.reiniciar() }) {
                        Icon(
                            imageVector = Icones.ReiniciarTTS,
                            contentDescription = "Reiniciar leitura"
                        )
                    }
                }

                FloatingActionButton(
                    onClick = {
                        when (status) {
                            TtsStatus.FALANDO -> viewModel.pausar()
                            TtsStatus.PAUSADO -> viewModel.resumir()
                            else -> viewModel.tocar(textoParaTocar)
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
                            TtsStatus.FALANDO -> Icon(
                                imageVector = Icones.PausarTTS,
                                contentDescription = "Pausar leitura"
                            )

                            TtsStatus.PAUSADO -> Icon(
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
                    visible = estaTocandoOuPausado,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    IconButton(onClick = { viewModel.parar() }) {
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
