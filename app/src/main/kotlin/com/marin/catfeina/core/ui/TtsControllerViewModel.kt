/*
 *  Projeto: Catfeina
 *  Arquivo: TtsControllerViewModel.kt
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
 *  Arquivo: TtsControllerViewModel.kt
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

import android.app.Application
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
class TtsControllerViewModel @Inject constructor(
    application: Application
) : ViewModel(), TextToSpeech.OnInitListener {

    private val ptBrLocale = Locale.forLanguageTag("pt-BR")

    // A inicialização é lazy para evitar criar o objeto TextToSpeech desnecessariamente.
    // O acesso no bloco `init` garante que a inicialização comece quando o ViewModel for criado.
    private val tts: TextToSpeech by lazy {
        _status.update { TtsStatus.INITIALIZING }
        TextToSpeech(application, this)
    }

    private val _status = MutableStateFlow(TtsStatus.UNINITIALIZED)
    val ttsStatus: StateFlow<TtsStatus> = _status.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

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
                        melhorVoz = findHighQualityVoice(vozesPtBrOffline)
                    }

                    if (melhorVoz == null) {
                        melhorVoz = vozesPtBrOffline.firstOrNull()
                    }
                }

                if (melhorVoz != null) {
                    tts.voice = melhorVoz
                    Timber.d("Voz selecionada: ${melhorVoz.name}")
                } else {
                    // Fallback: se nenhuma voz for encontrada, usa a configuração de idioma padrão.
                    tts.language = ptBrLocale
                    Timber.w("Nenhuma voz local para pt-BR encontrada. Usando configuração de idioma padrão.")
                }

            } catch (e: Exception) {
                // Fallback em caso de erro ao listar as vozes
                Timber.e(
                    e,
                    "Erro ao obter vozes do TTS. Usando a configuração de idioma padrão."
                )
                tts.language = ptBrLocale
            }

            setupProgressListener()
            _status.update { TtsStatus.STOPPED }
        } else {
            _status.update { TtsStatus.ERROR }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun findHighQualityVoice(voices: List<Voice>): Voice? {
        return try {
            voices
                .filter { it.quality >= 400 }
                .maxByOrNull { it.quality }
        } catch (e: Exception) {
            Timber.w(e, "Não foi possível avaliar a qualidade das vozes.")
            null
        }
    }

    private fun setupProgressListener() {
        tts.setOnUtteranceProgressListener(object :
            UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // O status já é definido como SPEAKING na função play().
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
                _status.update { TtsStatus.ERROR }
            }

            override fun onRangeStart(
                utteranceId: String?,
                start: Int,
                end: Int,
                frame: Int
            ) {
                textoCompletoAtual?.let { texto ->
                    if (texto.isNotEmpty()) {
                        val currentProgress =
                            (start + proximoIndiceParaFalar).toFloat() / texto.length
                        _progress.value = currentProgress.coerceIn(0f, 1f)
                    }
                }
                ultimoIndiceProgresso = proximoIndiceParaFalar + start
            }
        })
    }

    // Não continua se o TTS estiver em um estado inválido ou o texto for vazio.
    private fun podeFalar(texto: String): Boolean {
        return _status.value != TtsStatus.INITIALIZING &&
                _status.value != TtsStatus.ERROR &&
                texto.isNotBlank()
    }

    fun play(texto: String) {
        if (!podeFalar(texto)) return

        textoCompletoAtual = texto
        proximoIndiceParaFalar = 0
        ultimoIndiceProgresso = 0
        _progress.value = 0f
        _status.update { TtsStatus.SPEAKING }
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "utterance_principal")
    }

    fun pause() {
        if (_status.value == TtsStatus.SPEAKING) {
            // O método pause() do TTS é inconsistente. Usar stop() e guardar o progresso
            // para o '''resume''' é uma abordagem mais robusta.
            proximoIndiceParaFalar = ultimoIndiceProgresso
            tts.stop()
            _status.update { TtsStatus.PAUSED }
        }
    }

    fun resume() {
        val texto = textoCompletoAtual ?: return
        if (_status.value != TtsStatus.PAUSED) return

        _status.update { TtsStatus.SPEAKING }
        val textoRestante =
            texto.substring(proximoIndiceParaFalar.coerceIn(0, texto.length))
        tts.speak(
            textoRestante,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "utterance_principal"
        )
    }

    fun stop() {
        if (_status.value == TtsStatus.SPEAKING || _status.value == TtsStatus.PAUSED) {
            tts.stop()
            resetarParaFinalizado()
        }
    }

    fun restart() {
        // Para reiniciar, simplesmente chamamos play com o último texto.
        textoCompletoAtual?.let { play(it) }
    }

    private fun resetarParaFinalizado() {
        textoCompletoAtual = null
        proximoIndiceParaFalar = 0
        ultimoIndiceProgresso = 0
        _progress.value = 0f
        _status.update { TtsStatus.STOPPED }
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
        _status.update { TtsStatus.UNINITIALIZED }
    }
}

enum class TtsStatus {
    UNINITIALIZED, INITIALIZING, STOPPED, SPEAKING, PAUSED, ERROR
}
