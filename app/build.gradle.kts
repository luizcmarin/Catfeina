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
 *  Nota: Configuração otimizada do Gradle para o módulo :app
 *
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqlDelight)

}

android {
    namespace = "com.marin.catfeina"
    compileSdk = libs.versions.compileSdk.get().toInt()

    // Define a dimensão de sabores para o projeto.
    flavorDimensions += "appFlavor"

    defaultConfig {
        applicationId = "com.marin.catfeina"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        // Define o Hilt Test Runner para testes instrumentados
        testInstrumentationRunner = "com.marin.catfeina.HiltTestRunner"

        buildConfigField(
            "int",
            "VERSION_CODE",
            libs.versions.versionCode.get()
        )
        buildConfigField(
            "String",
            "VERSION_NAME",
            "\"${libs.versions.versionName.get()}\""
        )

        // Expõe a data/hora do build para o código-fonte via BuildConfig.
        val buildTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        buildConfigField("String", "BUILD_TIME", "\"${buildTime}\"")

        // Expõe as configurações de sincronização para o código-fonte.
        buildConfigField(
            "String",
            "SYNC_URL",
            "\"${libs.versions.syncUrl.get()}\""
        )
        buildConfigField(
            "String",
            "SYNC_ZIP_FILE_NAME",
            "\"${libs.versions.syncZipFileName.get()}\""
        )
        buildConfigField(
            "String",
            "SYNC_MANIFEST_NAME",
            "\"${libs.versions.syncManifestName.get()}\""
        )
        buildConfigField(
            "String",
            "SYNC_IMAGE_FOLDER_NAME",
            "\"${libs.versions.syncImageFolderName.get()}\""
        )
        buildConfigField(
            "String",
            "SYNC_DATA_FOLDER_NAME",
            "\"${libs.versions.syncDataFolderName.get()}\""
        )
        buildConfigField(
            "String",
            "SYNC_APP_FOLDER_NAME",
            "\"${libs.versions.syncAppFolderName.get()}\""
        )
    }

    // Define os diferentes "sabores" do aplicativo.
    productFlavors {
        create("catverso") {
            dimension = "appFlavor"
            applicationIdSuffix = ".catverso"
            versionNameSuffix = "-catverso"
        }
        create("catmoney") {
            dimension = "appFlavor"
            applicationIdSuffix = ".catmoney"
            versionNameSuffix = "-catmoney"
        }
    }

    buildTypes {
        debug {
            // Otimização para builds de debug mais rápidos.
            isCrunchPngs = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Acelera o desenvolvimento ao desabilitar variantes não utilizadas no dia a dia.
    // Recomenda-se comentar esta seção ao gerar um build de release.
    /*
    androidComponents {
        beforeVariants(selector().withBuildType("release")) { variantBuilder ->
            variantBuilder.enable = false
        }
    }
    */

    compileOptions {
        // Define a compatibilidade do Java para o nível 21.
        sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.jvmTarget.get()}")
        targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.jvmTarget.get()}")
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-Xreturn-value-checker=full")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true // Necessário para usar os campos `buildConfigField`.
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
        jniLibs {
            useLegacyPackaging = true
            excludes += "lib/armeabi/libjnidispatch.so"
            jniLibs.keepDebugSymbols.addAll(
                listOf(
                    "**/*librive-android.so",
                    "**/*libc++_shared.so",
                    "**/*libdatastore_shared_counter.so",
                    "**/*libandroidx.graphics.path.so"
                )
            )
        }
    }

    lint {
        enable.add("DuplicateStrings")
        sarifReport = true
        abortOnError = false
        checkReleaseBuilds = true
        warningsAsErrors = false
    }
}

// Seção de Dependências usando os bundles e o Version Catalog.
dependencies {
    // Plataforma do Compose (BOM - Bill of Materials). Garante versões consistentes.
    implementation(platform(libs.androidx.compose.bom))

    implementation(project(":core"))

    // Dependências do Core do Android (ktx, lifecycle, etc.)
    implementation(libs.bundles.core)
    implementation(libs.bundles.ui)
    implementation(libs.androidx.paging.compose)
    implementation(libs.sketch.compose)

    // Navegação
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // WorkManager para tarefas em background
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    // Ferramentas de UI para debug (inspeção de layouts, etc.). Não incluído em builds de release.
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview) // Para previews no Android Studio

    // Demais bundles do projeto
    implementation(libs.bundles.data)
    implementation(libs.bundles.sqldelight)
    implementation(libs.bundles.network)

    // Injeção de Dependência (Hilt) - Declaração e processador de anotações.
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)

    // Testes
    testImplementation(libs.bundles.test.unit)
    androidTestImplementation(libs.bundles.test.instrumentation)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Testes com Hilt
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

}

sqldelight {
    dependencies {
        implementation(libs.sqlDelight.driver.sqlite)
    }
    databases {
        create("CatfeinaDatabase") {
            packageName.set("com.marin.catfeina.sqldelight")
            // Desabilita a verificação de migração para evitar erros de permissão no Windows
            // e por não termos um banco de dados legado para migrar.
            verifyMigrations.set(false)
        }
    }
}
