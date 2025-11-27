/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/models/Informativo.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Modelo de domínio para a entidade Informativo e seus mapeadores.
*
*/
package com.marin.catfeina.data.models

import com.marin.catfeina.sqldelight.Tbl_informativo

data class Informativo(
    val id: Long,
    val chave: String,
    val titulo: String,
    val conteudo: String,
    val imagem: String?,
    val atualizadoem: Long
)

fun Tbl_informativo.toDomain(): Informativo {
    return Informativo(
        id = id,
        chave = chave,
        titulo = titulo,
        conteudo = conteudo,
        imagem = imagem,
        atualizadoem = atualizadoem
    )
}

/**
 * Converte um objeto de domínio (Informativo) para a entidade do banco de dados (Tbl_informativo).
 */
fun Informativo.toEntity(): Tbl_informativo {
    return Tbl_informativo(
        id = id,
        chave = chave,
        titulo = titulo,
        conteudo = conteudo,
        imagem = imagem,
        atualizadoem = atualizadoem
    )
}
