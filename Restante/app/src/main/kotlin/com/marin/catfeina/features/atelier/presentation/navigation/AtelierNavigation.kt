/*
 *  Projeto: Catfeina
 *  Arquivo: AtelierNavigation.kt
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

\npackage com.marin.catfeina.features.atelier.presentation.navigation\n\nimport androidx.navigation.NavController\nimport androidx.navigation.NavGraphBuilder\nimport androidx.navigation.NavType\nimport androidx.navigation.compose.composable\nimport androidx.navigation.navArgument\nimport com.marin.catfeina.features.atelier.presentation.edit.AtelierEditScreen\nimport com.marin.catfeina.features.atelier.presentation.list.AtelierListScreen\n\n// 1. Definição das rotas como constantes\nconst val ATELIER_LIST_ROUTE = \"atelier_list_route\"\nconst val ATELIER_EDIT_ROUTE = \"atelier_edit_route\"\n\n// 2. Funções de navegação para segurança de tipo\nfun NavController.navigateToAtelierList() {\n    this.navigate(ATELIER_LIST_ROUTE)\n}\n\nfun NavController.navigateToAtelierEdit(noteId: Long = 0L) {\n    this.navigate(\"$ATELIER_EDIT_ROUTE/$noteId\")\n}\n\n// 3. Função de extensão para adicionar as telas ao seu NavGraph principal\nfun NavGraphBuilder.atelierGraph(navController: NavController) {\n    composable(route = ATELIER_LIST_ROUTE) {\n        AtelierListScreen(\n            onNavigateToEdit = { noteId ->\n                navController.navigateToAtelierEdit(noteId)\n            }\n        )\n    }\n\n    composable(\n        route = \"$ATELIER_EDIT_ROUTE/{noteId}\",\n        arguments = listOf(navArgument(\"noteId\") { type = NavType.LongType })\n    ) {\n        AtelierEditScreen(\n            onNavigateBack = {\n                navController.popBackStack()\n            }\n        )\n    }\n}\n