/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tts/TtsControllerViewModel.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: ViewModel para controlar a funcionalidade de Text-to-Speech (TTS) com
 *  estados de play, pause, resume e controle de progresso.
 *
 */

package com.marin.core.tts

import android.app.Application
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.core.Constantes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TtsControllerViewModel @Inject constructor(
    application: Application
) : ViewModel(), TextToSpeech.OnInitListener {

    private val ptBrLocale = Locale.forLanguageTag(Constantes.ptBrLocale)

    // A inicialização é lazy para evitar criar o objeto TextToSpeech desnecessariamente.
    private val tts: TextToSpeech by lazy {
        _status.update { TtsStatus.INICIALIZANDO }
        TextToSpeech(application, this)
    }

    private val _status = MutableStateFlow(TtsStatus.NAO_INICIALIZADO)
    val status: StateFlow<TtsStatus> = _status.asStateFlow()

    private val _progresso = MutableStateFlow(0f)
    val progresso: StateFlow<Float> = _progresso.asStateFlow()

    private var textoCompletoAtual: String? = null
    private var proximoIndiceParaFalar: Int = 0
    private var ultimoIndiceProgresso: Int = 0

    init {
        // Dispara a inicialização lazy do TextToSpeech.
        tts
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            try {
                // Filtra as vozes disponíveis para encontrar as que são pt-BR e funcionam offline
                val vozesPtBrOffline = tts.voices?.filter {
                    it.locale == ptBrLocale && !it.isNetworkConnectionRequired
                }

                var melhorVoz: Voice? = null

                if (!vozesPtBrOffline.isNullOrEmpty()) {
                    // Em dispositivos com Android 13 (API 33) ou superior, podemos checar a qualidade
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        melhorVoz = encontrarVozDeAltaQualidade(vozesPtBrOffline)
                    }
                    // Fallback para caso nenhuma voz de alta qualidade seja encontrada
                    if (melhorVoz == null) {
                        melhorVoz = vozesPtBrOffline.firstOrNull()
                    }
                }

                if (melhorVoz != null) {
                    tts.voice = melhorVoz
                } else {
                    // Fallback: se nenhuma voz for encontrada, usa a configuração de idioma padrão.
                    tts.language = ptBrLocale
                }

            } catch (e: Exception) {
                // Em caso de qualquer exceção na busca de voz, recorre ao método seguro.
                tts.language = ptBrLocale
            }

            configurarProgressListener()
            _status.update { TtsStatus.PARADO }
        } else {
            _status.update { TtsStatus.ERRO }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun encontrarVozDeAltaQualidade(vozes: List<Voice>): Voice? {
        return try {
            // Busca vozes de alta qualidade (>=400) e pega a melhor delas
            vozes
                .filter { it.quality >= 400 } // Voice.QUALITY_HIGH
                .maxByOrNull { it.quality }
        } catch (e: Exception) {
            null
        }
    }

    private fun configurarProgressListener() {
        tts.setOnUtteranceProgressListener(object :
            UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // O status já é definido como FALANDO na função tocar().
            }

            override fun onDone(utteranceId: String?) {
                viewModelScope.launch {
                    resetarParaFinalizado()
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                onError(utteranceId, -1)
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _status.update { TtsStatus.ERRO }
            }



            override fun onRangeStart(
                utteranceId: String?,
                start: Int,
                end: Int,
                frame: Int
            ) {
                textoCompletoAtual?.let { texto ->
                    if (texto.isNotEmpty()) {
                        val progressoAtual =
                            (start + proximoIndiceParaFalar).toFloat() / texto.length
                        _progresso.value = progressoAtual.coerceIn(0f, 1f)
                    }
                }
                ultimoIndiceProgresso = proximoIndiceParaFalar + start
            }
        })
    }

    private fun podeFalar(texto: String): Boolean {
        return _status.value != TtsStatus.INICIALIZANDO &&
                _status.value != TtsStatus.ERRO &&
                texto.isNotBlank()
    }

    fun tocar(texto: String) {
        if (!podeFalar(texto)) return

        textoCompletoAtual = texto
        proximoIndiceParaFalar = 0
        ultimoIndiceProgresso = 0
        _progresso.value = 0f
        _status.update { TtsStatus.FALANDO }
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "utterance_principal")
    }

    fun pausar() {
        if (_status.value == TtsStatus.FALANDO) {
            // O método pause() do TTS é inconsistente. Usar stop() e guardar o progresso
            // para o 'resumir' é uma abordagem mais robusta.
            proximoIndiceParaFalar = ultimoIndiceProgresso
            tts.stop()
            _status.update { TtsStatus.PAUSADO }
        }
    }

    fun resumir() {
        val texto = textoCompletoAtual ?: return
        if (_status.value != TtsStatus.PAUSADO) return

        _status.update { TtsStatus.FALANDO }
        val textoRestante =
            texto.substring(proximoIndiceParaFalar.coerceIn(0, texto.length))
        tts.speak(
            textoRestante,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "utterance_principal"
        )
    }

    fun parar() {
        if (_status.value == TtsStatus.FALANDO || _status.value == TtsStatus.PAUSADO) {
            tts.stop()
            resetarParaFinalizado()
        }
    }

    fun reiniciar() {
        // Para reiniciar, simplesmente chamamos tocar com o último texto.
        textoCompletoAtual?.let { tocar(it) }
    }

    private fun resetarParaFinalizado() {
        textoCompletoAtual = null
        proximoIndiceParaFalar = 0
        ultimoIndiceProgresso = 0
        _progresso.value = 0f
        _status.update { TtsStatus.PARADO }
    }

    override fun onCleared() {
        super.onCleared()
        // Garante que o recurso TTS seja liberado quando o ViewModel for destruído.
        try {
            tts.stop()
            tts.shutdown()
        } catch (e: Exception) {
            // Ignora exceção se tts for lazy e nunca tiver sido inicializado.
        }
        _status.update { TtsStatus.NAO_INICIALIZADO }
    }
}

/**
 * Representa os possíveis estados do controlador de Text-to-Speech.
 */
enum class TtsStatus {
    NAO_INICIALIZADO, INICIALIZANDO, PARADO, FALANDO, PAUSADO, ERRO
}

