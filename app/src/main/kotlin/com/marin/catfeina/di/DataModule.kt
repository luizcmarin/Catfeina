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
*  Nota: Módulo de injeção de dependência para fontes de dados (Room, Retrofit, etc).
*
*/
package com.marin.catfeina.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.marin.catfeina.sqldelight.CatfeinaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext context: Context, @DatabaseName dbName: String): SqlDriver {
        return AndroidSqliteDriver(CatfeinaDatabase.Schema, context, dbName)
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver): CatfeinaDatabase {
        return CatfeinaDatabase(driver)
    }

    @Provides
    @Singleton
    fun provideTblPoesiaQueries(database: CatfeinaDatabase) = database.tbl_poesiaQueries

    @Provides
    @Singleton
    fun provideTblPoesiaNotaQueries(database: CatfeinaDatabase) = database.tbl_poesianotaQueries

    @Provides
    @Singleton
    fun provideTblAtelierQueries(database: CatfeinaDatabase) = database.tbl_atelierQueries

    @Provides
    @Singleton
    fun provideTblInformativoQueries(database: CatfeinaDatabase) = database.tbl_informativoQueries

    @Provides
    @Singleton
    fun provideTblPersonagemQueries(database: CatfeinaDatabase) = database.tbl_personagemQueries

    @Provides
    @Singleton
    fun provideTblMeowQueries(database: CatfeinaDatabase) = database.tbl_meowQueries

    @Provides
    @Singleton
    fun provideTblHistoricoQueries(database: CatfeinaDatabase) = database.tbl_historicoQueries

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("catfeina_prefs") }
        )
    }
}
