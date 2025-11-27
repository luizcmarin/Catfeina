/*
 *  Projeto: Catfeina
 *  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/sync/SyncDataModels.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Modelos de dados (DTOs) para desserializar os dados vindos da rede.
 *  Estes modelos devem espelhar a estrutura do JSON recebido.
 *
 */
package com.marin.catfeina.data.sync

import com.marin.catfeina.data.models.Atelier
import com.marin.catfeina.data.models.Informativo
import com.marin.catfeina.data.models.Meow
import com.marin.catfeina.data.models.Personagem
import com.marin.catfeina.data.models.Poesia
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO para um registro de poesia vindo do JSON da rede.
 */
@Serializable
data class PoesiaSync(
    @SerialName("id") val id: Long,
    @SerialName("titulo") val titulo: String,
    @SerialName("textobase") val textobase: String,
    @SerialName("texto") val texto: String,
    @SerialName("textofinal") val textofinal: String? = null,
    @SerialName("imagem") val imagem: String? = null,
    @SerialName("autor") val autor: String? = null,
    @SerialName("nota") val nota: String? = null,
    @SerialName("anterior") val anterior: Long? = null,
    @SerialName("proximo") val proximo: Long? = null,
    @SerialName("atualizadoem") val atualizadoem: Long
)

/**
 * DTO para um registro de personagem vindo do JSON da rede.
 */
@Serializable
data class PersonagemSync(
    @SerialName("id") val id: Long,
    @SerialName("nome") val nome: String,
    @SerialName("biografia") val biografia: String,
    @SerialName("imagem") val imagem: String? = null,
    @SerialName("atualizadoem") val atualizadoem: Long
)

/**
 * DTO para um registro de informativo vindo do JSON da rede.
 */
@Serializable
data class InformativoSync(
    @SerialName("id") val id: Long,
    @SerialName("chave") val chave: String,
    @SerialName("titulo") val titulo: String,
    @SerialName("conteudo") val conteudo: String,
    @SerialName("imagem") val imagem: String? = null,
    @SerialName("atualizadoem") val atualizadoem: Long
)

/**
 * DTO para uma nota do atelier vinda do JSON da rede.
 */
@Serializable
data class AtelierSync(
    @SerialName("id") val id: Long,
    @SerialName("titulo") val titulo: String,
    @SerialName("texto") val texto: String,
    @SerialName("atualizadoem") val atualizadoem: Long,
    @SerialName("fixada") val fixada: Boolean
)

/**
 * DTO para um registro de meow vindo do JSON da rede.
 */
@Serializable
data class MeowSync(
    @SerialName("id") val id: Long,
    @SerialName("texto") val texto: String,
    @SerialName("atualizadoem") val atualizadoem: Long
)

// --- Mapeadores de DTO de Sincronização para Modelo de Domínio ---

fun PoesiaSync.toDomain() = Poesia(
    id = id,
    titulo = titulo,
    textobase = textobase,
    texto = texto,
    textofinal = textofinal,
    imagem = imagem,
    autor = autor,
    nota = nota,
    anterior = anterior,
    proximo = proximo,
    atualizadoem = atualizadoem,
    // Campos locais são inicializados com valores padrão
    favorita = false,
    lida = false,
    dataleitura = null,
    notausuario = null
)

fun PersonagemSync.toDomain() = Personagem(
    id = id,
    nome = nome,
    biografia = biografia,
    imagem = imagem,
    atualizadoem = atualizadoem
)

fun InformativoSync.toDomain() = Informativo(
    id = id,
    chave = chave,
    titulo = titulo,
    conteudo = conteudo,
    imagem = imagem,
    atualizadoem = atualizadoem
)

fun AtelierSync.toDomain() = Atelier(
    id = id,
    titulo = titulo,
    texto = texto,
    atualizadoem = atualizadoem,
    fixada = fixada
)

fun MeowSync.toDomain() = Meow(
    id = id,
    texto = texto,
    atualizadoem = atualizadoem
)
