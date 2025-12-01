/*
*  Projeto: Catfeina/Catverso
*  Arquivo: core/src/main/kotlin/com/marin/core/tema/PreferenciasDeTemaRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Interface que define o contrato para o repositório de preferências de tema,
*  permitindo que a camada de domínio (:core) acesse as preferências sem
*  conhecer a implementação da camada de dados (:app).
*
*/
package com.marin.core.tema

import kotlinx.coroutines.flow.Flow

/**
 * Interface para o repositório que gerencia as preferências de tema do usuário.
 * Define o contrato que a camada de dados deve implementar.
 */
interface PreferenciasDeTemaRepository {

    val chaveTema: Flow<ChaveTema>
    val modoNoturno: Flow<ModoNoturno>
    val escalaFonte: Flow<Float>

    suspend fun setChaveTema(chave: ChaveTema)
    suspend fun setModoNoturno(modo: ModoNoturno)
    suspend fun setEscalaFonte(escala: Float)
}
