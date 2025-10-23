/*
 *  Projeto: Catfeina
 *  Arquivo: PersonagensNavigation.kt
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


package com.marin.catfeina.features.personagens.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.marin.catfeina.features.personagens.presentation.PersonagensScreenHorizontal
import com.marin.catfeina.features.personagens.presentation.PersonagensScreenVertical

const val PERSONAGENS_ROUTE_HORIZONTAL = "personagens_horizontal_route"
const val PERSONAGENS_ROUTE_VERTICAL = "personagens_vertical_route"

fun NavController.navigateToPersonagensHorizontal() {
    this.navigate(PERSONAGENS_ROUTE_HORIZONTAL)
}

fun NavController.navigateToPersonagensVertical() {
    this.navigate(PERSONAGENS_ROUTE_VERTICAL)
}

/**
 * Adiciona as telas de Personagens ao seu NavGraph principal.
 * Você pode escolher qual das duas versões (Horizontal ou Vertical) quer usar.
 */
fun NavGraphBuilder.personagensGraph(navController: NavController) {
    // Rota para a tela com cards HORIZONTAIS
    composable(route = PERSONAGENS_ROUTE_HORIZONTAL) {
        PersonagensScreenHorizontal(
            onNavigateToPersonagemDetail = {
                // Adicionar navegação para a tela de detalhe do personagem
                // navController.navigateToPersonagemDetail(it)
            }
        )
    }

    // Rota para a tela com cards VERTICAIS (em grid)
    composable(route = PERSONAGENS_ROUTE_VERTICAL) {
        PersonagensScreenVertical(
            onNavigateToPersonagemDetail = {
                // Adicionar navegação para a tela de detalhe do personagem
                // navController.navigateToPersonagemDetail(it)
            }
        )
    }
}
