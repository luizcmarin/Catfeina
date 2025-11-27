/*
 *  Projeto: Catfeina / Catverso
 *  Arquivo: build.gradle.kts (Nível do Projeto)
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Este é o arquivo de build da raiz do projeto. Ele define os plugins
 *        que estarão disponíveis para todos os módulos.
 *
 */
plugins {
    // Plugins essenciais para a aplicação Android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Plugin para habilitar o Jetpack Compose
    alias(libs.plugins.kotlin.compose) apply false

    // Plugin para Injeção de Dependência com Hilt
    alias(libs.plugins.hilt.android) apply false

    // Plugin para Processamento de Anotações do Kotlin (usado por Hilt e Room/SQLDelight)
    alias(libs.plugins.ksp) apply false

    // Plugin para o SQLDelight (geração de código a partir do SQL)
    alias(libs.plugins.sqlDelight) apply false
    alias(libs.plugins.android.library) apply false
}
