/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/androidTest/kotlin/com/marin/catfeina/data/repositories/PoesiaRepositoryTest.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Teste instrumentado para o PoesiaRepository, validando a interação com o
*  banco de dados em um ambiente de teste isolado com Hilt.
*
*/
package com.marin.catfeina.data.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.marin.catfeina.data.models.Poesia
import com.marin.core.ui.UiState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class PoesiaRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var poesiaRepository: PoesiaRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun buscarPoesias_deveRetornarApenasPoesiasCorrespondentes() = runTest {
        // 1. Arrange: Inserir dados de teste
        val poesiasDeTeste = listOf(
            poesiaDeTeste(1, "Gato de Botas", "Era uma vez um gato muito esperto."),
            poesiaDeTeste(2, "O Cão e a Raposa", "Uma amizade improvável no coração da floresta."),
            poesiaDeTeste(3, "Gato Malhado", "Um felino malhado e muito arteiro.")
        )
        poesiaRepository.upsertPoesias(poesiasDeTeste)

        // 2. Act & 3. Assert: Executar a busca e verificar o resultado.
        // Em um ambiente de teste com dispatcher imediato, o estado Loading pode ser
        // resolvido tão rápido que o coletamos diretamente como Success.
        val state = poesiaRepository.buscarPoesias("gato").first { it is UiState.Success }

        // Verifica se o estado é Success
        assertThat(state).isInstanceOf(UiState.Success::class.java)
        val resultado = (state as UiState.Success).data

        // Verifica se o resultado tem o tamanho esperado (2 poesias com "gato")
        assertThat(resultado).hasSize(2)

        // Verifica se os títulos corretos foram retornados, ignorando a ordem
        val titulos = resultado.map { it.titulo }
        assertThat(titulos).containsExactly("Gato de Botas", "Gato Malhado")
    }

    // Função utilitária para criar objetos Poesia para os testes
    private fun poesiaDeTeste(id: Long, titulo: String, texto: String) = Poesia(
        id = id,
        titulo = titulo,
        texto = texto,
        imagem = null,
        autor = "Autor Teste",
        nota = null,
        textobase = "",
        textofinal = null,
        anterior = null,
        proximo = null,
        atualizadoem = System.currentTimeMillis(),
        favorita = false,
        lida = false,
        dataleitura = null,
        notausuario = null
    )
}
