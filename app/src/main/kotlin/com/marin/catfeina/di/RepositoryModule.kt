/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/di/RepositoryModule.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Módulo Hilt consolidado para vincular as interfaces dos repositórios
*  às suas implementações concretas.
*
*/
package com.marin.catfeina.di

import com.marin.catfeina.data.repositories.AtelierRepository
import com.marin.catfeina.data.repositories.AtelierRepositoryImpl
import com.marin.catfeina.data.repositories.HistoricoRepository
import com.marin.catfeina.data.repositories.HistoricoRepositoryImpl
import com.marin.catfeina.data.repositories.InformativoRepository
import com.marin.catfeina.data.repositories.InformativoRepositoryImpl
import com.marin.catfeina.data.repositories.MeowRepository
import com.marin.catfeina.data.repositories.MeowRepositoryImpl
import com.marin.catfeina.data.repositories.PersonagemRepository
import com.marin.catfeina.data.repositories.PersonagemRepositoryImpl
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.catfeina.data.repositories.PoesiaRepositoryImpl
import com.marin.catfeina.data.repositories.SyncRepository
import com.marin.catfeina.data.repositories.SyncRepositoryImpl
import com.marin.catfeina.data.repositories.UserPreferencesRepository
import com.marin.core.tema.PreferenciasDeTemaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAtelierRepository(impl: AtelierRepositoryImpl): AtelierRepository

    @Binds
    @Singleton
    abstract fun bindHistoricoRepository(impl: HistoricoRepositoryImpl): HistoricoRepository

    @Binds
    @Singleton
    abstract fun bindInformativoRepository(impl: InformativoRepositoryImpl): InformativoRepository

    @Binds
    @Singleton
    abstract fun bindPersonagemRepository(impl: PersonagemRepositoryImpl): PersonagemRepository

    @Binds
    @Singleton
    abstract fun bindPoesiaRepository(impl: PoesiaRepositoryImpl): PoesiaRepository

    @Binds
    @Singleton
    abstract fun bindSyncRepository(impl: SyncRepositoryImpl): SyncRepository

    @Binds
    @Singleton
    abstract fun bindMeowRepository(impl: MeowRepositoryImpl): MeowRepository

    @Binds
    @Singleton
    abstract fun bindPreferenciasDeTemaRepository(impl: UserPreferencesRepository): PreferenciasDeTemaRepository
}
