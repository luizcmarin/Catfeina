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

/**
 * Representa a estrutura raiz do arquivo `manifest.json`.
 * @param modulos Lista de módulos de dados a serem sincronizados (ex: poesias, personagens).
 * @param imagens Informações sobre o pacote de imagens a ser sincronizado.
 * @param appUpdate Informações sobre uma atualização opcional do aplicativo.
 */
@Serializable
data class ManifestDto(
    @SerialName("modulos")
    val modulos: List<ModuloDto> = emptyList(),

    @SerialName("imagens")
    val imagens: ImagensDto? = null,

    @SerialName("app_update")
    val appUpdate: AppUpdateDto? = null
)

/**
 * Representa um módulo de dados individual dentro do manifesto.
 * @param nome Nome identificador do módulo (ex: "poesias").
 * @param versao A versão atual dos dados deste módulo no servidor.
 * @param arquivo O nome do arquivo JSON que contém os dados deste módulo.
 */
@Serializable
data class ModuloDto(
    @SerialName("nome")
    val nome: String,

    @SerialName("versao")
    val versao: Int,

    @SerialName("arquivo")
    val arquivo: String
)

/**
 * Representa as informações sobre o pacote de imagens de fundo.
 * @param versao A versão atual do pacote de imagens no servidor.
 * @param arquivo O nome do arquivo ZIP que contém as imagens.
 */
@Serializable
data class ImagensDto(
    @SerialName("versao")
    val versao: Int,

    @SerialName("arquivo")
    val arquivo: String
)

/**
 * Representa os detalhes de uma atualização disponível para o aplicativo.
 * @param versionCode O `versionCode` da nova versão.
 * @param versionName O `versionName` da nova versão.
 * @param changelog As notas de lançamento ou o que há de novo.
 * @param url O link para baixar a nova versão do APK.
 */
@Serializable
data class AppUpdateDto(
    @SerialName("versionCode")
    val versionCode: Int,

    @SerialName("versionName")
    val versionName: String,

    @SerialName("changelog")
    val changelog: String,

    @SerialName("url")
    val url: String
)
