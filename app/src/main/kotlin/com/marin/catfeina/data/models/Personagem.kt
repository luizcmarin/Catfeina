/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/models/Personagem.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Modelo de domínio para a entidade Personagem e seus mapeadores.
*
*/
package com.marin.catfeina.data.models

import com.marin.catfeina.sqldelight.Tbl_personagem

data class Personagem(
    val id: Long,
    val nome: String,
    val biografia: String,
    val imagem: String?,
    val atualizadoem: Long
)

fun Tbl_personagem.toDomain(): Personagem {
    return Personagem(
        id = id,
        nome = nome,
        biografia = biografia,
        imagem = imagem,
        atualizadoem = atualizadoem
    )
}

/**
 * Converte um objeto de domínio (Personagem) para a entidade do banco de dados (Tbl_personagem).
 */
fun Personagem.toEntity(): Tbl_personagem {
    return Tbl_personagem(
        id = id,
        nome = nome,
        biografia = biografia,
        imagem = imagem,
        atualizadoem = atualizadoem
    )
}
