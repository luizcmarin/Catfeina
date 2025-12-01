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

import com.marin.catfeina.sqldelight.PoesiaView
import com.marin.catfeina.sqldelight.Tbl_poesia
import com.marin.catfeina.sqldelight.Tbl_poesianota

/**
 * Representa uma poesia completa, combinando os dados da obra e as interações do usuário.
 * Esta é a classe principal usada em toda a camada de UI.
 */
data class Poesia(
    val id: Long,
    val texto: String, // Conteúdo completo em Markdown
    val anterior: Long?,
    val proximo: Long?,
    val atualizadoem: Long,
    val favorita: Boolean,
    val lida: Boolean,
    val dataleitura: Long?,
    val notausuario: String?
)

// --- FUNÇÕES DE MAPEAMENTO ---

/**
 * Converte um objeto da View do banco de dados para o modelo de domínio `Poesia`.
 * Esta é a principal função de mapeamento para leitura de dados.
 */
fun PoesiaView.toDomain(): Poesia {
    return Poesia(
        id = id,
        texto = texto,
        anterior = anterior,
        proximo = proximo,
        atualizadoem = atualizadoem,
        favorita = favorita == 1L, // Converte de Long (0 ou 1) para Boolean
        lida = lida == 1L,         // Converte de Long (0 ou 1) para Boolean
        dataleitura = dataleitura,
        notausuario = notausuario
    )
}

/**
 * Converte um modelo de domínio `Poesia` para uma entidade da tabela `tbl_poesia`.
 * Usado ao inserir ou atualizar os dados imutáveis da obra.
 */
fun Poesia.toPoesiaEntity(): Tbl_poesia {
    return Tbl_poesia(
        id = id,
        texto = texto,
        anterior = anterior,
        proximo = proximo,
        atualizadoem = atualizadoem
    )
}

/**
 * Converte um modelo de domínio `Poesia` para uma entidade da tabela `tbl_poesianota`.
 * Usado ao salvar as interações do usuário (favorito, lido, nota).
 */
fun Poesia.toPoesiaNotaEntity(): Tbl_poesianota {
    return Tbl_poesianota(
        poesiaid = id,
        favorita = if (favorita) 1L else 0L, // Converte de Boolean para Long
        lida = if (lida) 1L else 0L,         // Converte de Boolean para Long
        dataleitura = dataleitura,
        notausuario = notausuario
    )
}
