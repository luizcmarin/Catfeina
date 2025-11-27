/*
*  Projeto: Catfeina/Catverso
*  Arquivo: core/src/main/kotlin/com/marin/core/Constantes.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Centraliza as constantes utilizadas em todo o aplicativo.
*
*/

package com.marin.core

object Constantes {

    const val ptBrLocale = "pt-BR"

    // Rotas principais de navegação
    const val ROTA_INICIO = "inicio"
    const val ROTA_CONFIGURACOES = "configuracoes"
    const val ROTA_POESIA = "poesia"
    const val ROTA_SINCRONIZACAO = "sincronizacao"
    const val ROTA_DEBUG = "debug"

    // Banco de dados
    const val CATVERSO_DATABASE_FILE_NAME = "catverso.db"
    const val CATVERSO_DATABASE_FILE_VERSAO = 1
    const val CATMONEY_DATABASE_FILE_NAME = "catmoney.db"
    const val CATMONEY_DATABASE_FILE_VERSAO = 1

    // Preferências/DataStore
    const val USER_PREFERENCES_NAME = "catfeina_preferences"
    const val PREF_TEMA = "pref_tema"
    const val PREF_FONTE = "pref_fonte"

    // Sincronização em Background
    const val SYNC_WORKER_NAME = "SyncWorker"
    const val SYNC_INTERVAL_HOURS = 240L

    // Mascote
    const val ASSET_ROMROM = "cat_caixa.json"
}
