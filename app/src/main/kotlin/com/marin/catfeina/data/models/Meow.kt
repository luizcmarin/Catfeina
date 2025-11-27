/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/models/Meow.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Modelo de domínio para a entidade Meow (frases do mascote) e seus mapeadores.
*
*/
package com.marin.catfeina.data.models

import com.marin.catfeina.sqldelight.Tbl_meow

data class Meow(
    val id: Long,
    val texto: String,
    val atualizadoem: Long
)

/**
 * Converte um objeto do banco de dados (Tbl_meow) para o modelos de domínio (Meow).
 */
fun Tbl_meow.toDomain(): Meow {
    return Meow(
        id = id,
        texto = texto,
        atualizadoem = atualizadoem
    )
}

/**
 * Converte um objeto de domínio (Meow) para a entidade do banco de dados (Tbl_meow).
 */
fun Meow.toEntity(): Tbl_meow {
    return Tbl_meow(
        id = id,
        texto = texto,
        atualizadoem = atualizadoem
    )
}
