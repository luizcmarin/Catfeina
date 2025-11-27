/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/models/Poesia.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Modelo de domínio para a entidade Poesia e seus mapeadores.
*
*/
package com.marin.catfeina.data.models

import com.marin.catfeina.sqldelight.BuscarPoesias
import com.marin.catfeina.sqldelight.GetPoesia
import com.marin.catfeina.sqldelight.GetPoesiaAleatoria
import com.marin.catfeina.sqldelight.GetPoesias
import com.marin.catfeina.sqldelight.GetPoesiasFavoritas
import com.marin.catfeina.sqldelight.Tbl_poesia
import com.marin.catfeina.sqldelight.Tbl_poesianota

data class Poesia(
    val id: Long,
    val titulo: String,
    val texto: String,
    val imagem: String?,
    val autor: String?,
    val nota: String?,
    val textobase: String,
    val textofinal: String?,
    val anterior: Long?,
    val proximo: Long?,
    val atualizadoem: Long,
    val favorita: Boolean,
    val lida: Boolean,
    val dataleitura: Long?,
    val notausuario: String?
)

// Mapeadores do Banco de Dados para o Domínio

fun GetPoesias.toDomain(): Poesia {
    return Poesia(
        id = id,
        titulo = titulo,
        texto = texto,
        imagem = imagem,
        autor = autor,
        nota = nota,
        textobase = textobase,
        textofinal = textofinal,
        anterior = anterior,
        proximo = proximo,
        atualizadoem = atualizadoem,
        favorita = favorita == 1L,
        lida = lida == 1L,
        dataleitura = dataleitura,
        notausuario = notausuario
    )
}

fun GetPoesia.toDomain(): Poesia {
    return Poesia(
        id = id,
        titulo = titulo,
        texto = texto,
        imagem = imagem,
        autor = autor,
        nota = nota,
        textobase = textobase,
        textofinal = textofinal,
        anterior = anterior,
        proximo = proximo,
        atualizadoem = atualizadoem,
        favorita = favorita == 1L,
        lida = lida == 1L,
        dataleitura = dataleitura,
        notausuario = notausuario
    )
}

fun GetPoesiaAleatoria.toDomain(): Poesia {
    return Poesia(
        id = id,
        titulo = titulo,
        texto = texto,
        imagem = imagem,
        autor = autor,
        nota = nota,
        textobase = textobase,
        textofinal = textofinal,
        anterior = anterior,
        proximo = proximo,
        atualizadoem = atualizadoem,
        favorita = favorita == 1L,
        lida = lida == 1L,
        dataleitura = dataleitura,
        notausuario = notausuario
    )
}

fun GetPoesiasFavoritas.toDomain(): Poesia {
    return Poesia(
        id = id,
        titulo = titulo,
        texto = texto,
        imagem = imagem,
        autor = autor,
        nota = nota,
        textobase = textobase,
        textofinal = textofinal,
        anterior = anterior,
        proximo = proximo,
        atualizadoem = atualizadoem,
        favorita = favorita == 1L,
        lida = lida == 1L,
        dataleitura = dataleitura,
        notausuario = notausuario
    )
}

fun BuscarPoesias.toDomain(): Poesia {
    return Poesia(
        id = id,
        titulo = titulo,
        texto = texto,
        imagem = imagem,
        autor = autor,
        nota = nota,
        textobase = textobase,
        textofinal = textofinal,
        anterior = anterior,
        proximo = proximo,
        atualizadoem = atualizadoem,
        favorita = favorita == 1L,
        lida = lida == 1L,
        dataleitura = dataleitura,
        notausuario = notausuario
    )
}

// Mapeadores do Domínio para o Banco de Dados

fun Poesia.toPoesiaEntity(): Tbl_poesia {
    return Tbl_poesia(
        id = id,
        titulo = titulo,
        texto = texto,
        imagem = imagem,
        autor = autor,
        nota = nota,
        textobase = textobase,
        textofinal = textofinal,
        anterior = anterior,
        proximo = proximo,
        atualizadoem = atualizadoem
    )
}

fun Poesia.toPoesiaNotaEntity(): Tbl_poesianota {
    return Tbl_poesianota(
        poesiaid = id,
        favorita = if (favorita) 1L else 0L,
        lida = if (lida) 1L else 0L,
        dataleitura = dataleitura,
        notausuario = notausuario
    )
}
