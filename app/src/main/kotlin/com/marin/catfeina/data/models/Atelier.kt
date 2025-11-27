/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/models/Atelier.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Modelo de domínio para a entidade Atelier e seus mapeadores.
*
*/
package com.marin.catfeina.data.models

import com.marin.catfeina.sqldelight.Tbl_atelier

data class Atelier(
    val id: Long,
    val titulo: String,
    val texto: String,
    val atualizadoem: Long,
    val fixada: Boolean
)

/**
 * Converte um objeto do banco de dados (Tbl_atelier) para o modelos de domínio (Atelier).
 */
fun Tbl_atelier.toDomain(): Atelier {
    return Atelier(
        id = id,
        titulo = titulo,
        texto = texto,
        atualizadoem = atualizadoem,
        fixada = fixada == 1L
    )
}

/**
 * Converte um objeto de domínio (Atelier) para a entidade do banco de dados (Tbl_atelier).
 */
fun Atelier.toEntity(): Tbl_atelier {
    return Tbl_atelier(
        id = id,
        titulo = titulo,
        texto = texto,
        atualizadoem = atualizadoem,
        fixada = if (fixada) 1L else 0L
    )
}
