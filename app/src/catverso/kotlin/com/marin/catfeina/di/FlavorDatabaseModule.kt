/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/catverso/kotlin/com/marin/catfeina/di/FlavorDatabaseModule.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Módulo Hilt específico para o flavor 'catverso', responsável por prover
*  o nome do banco de dados correto.
*
*/
package com.marin.catfeina.di

import com.marin.core.Constantes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FlavorDatabaseModule {

    @Provides
    @DatabaseName
    fun provideDatabaseName(): String = Constantes.CATVERSO_DATABASE_FILE_NAME
}