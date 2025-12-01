/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/poesias/PoesiasScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Tela para exibir a lista completa de poesias.
*
*/
package com.marin.catfeina.ui.telas.poesias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.marin.catfeina.Screen
import com.marin.catfeina.ui.telas.inicio.PoesiaListItem

@Composable
fun PoesiasScreen(
    navController: NavController,
    viewModel: PoesiasViewModel = hiltViewModel(),
) {
    val poesias = viewModel.poesias.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(poesias.itemCount) { index ->
            val poesia = poesias[index]
            poesia?.let {
                PoesiaListItem(poesia = it, onClick = {
                    navController.navigate(Screen.LeitorPoesia.createRoute(it.id))
                })
            }
        }
    }
}
