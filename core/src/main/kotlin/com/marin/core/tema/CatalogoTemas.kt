/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: core/tema/CatalogoTemas.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Catálogo central que define todos os esquemas de temas disponíveis na aplicação.
 *
 */

package com.marin.core.tema

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography

/**
 * Representa as chaves únicas para cada tema. Usado para identificação e persistência.
 */
enum class ChaveTema {
    PRIMAVERA,
    VERAO,
    OUTONO,
    INVERNO
}

/**
 * Modelo que define a estrutura de um tema, associando uma chave a um esquema de tema.
 */
data class Tema(
    val chave: ChaveTema,
    val nome: String,
    val esquema: EsquemaDoTema
)

/**
 * Interface que define o contrato para um esquema de tema, exigindo esquemas de cores e tipografia.
 */
interface EsquemaDoTema {
    val lightModeColors: ColorScheme
    val darkModeColors: ColorScheme
    val typography: Typography
}

/**
 * Objeto Singleton que serve como a única fonte da verdade para os temas predefinidos da aplicação.
 */
object CatalogoTemas {

    private val temasDisponiveis: Map<ChaveTema, Tema> = mapOf(
        ChaveTema.PRIMAVERA to Tema(ChaveTema.PRIMAVERA, "Primavera", PrimaveraEsquema),
        ChaveTema.VERAO to Tema(ChaveTema.VERAO, "Verão", VeraoEsquema),
        ChaveTema.OUTONO to Tema(ChaveTema.OUTONO, "Outono", OutonoEsquema),
        ChaveTema.INVERNO to Tema(ChaveTema.INVERNO, "Inverno", InvernoEsquema)
    )

    /**
     * Retorna uma lista de todos os temas disponíveis.
     * @return Uma lista de [Tema].
     */
    fun obterTodos(): List<Tema> = temasDisponiveis.values.toList()

    /**
     * Obtém um tema específico pela sua chave. Se a chave não for encontrada, retorna o tema Primavera.
     * @param chave A chave do tema desejado.
     * @return O [Tema] correspondente ou o tema Primavera como fallback.
     */
    fun obter(chave: ChaveTema?): Tema = temasDisponiveis[chave] ?: temasDisponiveis.getValue(ChaveTema.PRIMAVERA)
}
