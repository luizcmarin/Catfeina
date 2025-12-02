/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/sync/ManifestDto.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: DTOs para a desserialização do arquivo de manifesto (manifest.json).
*
*/
package com.marin.catfeina.data.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ManifestDto(
    @SerialName("modulos")
    val modulos: List<ModuloDto> = emptyList(),

    @SerialName("imagens")
    val imagens: ImagensDto? = null,

    @SerialName("app_update")
    val appUpdate: AppUpdateDto? = null
)

@Serializable
data class ModuloDto(
    @SerialName("nome")
    val nome: String,

    @SerialName("versao")
    val versao: Int,

    @SerialName("arquivo")
    val arquivo: String
)

@Serializable
data class ImagensDto(
    @SerialName("versao")
    val versao: Int,

    @SerialName("arquivo")
    val arquivo: String
)

@Serializable
data class AppUpdateDto(
    @SerialName("versionCode")
    val versionCode: Int? = null,

    @SerialName("versionName")
    val versionName: String? = null,

    @SerialName("changelog")
    val changelog: String? = null,

    @SerialName("url")
    val url: String? = null
)
