/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/androidTest/kotlin/com/marin/catfeina/di/TestDatabaseModule.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Módulo Hilt de teste que substitui o DataModule de produção para prover
*  dependências para um ambiente de teste isolado.
*
*/
package com.marin.catfeina.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.sqldelight.CatfeinaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class] // Substitui o módulo de produção
)
object TestDatabaseModule {

    // region SQLDelight / Database (em memória)
    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver {
        return AndroidSqliteDriver(
            schema = CatfeinaDatabase.Schema,
            context = context,
            name = null // Banco de dados em memória para testes
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver): CatfeinaDatabase {
        // A criação do schema é gerenciada automaticamente pelo driver quando o schema
        // é passado em seu construtor. A chamada manual foi removida.
        return CatfeinaDatabase(driver)
    }

    // endregion

    // region Ktor / Network (Provisão necessária para compilação dos testes)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient {
        // Não precisamos de uma implementação complexa aqui, apenas o suficiente para
        // satisfazer o grafo de dependências do Hilt para outros repositórios.
        return HttpClient(OkHttp) {
            defaultRequest {
                url(BuildConfig.SYNC_URL)
            }
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
    // endregion

    // region Provisão de Queries e DataStore (necessário para substituir o DataModule completo)

    @Provides fun providePoesiaQueries(db: CatfeinaDatabase) = db.tbl_poesiaQueries
    @Provides fun providePoesiaNotaQueries(db: CatfeinaDatabase) = db.tbl_poesianotaQueries
    @Provides fun providePersonagemQueries(db: CatfeinaDatabase) = db.tbl_personagemQueries
    @Provides fun provideAtelierQueries(db: CatfeinaDatabase) = db.tbl_atelierQueries
    @Provides fun provideHistoricoQueries(db: CatfeinaDatabase) = db.tbl_historicoQueries
    @Provides fun provideInformativoQueries(db: CatfeinaDatabase) = db.tbl_informativoQueries
    @Provides fun provideMeowQueries(db: CatfeinaDatabase) = db.tbl_meowQueries

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        // Em um cenário de teste real, poderíamos substituir isso por um fake.
        // Por enquanto, apenas provemos uma instância para satisfazer o Hilt.
        return androidx.datastore.preferences.preferencesDataStore(name = "test_prefs").getValue(context, this::javaClass)
    }

    //endregion
}
