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
    const val POESIA_ID_ARG = "poesiaId"
    const val INFORMATIVO_CHAVE_ARG = "informativoChave"
}

object AppDestinations {
    const val INICIO_ROUTE = "inicio"
    const val PESQUISA_ROUTE = "pesquisa"
    const val ATELIER_ROUTE = "atelier"
    const val POESIAS_ROUTE = "poesias"
    const val POESIA_DETAIL_ROUTE = "poesia_detail"
    const val INFORMATIVO_DETAIL_ROUTE = "informativo_detail"
    const val POESIA_DETAIL_WITH_ARG_ROUTE = "${POESIA_DETAIL_ROUTE}/{${AppDestinationsArgs.POESIA_ID_ARG}}"
    const val INFORMATIVO_DETAIL_WITH_ARG_ROUTE = "${INFORMATIVO_DETAIL_ROUTE}/{${AppDestinationsArgs.INFORMATIVO_CHAVE_ARG}}"
    const val PREFACIO_ROUTE = "prefacio"
}

object AppScreenRoutes {
    fun poesiaDetail(poesiaId: Long) = "${AppDestinations.POESIA_DETAIL_ROUTE}/$poesiaId"
    fun informativoDetail(chave: String) = "${AppDestinations.INFORMATIVO_DETAIL_ROUTE}/$chave"
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
    NavMenuItem(AppDestinations.PREFACIO_ROUTE, "Prefácio", Icones.QrCode)
)