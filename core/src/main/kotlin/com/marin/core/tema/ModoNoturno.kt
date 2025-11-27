/*
*  Projeto: Catfeina/Catverso
*  Arquivo: core/src/main/kotlin/com/marin/core/tema/ModoNoturno.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Enum que representa as opções de modo noturno disponíveis para o usuário,
*  permitindo uma escolha explícita entre claro, escuro ou seguir o sistema.
*
*/
package com.marin.core.tema

/**
 * Define os três estados possíveis para a configuração do modo noturno da aplicação.
 */
enum class ModoNoturno {
    CLARO,    // Força o tema claro, independentemente da configuração do sistema.
    ESCURO,   // Força o tema escuro, independentemente da configuração do sistema.
    SISTEMA   // Segue a configuração de tema do sistema operacional.
}
