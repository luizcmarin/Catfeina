/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/busca/BuscaScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela de Busca.
*
*/
package com.marin.catfeina.ui.telas.busca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.marin.catfeina.R
import com.marin.catfeina.Screen
import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.core.ui.AnimatedAsset
import com.marin.core.util.cliqueSeguro

@Composable
fun BuscaScreen(
    modifier: Modifier = Modifier,
    viewModel: BuscaViewModel = hiltViewModel(),
    navController: NavController,
    repository: PoesiaRepository
) {
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BuscaScreenContent(
        modifier = modifier,
        searchTerm = searchTerm,
        uiState = uiState,
        repository = repository,
        onSearchTermChange = { viewModel.onSearchTermChange(it) },
        onPoesiaClick = { poesiaId ->
            navController.navigate(Screen.LeitorPoesia.createRoute(poesiaId))
        }
    )
}

@Composable
private fun BuscaScreenContent(
    modifier: Modifier = Modifier,
    searchTerm: String,
    uiState: BuscaUiState,
    repository: PoesiaRepository,
    onSearchTermChange: (String) -> Unit,
    onPoesiaClick: (Long) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = onSearchTermChange,
            label = { Text(stringResource(R.string.busca_placeholder)) },
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
        )

        uiState.debugQuery?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { clipboardManager.setText(AnnotatedString(it)) }) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copiar Query"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (searchTerm.length > 2) {
            ResultadosBusca(uiState = uiState, repository = repository, onPoesiaClick = onPoesiaClick, modifier = Modifier.weight(0.4f))
        } else {
             CenteredMessage(assetName = "cat_soneca.lottie", message = stringResource(R.string.busca_ociosa), modifier = Modifier.weight(0.4f))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        
        Favoritos(uiState = uiState, repository = repository, onPoesiaClick = onPoesiaClick, modifier = Modifier.weight(0.6f))
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun ResultadosBusca(uiState: BuscaUiState, repository: PoesiaRepository, onPoesiaClick: (Long) -> Unit, modifier: Modifier = Modifier) {
     Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error != null -> Text(uiState.error)
            uiState.resultados.isEmpty() -> Text(stringResource(R.string.busca_sem_resultados))
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.resultados, key = {it.id}) { poesia ->
                        PoesiaSearchResultItem(poesia = poesia, repository = repository, onClick = { onPoesiaClick(poesia.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun Favoritos(uiState: BuscaUiState, repository: PoesiaRepository, onPoesiaClick: (Long) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text("Favoritos", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (uiState.favoritos.isEmpty()) {
            CenteredMessage(assetName = "", message = "Você ainda não tem poesias favoritas.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.favoritos, key = {it.id}) { poesia ->
                    PoesiaSearchResultItem(poesia = poesia, repository = repository, onClick = { onPoesiaClick(poesia.id) })
                }
            }
        }
    }
}

@Composable
private fun PoesiaSearchResultItem(poesia: Poesia, repository: PoesiaRepository, onClick: () -> Unit) {
    val titulo = remember(poesia) { repository.extrairTitulo(poesia) }
    val resumo = remember(poesia) { repository.extrairResumo(poesia) }
    val imagem = remember(poesia) { repository.extrairImagemPrincipal(poesia) }

    ListItem(
        headlineContent = { Text(titulo) },
        supportingContent = { Text(resumo, maxLines = 2) },
        leadingContent = {
            imagem?.let {
                AsyncImage(
                    request = ImageRequest(LocalContext.current, it),
                    contentDescription = titulo,
                    modifier = Modifier.size(56.dp).aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }
        },
        modifier = Modifier.cliqueSeguro(onClick = onClick).fillMaxWidth()
    )
}

@Composable
private fun CenteredMessage(assetName: String, message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(assetName.isNotBlank()){
            AnimatedAsset(
                modifier = Modifier.size(150.dp),
                assetName = assetName
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(text = message)
    }
}
