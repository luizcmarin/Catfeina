/*
*  Projeto: Catfeina/Catverso
*  Arquivo: core/src/main/kotlin/com/marin/core/util/SyncState.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Sealed class que representa os possíveis estados de uma operação de sincronização.
*
*/
package com.marin.core.util

sealed class SyncState {
    data object Ocioso : SyncState()
    data class Executando(val mensagem: String) : SyncState()
    data class Sucesso(val mensagem: String) : SyncState()
    data class Falha(val erro: String) : SyncState()
}
