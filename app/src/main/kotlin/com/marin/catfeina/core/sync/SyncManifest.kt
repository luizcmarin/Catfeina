/*
 *  Projeto: Catfeina
 *  Arquivo: SyncManifest.kt
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

package com.marin.catfeina.core.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncManifest(
    @SerialName("versao")
    val versao: Long,

    @SerialName("nota")
    val nota: String,

    @SerialName("arquivos_dados")
    val arquivosDados: List<String>,

    @SerialName("versao_app")
    val versaoApp: String? = null
)