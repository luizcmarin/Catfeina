/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/models/Historico.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Modelo de domínio para a entidade Historico e seus mapeadores.
*
*/
package com.marin.catfeina.data.models

import com.marin.catfeina.sqldelight.Tbl_historico

data class Historico(
    val id: Long,
    val nometabela: String,
    val conteudoid: Long,
    val titulo: String,
    val vistoem: Long
)

/**
 * Converte um objeto do banco de dados (Tbl_historico) para o modelos de domínio (Historico).
 */
fun Tbl_historico.toDomain(): Historico {
    return Historico(
        id = id,
        nometabela = nometabela,
        conteudoid = conteudoid,
        titulo = titulo,
        vistoem = vistoem
    )
}

/**
 * Converte um objeto de domínio (Historico) para a entidade do banco de dados (Tbl_historico).
 */
fun Historico.toEntity(): Tbl_historico {
    return Tbl_historico(
        id = id,
        nometabela = nometabela,
        conteudoid = conteudoid,
        titulo = titulo,
        vistoem = vistoem
    )
}
