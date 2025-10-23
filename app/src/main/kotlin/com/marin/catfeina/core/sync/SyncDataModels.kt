/*
 *  Projeto: Catfeina
 *  Arquivo: SyncDataModels.kt
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
 *
 *  Projeto: Catfeina
 *  Arquivo: SyncDataModels.kt
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: SyncDataModels.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: SyncDataModels.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
 *
 */

package com.marin.catfeina.core.sync

import com.marin.catfeina.core.utils.CategoriaPoesiaEnum
import kotlinx.serialization.Serializable

/**
 * Representa a estrutura de um único registro de poesia no arquivo poesias.json.
 * Os nomes dos campos devem corresponder exatamente aos do JSON.
 */
@Serializable
data class PoesiaSync(
    val id: Long,
    val categoria: CategoriaPoesiaEnum,
    val titulo: String,
    val textoBase: String,
    val texto: String,
    val textoFinal: String? = null,
    val imagem: String? = null,
    val autor: String? = null,
    val nota: String? = null,
    val campoUrl: String? = null,
    val dataCriacao: Long
)

/**
 * Representa o estado de uma poesia para um usuário. Pode ser sincronizado opcionalmente.
 */
@Serializable
data class PoesiaNotaSync(
    val poesiaId: Long,
    val ehFavorita: Boolean = false,
    val foiLida: Boolean = false,
    val dataFavoritado: Long? = null,
    val dataLeitura: Long? = null,
    val notaUsuario: String? = null
)

/**
 * Representa a estrutura de um personagem no arquivo personagens.json.
 */
@Serializable
data class PersonagemSync(
    val id: Long,
    val nome: String,
    val descricao: String,
    val imagem_url: String? = null, // Corresponde à coluna do DB
    val dataCriacao: Long
)

/**
 * Representa a estrutura de um informativo no arquivo informativos.json.
 */
@Serializable
data class InformativoSync(
    val chave: String,
    val titulo: String,
    val conteudo: String,
    val dataAtualizacao: Long
)
