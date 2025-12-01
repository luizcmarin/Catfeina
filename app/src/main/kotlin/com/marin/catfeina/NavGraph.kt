/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/NavGraph.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Define as rotas e o grafo de navegação principal da aplicação usando Jetpack Navigation Compose.
*
*/
package com.marin.catfeina

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.catfeina.ui.TemaViewModel
import com.marin.catfeina.ui.telas.atelier.AtelierScreen
import com.marin.catfeina.ui.telas.busca.BuscaScreen
import com.marin.catfeina.ui.telas.configuracoes.SettingsScreen
import com.marin.catfeina.ui.telas.debug.DebugScreen
import com.marin.catfeina.ui.telas.historico.HistoricoScreen
import com.marin.catfeina.ui.telas.informativo.InformativoScreen
import com.marin.catfeina.ui.telas.inicio.InicioScreen
import com.marin.catfeina.ui.telas.leitorpoesia.PoesiaReaderScreen
import com.marin.catfeina.ui.telas.personagens.PersonagensScreen
import com.marin.catfeina.ui.telas.poesias.PoesiasScreen
import com.marin.catfeina.ui.telas.sincronizacao.SyncScreen
import com.marin.catfeina.ui.telas.splash.SplashScreen

// Sealed class para garantir a segurança de tipos (type-safety) na navegação.
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Inicio : Screen("inicio")
    data object Poesias : Screen("poesias")
    data object LeitorPoesia : Screen("leitor_poesia/{poesiaId}") {
        fun createRoute(poesiaId: Long) = "leitor_poesia/$poesiaId"
    }

    data object Informativo : Screen("informativo/{informativoKey}") {
        fun createRoute(informativoKey: String) = "informativo/$informativoKey"
    }

    data object Atelier : Screen("atelier")
    data object Busca : Screen("busca")
    data object Personagens : Screen("personagens")
    data object Historico : Screen("historico")
    data object Sincronizacao : Screen("sincronizacao")
    data object Configuracoes : Screen("configuracoes")
    data object Debug : Screen("debug")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: PoesiaRepository,
    modifier: Modifier = Modifier,
    temaViewModel: TemaViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToMain = {
                navController.navigate(Screen.Inicio.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Inicio.route) {
            InicioScreen(navController = navController)
        }
        composable(Screen.Poesias.route) {
            PoesiasScreen(navController = navController)
        }
        composable(Screen.LeitorPoesia.route) {
            PoesiaReaderScreen(navController = navController)
        }
        composable(Screen.Informativo.route) {
            InformativoScreen(navController = navController)
        }
        composable(Screen.Atelier.route) {
            AtelierScreen()
        }
        composable(Screen.Busca.route) {
            BuscaScreen(navController = navController, repository = repository)
        }
        composable(Screen.Personagens.route) {
            PersonagensScreen()
        }
        composable(Screen.Historico.route) {
            HistoricoScreen(
                onPoesiaClick = { poesiaId ->
                    navController.navigate(
                        Screen.LeitorPoesia.createRoute(
                            poesiaId
                        )
                    )
                }
            )
        }
        composable(Screen.Sincronizacao.route) {
            SyncScreen(onSyncComplete = { navController.navigateUp() })
        }
        composable(Screen.Configuracoes.route) {
            SettingsScreen(temaViewModel = temaViewModel)
        }
        composable(Screen.Debug.route) {
            DebugScreen(navController = navController)
        }
    }
}