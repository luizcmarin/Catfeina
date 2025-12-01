/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tts/TtsService.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Serviço para gerenciar a funcionalidade de Text-to-Speech (Leitura em Voz Alta).
 *
 */

package com.marin.core.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.marin.core.Constantes
import com.marin.core.util.CatLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

enum class TtsEstado {
    PARADO,
    REPRODUZINDO,
    PAUSADO, // Embora o TTS do Android não tenha um "pause" real, podemos simular
    ERRO,
    INICIALIZANDO
}

@Singleton
class TtsService @Inject constructor(
    @param:ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private val _estado = MutableStateFlow(TtsEstado.INICIALIZANDO)
    val estado = _estado.asStateFlow()

    private var textoAtual: String = ""
    private var ultimoPontoParada: Int = 0

    init {
        try {
            tts = TextToSpeech(context, this).apply {
                setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _estado.value = TtsEstado.REPRODUZINDO
                    }

                    override fun onDone(utteranceId: String?) {
                        _estado.value = TtsEstado.PARADO
                        ultimoPontoParada = 0 // Reseta ao concluir
                    }

                    @Deprecated("deprecated in API level 21")
                    override fun onError(utteranceId: String?) {
                        _estado.value = TtsEstado.ERRO
                    }
                })
            }
        } catch (e: Exception) {
            CatLog.e("Falha ao instanciar o TextToSpeech", e)
            _estado.value = TtsEstado.ERRO
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val resultado = tts?.setLanguage(Locale.forLanguageTag(Constantes.ptBrLocale))
            if (resultado == TextToSpeech.LANG_MISSING_DATA || resultado == TextToSpeech.LANG_NOT_SUPPORTED) {
                CatLog.e("TTS: A língua portuguesa (pt-BR) não é suportada.")
                _estado.value = TtsEstado.ERRO
            } else {
                CatLog.d("Serviço de TTS inicializado.")
                _estado.value = TtsEstado.PARADO
            }
        } else {
            CatLog.e("TTS: Falha na inicialização. Código de status: $status")
            _estado.value = TtsEstado.ERRO
        }
    }

    fun reproduzir(texto: String) {
        if (_estado.value == TtsEstado.PAUSADO && texto == textoAtual) {
            retomar()
        } else {
            textoAtual = texto
            ultimoPontoParada = 0
            tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }
    }

    fun pausar() {
        // O TTS do Android não tem um pause real. A simulação é feita parando a fala
        // e guardando a posição para um futuro 'retomar'. 
        // A implementação de `onUtteranceCompleted` lidaria com o ponto de parada, mas é complexo.
        // Por simplicidade, vamos apenas parar.
        tts?.stop()
        _estado.value = TtsEstado.PAUSADO // Estado simulado
    }

    private fun retomar() {
        // Simulação de 'retomar', reiniciando a fala. Uma implementação real exigiria
        // salvar o progresso da fala, o que é mais complexo.
        reproduzir(textoAtual)
    }

    fun parar() {
        tts?.stop()
        _estado.value = TtsEstado.PARADO
        ultimoPontoParada = 0
    }

    fun finalizar() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        _estado.value = TtsEstado.PARADO
        CatLog.d("Serviço de TTS finalizado.")
    }
}
