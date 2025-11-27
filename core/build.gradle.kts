/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/build.gradle.kts
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Configuração Gradle para o módulo :core, responsável pelo código compartilhado.
 *
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.marin.core"
    compileSdk = libs.versions.compileSdk.get().toInt()

    // Define a dimensão do flavor. DEVE ser a mesma do módulo :app.
    flavorDimensions += "appFlavor"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    // Define os product flavors para este módulo de biblioteca.
    // Eles correspondem aos flavors do módulo :app.
    productFlavors {
        create("catverso") {
            dimension = "appFlavor"
            // Define um campo no BuildConfig com a TAG de log para este flavor.
            buildConfigField("String", "LOG_TAG", "\"CatversoApp\"")
        }
        create("catmoney") {
            dimension = "appFlavor"
            // Define um campo no BuildConfig com a TAG de log para este flavor.
            buildConfigField("String", "LOG_TAG", "\"CatmoneyApp\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.jvmTarget.get()}")
        targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.jvmTarget.get()}")
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.valueOf("JVM_${libs.versions.jvmTarget.get()}"))
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Dependência para animações Lottie, AnimatedVectorDrawable
    implementation(libs.lottie.compose)
    implementation(libs.androidx.compose.animation.graphics)

    // Dependências adicionadas para JSON e Markdown
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.commonmark)
    implementation(libs.androidx.animation.graphics)

    // Dependências para ViewModel e Hilt
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Dependência para integração do Hilt com Navegação do Compose (ADICIONADA)
    implementation(libs.androidx.hilt.navigation.compose)

}
