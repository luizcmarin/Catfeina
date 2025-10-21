/*
 *  Projeto: Catfeina
 *  Arquivo: build.gradle.kts
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
 *  Arquivo: build.gradle.kts
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
 * //  Arquivo: build.gradle.kts
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

// =============================================================================
// Arquivo: build.gradle.kts (Nível de Projeto)
// Descrição: Configuração de build para o projeto Catfeina. Declara os
//            plugins disponíveis para os submódulos do projeto.
// =============================================================================

plugins {
    // Plugins essenciais
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Plugin para Injeção de Dependência (Hilt)
    alias(libs.plugins.hilt.android) apply false

    // Plugin para processamento de anotações (necessário para Hilt, Room, etc.)
    alias(libs.plugins.ksp) apply false

    // Plugin para serialização de objetos Kotlin para JSON
    alias(libs.plugins.kotlin.serialization) apply false

    // Plugin para gerar configurações de build de forma segura
    alias(libs.plugins.buildKonfig) apply false

    // Plugin para persistência de dados com SQL (type-safe)
    alias(libs.plugins.sqlDelight) apply false
}