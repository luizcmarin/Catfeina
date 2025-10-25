/*
 *  Projeto: Catfeina
 *  Arquivo: CatfeinaApp.kt
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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marin.catfeina.core.temas.TemasViewModel
import com.marin.catfeina.core.ui.SyncState
import com.marin.catfeina.core.utils.Icones
import com.marin.catfeina.ui.atelier.AtelierEditScreen
import com.marin.catfeina.ui.atelier.AtelierListScreen
import com.marin.catfeina.ui.personagens.PersonagensScreenHorizontal
import com.marin.catfeina.ui.personagens.PersonagensScreenVertical
import com.marin.catfeina.ui.informativo.InformativoScreen
import com.marin.catfeina.ui.main.MainScreenContent
import com.marin.catfeina.ui.main.MainScreenViewModel
import com.marin.catfeina.ui.main.MainViewModel
import com.marin.catfeina.ui.poesia.PoesiaDetailScreen
import com.marin.catfeina.ui.poesia.PoesiaListScreen
import com.marin.catfeina.ui.search.SearchScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatfeinaApp(
    temasViewModel: TemasViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val temasUiState by temasViewModel.uiState.collectAsStateWithLifecycle()
    val syncState by mainViewModel.syncState.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithBottomBar = remember { bottomBarNavItems.map { it.route }.toSet() }
    val shouldShowBottomBar = currentRoute in routesWithBottomBar

    val snackbarHostState = remember { SnackbarHostState() }
    val syncMessage by mainViewModel.syncStatusMessage.collectAsStateWithLifecycle()

    LaunchedEffect(syncMessage) {
        if (syncMessage != null) {
            snackbarHostState.showSnackbar(syncMessage!!)
            mainViewModel.limparMensagemSincronizacao()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuDrawerContent(
                navController = navController,
                currentRoute = currentRoute,
                temasViewModel = temasViewModel,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Catfeina") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icones.Menu, contentDescription = "Abrir Menu")
                        }
                    },
                    actions = {
                        Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                            if (syncState == SyncState.Executando) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            } else {
                                IconButton(onClick = { mainViewModel.iniciarSincronizacao() }) {
                                    Icon(imageVector = Icones.Atualizar, contentDescription = "Atualizar")
                                }
                            }
                        }
                        IconButton(onClick = { temasViewModel.onDarkModeChange(!temasUiState.isDarkMode) }) {
                            Icon(imageVector = Icones.Lampada, contentDescription = "Claro/Escuro")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            },
            bottomBar = {
                if (shouldShowBottomBar) {
                    BottomAppBar {
                        bottomBarNavItems.forEach { navItem ->
                            NavigationBarItem(
                                icon = { Icon(navItem.icon, contentDescription = navItem.title) },
                                label = { Text(navItem.title) },
                                selected = currentRoute == navItem.route,
                                onClick = {
                                    navController.navigate(navItem.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppDestinations.INICIO_ROUTE,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppDestinations.INICIO_ROUTE) {
                    MainScreenContent(
                        onPoesiaClick = { poesiaId ->
                            navController.navigate(AppScreenRoutes.poesiaDetail(poesiaId))
                        }
                    )
                }
                composable(AppDestinations.PESQUISA_ROUTE) {
                    SearchScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToResult = { tipo, id ->
                            // Como sua busca atualmente só retorna poesias,
                            // navegamos diretamente para o detalhe da poesia.
                            if (tipo == com.marin.catfeina.core.utils.TipoConteudoEnum.POESIA) {
                                navController.navigate(AppScreenRoutes.poesiaDetail(id))
                            }
                        }
                    )
                }
                composable(AppDestinations.ATELIER_ROUTE) {
                    AtelierListScreen(
                        onNavigateToEdit = { noteId ->
                            navController.navigate(AppScreenRoutes.atelierEdit(noteId))
                        }
                    )
                }
                composable(
                    route = AppDestinations.ATELIER_EDIT_WITH_ARG_ROUTE,
                    arguments = listOf(navArgument(AppDestinationsArgs.ATELIER_NOTE_ID_ARG) { type = NavType.LongType })
                ) {
                    AtelierEditScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = AppDestinations.INFORMATIVO_DETAIL_WITH_ARG_ROUTE,
                    arguments = listOf(
                        navArgument(AppDestinationsArgs.INFORMATIVO_CHAVE_ARG) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    InformativoScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(AppDestinations.POESIAS_ROUTE) {
                    val viewModel: MainScreenViewModel = hiltViewModel()
                    PoesiaListScreen(
                        viewModel,
                        onPoesiaClick = { poesiaId ->
                            navController.navigate(AppScreenRoutes.poesiaDetail(poesiaId))
                        }
                    )
                }
                composable(
                    route = AppDestinations.POESIA_DETAIL_WITH_ARG_ROUTE,
                    arguments = listOf(navArgument(AppDestinationsArgs.POESIA_ID_ARG) { type = NavType.LongType })
                ) {
                    PoesiaDetailScreen(onNavigateBack = { navController.navigateUp() })
                }
                composable(route = AppDestinations.PERSONAGENS_ROUTE_HORIZONTAL) {
                    PersonagensScreenHorizontal(
                        onNavigateToPersonagemDetail = { personagemId ->
                            navController.navigate(AppScreenRoutes.personagemDetail(personagemId))
                        }
                    )
                }
                composable(route = AppDestinations.PERSONAGENS_ROUTE_VERTICAL) {
                    PersonagensScreenVertical(
                        onNavigateToPersonagemDetail = { personagemId ->
                            navController.navigate(AppScreenRoutes.personagemDetail(personagemId))
                        }
                    )
                }
            }
        }
    }
}
