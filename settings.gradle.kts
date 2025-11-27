/*
 *  Projeto: Catfeina
 *  Arquivo: settings.gradle.kts
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Configuração do Gradle para o projeto Catfeina.
 *
 */
@file:Suppress("UnstableApiUsage")

include(":core")


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Ativa o acesso a projetos de forma type-safe, recomendado para projetos multi-módulo.
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Catfeina"
include(":app")
