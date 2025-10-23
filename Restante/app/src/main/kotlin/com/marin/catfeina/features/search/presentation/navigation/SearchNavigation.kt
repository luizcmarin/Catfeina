/*
 *  Projeto: Catfeina
 *  Arquivo: SearchNavigation.kt
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


package com.marin.catfeina.features.search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.marin.catfeina.features.search.presentation.SearchScreen

const val SEARCH_ROUTE = "search_route"

fun NavController.navigateToSearch() {
    this.navigate(SEARCH_ROUTE)
}

fun NavGraphBuilder.searchGraph(navController: NavController) {
    composable(route = SEARCH_ROUTE) {
        SearchScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToResult = { tipo, id ->
                // Lógica para navegar para a tela de detalhe correta
                // Exemplo:
                // when (tipo) {
                //     TipoConteudoEnum.POESIA -> navController.navigateToPoesiaDetail(id)
                //     TipoConteudoEnum.PERSONAGEM -> navController.navigateToPersonagemDetail(id)
                //     TipoConteudoEnum.ATELIER -> navController.navigateToAtelierEdit(id)
                // }
            }
        )
    }
}
