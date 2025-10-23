/*
 *  Projeto: Catfeina
 *  Arquivo: ParserModule.kt
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
 *  Arquivo: ParserModule.kt
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
 * //  Arquivo: ParserModule.kt
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

import com.marin.catfeina.core.formatador.parser.ParserTextoFormatado
import com.marin.catfeina.core.formatador.parser.ProcessadorCabecalho
import com.marin.catfeina.core.formatador.parser.ProcessadorCitacao
import com.marin.catfeina.core.formatador.parser.ProcessadorEstiloSimples
import com.marin.catfeina.core.formatador.parser.ProcessadorImagem
import com.marin.catfeina.core.formatador.parser.ProcessadorItemLista
import com.marin.catfeina.core.formatador.parser.ProcessadorLinhaHorizontal
import com.marin.catfeina.core.formatador.parser.ProcessadorLink
import com.marin.catfeina.core.formatador.parser.ProcessadorTag
import com.marin.catfeina.core.formatador.parser.ProcessadorTooltip
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParserModule {

    @Provides
    @Singleton
    fun provideParserTextoFormatado(
        // Injetamos o Provider aqui para quebrar o ciclo de dependência
        parserProvider: Provider<ParserTextoFormatado>
    ): ParserTextoFormatado {
        // Criamos a lista de processadores manualmente dentro do método
        val processadores = listOf<ProcessadorTag>(
            ProcessadorCabecalho(),
            ProcessadorCitacao(),
            ProcessadorEstiloSimples(),
            ProcessadorImagem(),
            // Passamos o Provider para o único processador que precisa dele
            ProcessadorItemLista(parserProvider),
            ProcessadorLinhaHorizontal(),
            ProcessadorLink(),
            ProcessadorTooltip()
        )
        // Retornamos o parser com sua lista de dependências já resolvida
        return ParserTextoFormatado(processadores)
    }
}
