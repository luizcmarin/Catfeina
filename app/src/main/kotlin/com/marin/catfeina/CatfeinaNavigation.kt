/*
 *  Projeto: Catfeina
 *  Arquivo: CatfeinaNavigation.kt
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

package com.marin.catfeina

import androidx.compose.ui.graphics.vector.ImageVector
import com.marin.catfeina.core.utils.Icones

object AppDestinationsArgs {
    const val ATELIER_NOTE_ID_ARG = "noteId"
    const val POESIA_ID_ARG = "poesiaId"
    const val INFORMATIVO_CHAVE_ARG = "informativoChave"
}

object AppDestinations {
    const val INICIO_ROUTE = "inicio"
    const val PESQUISA_ROUTE = "pesquisa"
    const val ATELIER_ROUTE = "atelier"
    const val ATELIER_EDIT_ROUTE = "atelier_edit"
    const val ATELIER_EDIT_WITH_ARG_ROUTE = "${ATELIER_EDIT_ROUTE}/{${AppDestinationsArgs.ATELIER_NOTE_ID_ARG}}"
    const val PERSONAGENS_ROUTE_HORIZONTAL = "personagens_horizontal_route"
    const val PERSONAGENS_ROUTE_VERTICAL = "personagens_vertical_route"
    const val PERSONAGEM_DETAIL_ROUTE = "personagem_detail"
    const val POESIAS_ROUTE = "poesias"
    const val POESIA_DETAIL_ROUTE = "poesia_detail"
    const val INFORMATIVO_DETAIL_ROUTE = "informativo_detail"
    const val POESIA_DETAIL_WITH_ARG_ROUTE = "${POESIA_DETAIL_ROUTE}/{${AppDestinationsArgs.POESIA_ID_ARG}}"
    const val INFORMATIVO_DETAIL_WITH_ARG_ROUTE = "${INFORMATIVO_DETAIL_ROUTE}/{${AppDestinationsArgs.INFORMATIVO_CHAVE_ARG}}"
}

object AppScreenRoutes {
    fun atelierEdit(noteId: Long) = "${AppDestinations.ATELIER_EDIT_ROUTE}/$noteId"
    fun poesiaDetail(poesiaId: Long) = "${AppDestinations.POESIA_DETAIL_ROUTE}/$poesiaId"
    fun informativoDetail(chave: String) = "${AppDestinations.INFORMATIVO_DETAIL_ROUTE}/$chave"
    fun personagemDetail(personagemId: Long) = "${AppDestinations.PERSONAGEM_DETAIL_ROUTE}/$personagemId"
}

data class NavMenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

val bottomBarNavItems = listOf(
    NavMenuItem(AppDestinations.INICIO_ROUTE, "Início", Icones.Inicio),
    NavMenuItem(AppDestinations.POESIAS_ROUTE, "Poesias", Icones.Poesia),
    NavMenuItem(AppDestinations.PESQUISA_ROUTE, "Pesquisa", Icones.Pesquisa),
    NavMenuItem(AppDestinations.ATELIER_ROUTE, "Atelier", Icones.Diamante)
)

val drawerNavItems = listOf(
    NavMenuItem(AppDestinations.INICIO_ROUTE, "Início", Icones.Inicio),
    NavMenuItem(AppDestinations.POESIAS_ROUTE, "Poesias", Icones.Poesia),
    NavMenuItem(AppDestinations.ATELIER_ROUTE, "Atelier", Icones.Atelier),
    NavMenuItem(AppDestinations.PERSONAGENS_ROUTE_VERTICAL, "Personagens V", Icones.Personagem),
    NavMenuItem(AppDestinations.PERSONAGENS_ROUTE_HORIZONTAL, "Personagens H", Icones.Personagem),
    NavMenuItem(AppScreenRoutes.informativoDetail("prefacio"), "Prefácio", Icones.Prefacio)
)