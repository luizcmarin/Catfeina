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

// =============================================================================
// Arquivo: app/build.gradle.kts
// Descrição: Configuração otimizada do Gradle para o módulo :app do Catfeina.
// =============================================================================

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
    id("com.google.android.gms.oss-licenses-plugin")

}

android {
    namespace = "com.marin.catfeina"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.marin.catfeina"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()


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
        // Cria um campo String com a data e hora do build
        val buildTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        buildConfigField("String", "BUILD_TIME", "\"${buildTime}\"")


        // Configurações de Sincronização
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

    buildTypes {
        debug {
            isCrunchPngs = false // Boa prática para builds mais rápidos em debug
        }
        release {
            isMinifyEnabled = false // HABILITE O MINIFY PARA RELEASE
            isShrinkResources = false // HABILITE PARA REMOVER RECURSOS NÃO USADOS
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Desabilita a variante 'release' para acelerar a sincronização e compilação
    // durante o desenvolvimento.
    androidComponents {
        beforeVariants(selector().withBuildType("release")) { variantBuilder ->
            variantBuilder.enable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
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

dependencies {
    // ---------- BUNDLES MODERNIZADOS ----------
    implementation(libs.bundles.core)
    implementation(libs.bundles.ui)
    implementation(libs.bundles.data)
    implementation(libs.bundles.network)
    implementation(libs.bundles.navigation)

    // Plataforma do Compose (BOM - Bill of Materials)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.paging.common)
    implementation(libs.google.gms.oss.licenses.plugin)
    debugImplementation(libs.androidx.compose.ui.tooling) // ui-tooling apenas para debug

    // ---------- DEPENDÊNCIAS INDIVIDUAIS DE INFRAESTRUTURA ----------

    // Animações

    // ViewModel para Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Injeção de Dependência (Hilt)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

sqldelight {
    databases {
        create("CatfeinaDatabase") {
            packageName.set("com.marin.catfeina.sqldelight")
            // Desabilita a verificação de migração para evitar erros de permissão no Windows
            // e por não termos um banco de dados legado para migrar.
            verifyMigrations.set(false)
        }
    }
}

tasks.matching { it.name.startsWith("ksp") }.configureEach {
    if (name.contains("Debug")) {
        dependsOn("generateDebugCatfeinaDatabaseInterface")
    }
    if (name.contains("Release")) {
        dependsOn("generateReleaseCatfeinaDatabaseInterface")
    }
}
tasks.matching { it.name.endsWith("OssLicensesCleanUp") }.configureEach {
    val buildType = name.removeSuffix("OssLicensesCleanUp")
    val dependencyTaskName = "${buildType}OssDependencyTask"
    dependsOn(dependencyTaskName)
}