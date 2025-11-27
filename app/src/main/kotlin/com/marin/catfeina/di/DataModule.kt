/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/di/DataModule.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Módulo Hilt unificado para prover todas as fontes de dados da aplicação,
*  incluindo rede (Ktor), banco de dados (SQLDelight) e preferências (DataStore).
*
*/
package com.marin.catfeina.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.sqldelight.CatfeinaDatabase
import com.marin.core.Constantes
import com.marin.core.util.CatLog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton

// Extensão para acesso conveniente ao DataStore a partir do Contexto.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constantes.USER_PREFERENCES_NAME
)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseName

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    //region Ktor / Network
    @Provides
    @Singleton
    fun provideJson(): Json = Json { // Adicionado provider para o Json
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url(BuildConfig.SYNC_URL)
                header("User-Agent", "Catfeina/${BuildConfig.VERSION_NAME}")
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 15000
            }
            install(Logging) {
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) {
                        CatLog.d("Ktor: $message")
                    }
                }
            }
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
    //endregion

    //region SQLDelight / Database
    @Provides
    @Singleton
    fun provideSqlDriver(
        @ApplicationContext context: Context,
        @DatabaseName dbName: String
    ): SqlDriver {
        return AndroidSqliteDriver(
            schema = CatfeinaDatabase.Schema,
            context = context,
            name = dbName
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver): CatfeinaDatabase {
        return CatfeinaDatabase(driver)
    }

    @Provides fun providePoesiaQueries(database: CatfeinaDatabase) = database.tbl_poesiaQueries
    @Provides fun providePoesiaNotaQueries(database: CatfeinaDatabase) = database.tbl_poesianotaQueries
    @Provides fun providePersonagemQueries(database: CatfeinaDatabase) = database.tbl_personagemQueries
    @Provides fun provideAtelierQueries(database: CatfeinaDatabase) = database.tbl_atelierQueries
    @Provides fun provideHistoricoQueries(database: CatfeinaDatabase) = database.tbl_historicoQueries
    @Provides fun provideInformativoQueries(database: CatfeinaDatabase) = database.tbl_informativoQueries
    @Provides fun provideMeowQueries(database: CatfeinaDatabase) = database.tbl_meowQueries
    //endregion

    //region DataStore / Preferences
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
    //endregion
}
