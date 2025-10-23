/*
 *  Projeto: Catfeina
 *  Arquivo: DatabaseModule.kt
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
 *  Arquivo: DatabaseModule.kt
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
 * //  Arquivo: DatabaseModule.kt
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

package com.marin.catfeina.di

import android.content.Context
import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.marin.catfeina.core.utils.CategoriaPoesiaEnum
import com.marin.catfeina.core.utils.TipoConteudoEnum
import com.marin.catfeina.sqldelight.CatfeinaDatabase
import com.marin.catfeina.sqldelight.CatfeinaDatabaseQueries
import com.marin.catfeina.sqldelight.Historico
import com.marin.catfeina.sqldelight.Poesias
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // region ADAPTADORES DE TIPO
    private val categoriaPoesiaEnumAdapter = object : ColumnAdapter<CategoriaPoesiaEnum, String> {
        override fun decode(databaseValue: String): CategoriaPoesiaEnum = CategoriaPoesiaEnum.valueOf(databaseValue)
        override fun encode(value: CategoriaPoesiaEnum): String = value.name
    }

    private val tipoConteudoEnumAdapter = object : ColumnAdapter<TipoConteudoEnum, String> {
        override fun decode(databaseValue: String): TipoConteudoEnum = TipoConteudoEnum.valueOf(databaseValue)
        override fun encode(value: TipoConteudoEnum): String = value.name
    }
    // endregion

    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext appContext: Context): SqlDriver {
        return AndroidSqliteDriver(CatfeinaDatabase.Schema, appContext, "catfeina.db")
    }

    @Provides
    @Singleton
    fun provideCatfeinaDatabase(driver: SqlDriver): CatfeinaDatabase {
        return CatfeinaDatabase(
            driver = driver,
            poesiasAdapter = Poesias.Adapter(
                categoriaAdapter = categoriaPoesiaEnumAdapter
            ),
            historicoAdapter = Historico.Adapter(
                tipoConteudoAdapter = tipoConteudoEnumAdapter
            )
        )
    }

    @Provides
    @Singleton
    fun provideCatfeinaDatabaseQueries(database: CatfeinaDatabase): CatfeinaDatabaseQueries {
        return database.catfeinaDatabaseQueries
    }
}
