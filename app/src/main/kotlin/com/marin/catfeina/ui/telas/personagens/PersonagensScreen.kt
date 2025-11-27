/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/personagens/PersonagensScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela de Personagens.
*
*/

package com.marin.catfeina.ui.telas.personagens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.marin.catfeina.data.models.Personagem
import com.marin.catfeina.ui.componentes.ErrorMessage
import com.marin.catfeina.ui.componentes.LoadingWheel

@Composable
fun PersonagensScreen(
    viewModel: PersonagensViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is PersonagensUiState.Loading -> LoadingWheel()
        is PersonagensUiState.Error -> ErrorMessage()
        is PersonagensUiState.Success -> {
            if (state.personagens.isEmpty()) {
                // TODO: Adicionar um estado para quando a lista está vazia
            } else {
                PersonagensList(personagens = state.personagens)
            }
        }
    }
}

@Composable
fun PersonagensList(personagens: List<Personagem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(personagens) { personagem ->
            PersonagemCard(personagem = personagem)
        }
    }
}

@Composable
fun PersonagemCard(personagem: Personagem) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = personagem.nome, style = MaterialTheme.typography.titleLarge)
            Text(text = personagem.biografia, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
