/*
 *  Projeto: Catfeina
 *  Arquivo: TemasScreen.kt
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

/*
 *
 *  Projeto: Catfeina
 *  Arquivo: TemasScreen.kt
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: TemasScreen.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
 *
 */

// ===================================================================================
// Arquivo: com.marin.catfeina.ui.temas.TemasScreen.kt
//
// Descrição: Composable que constrói a tela de seleção de temas.
// ===================================================================================

package com.marin.catfeina.ui.temas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.marin.catfeina.core.temas.TemasPredefinidos
import com.marin.catfeina.core.temas.ThemeModelKey
import com.marin.catfeina.core.utils.Icones

@Composable
fun TemasScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: TemasViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    TemasScreenContent(
        uiState = uiState,
        onThemeSelected = viewModel::onThemeSelected,
        onDarkModeChanged = viewModel::onDarkModeChange,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemasScreenContent(
    uiState: TemasUiState,
    onThemeSelected: (ThemeModelKey) -> Unit,
    onDarkModeChanged: (Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Temas") },
                windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icones.Voltar,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Ação do FAB */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icones.Mais, contentDescription = "Adicionar")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Estações",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.temasDisponiveis.entries.toList()) { (key, themeModel) ->
                        val isSelected = uiState.temaAtualKey == key

                        ThemeSelectorChip(
                            label = key.name,
                            chipColorScheme = if (uiState.isDarkMode) themeModel.colorPalette.darkModeColors else themeModel.colorPalette.lightModeColors,
                            isSelected = isSelected,
                            onClick = { onThemeSelected(key) }
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Modo Escuro", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = onDarkModeChanged,
                        thumbContent = if (uiState.isDarkMode) {
                            {
                                Icon(
                                    imageVector = Icones.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Exemplos de Componentes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icones.Lampada, contentDescription = null)
                        Text("Que tal uma xícara de café pela manhã?")
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Itens de Trabalho", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        CheckboxRow(text = "Beber água de manhã", initialChecked = true)
                        CheckboxRow(text = "Comprar comida no mercado", initialChecked = false)
                        CheckboxRow(
                            text = "Construir módulo de tema dinâmico",
                            initialChecked = true
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ThemeSelectorChip(
    label: String,
    chipColorScheme: ColorScheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 80.dp, height = 50.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(chipColorScheme.primary)
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.shapes.medium
                ) else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = chipColorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun CheckboxRow(text: String, initialChecked: Boolean) {
    var checked by remember { mutableStateOf(initialChecked) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { checked = !checked }
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it }
        )
        Text(text = text, modifier = Modifier.padding(start = 8.dp))
    }
}

@Preview(showBackground = true, name = "Tela de Vozes - Claro")
@Composable
private fun TemasScreenLightPreview() {
    val temasPreview = TemasPredefinidos.get()
    val temaVerao = temasPreview.getValue(ThemeModelKey.VERAO)
    val previewState = TemasUiState(
        temasDisponiveis = temasPreview,
        temaAtualKey = ThemeModelKey.VERAO,
        isDarkMode = false
    )

    MaterialTheme(colorScheme = temaVerao.colorPalette.lightModeColors) {
        TemasScreenContent(
            uiState = previewState,
            onThemeSelected = {},
            onDarkModeChanged = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Tela de Vozes - Escuro")
@Composable
private fun TemasScreenDarkPreview() {
    val temasPreview = TemasPredefinidos.get()
    val temaInverno = temasPreview.getValue(ThemeModelKey.INVERNO)

    val previewState = TemasUiState(
        temasDisponiveis = temasPreview,
        temaAtualKey = ThemeModelKey.INVERNO,
        isDarkMode = true
    )

    MaterialTheme(colorScheme = temaInverno.colorPalette.darkModeColors) {
        TemasScreenContent(
            uiState = previewState,
            onThemeSelected = {},
            onDarkModeChanged = {},
            onNavigateBack = {}
        )
    }
}
