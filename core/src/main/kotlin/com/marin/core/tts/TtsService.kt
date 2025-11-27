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
import com.marin.core.Constantes
import com.marin.core.util.CatLog
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsService @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var estaInicializado = false

    init {
        try {
            // Inicializa o motor TTS. O resultado será entregue no callback onInit.
            tts = TextToSpeech(context, this)
        } catch (e: Exception) {
            CatLog.e("Falha ao instanciar o TextToSpeech", e)
            estaInicializado = false
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Tenta definir o idioma para Português do Brasil, usando a constante centralizada.
            val resultado = tts?.setLanguage(Locale.forLanguageTag(Constantes.ptBrLocale))

            // Verifica se o idioma é suportado.
            if (resultado == TextToSpeech.LANG_MISSING_DATA || resultado == TextToSpeech.LANG_NOT_SUPPORTED) {
                CatLog.e("TTS: A língua portuguesa (pt-BR) não é suportada neste dispositivo.")
                estaInicializado = false
            } else {
                CatLog.d("Serviço de TTS inicializado com sucesso.")
                estaInicializado = true
            }
        } else {
            CatLog.e("TTS: Falha na inicialização. Código de status: $status")
            estaInicializado = false
        }
    }

    /**
     * Lê o texto fornecido em voz alta.
     * @param texto O conteúdo a ser falado.
     */
    fun falar(texto: String) {
        if (estaInicializado) {
            tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            CatLog.w("TTS não está inicializado. Não é possível falar o texto solicitado.")
        }
    }

    /**
     * Interrompe a fala atual, se estiver ocorrendo.
     */
    fun parar() {
        if (tts?.isSpeaking == true) {
            tts?.stop()
        }
    }

    /**
     * Libera os recursos do serviço de TTS. Deve ser chamado quando o serviço não for mais necessário.
     */
    fun finalizar() {
        parar()
        tts?.shutdown()
        tts = null
        estaInicializado = false
        CatLog.d("Serviço de TTS finalizado.")
    }
}
