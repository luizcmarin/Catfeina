/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/configuracoes/SettingsScreen.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable para a tela de Configurações.
*
*/
package com.marin.catfeina.ui.telas.configuracoes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.R
import com.marin.catfeina.ui.TemaUiState
import com.marin.catfeina.ui.TemaViewModel
import com.marin.core.tema.CatalogoTemas
import com.marin.core.tema.ChaveTema
import com.marin.core.tema.ModoNoturno
import com.marin.core.ui.Icones

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    temaViewModel: TemaViewModel = hiltViewModel()
) {
    val temaUiState by temaViewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreenContent(
        modifier = modifier,
        temaUiState = temaUiState,
        onModoNoturnoChange = { modo -> temaViewModel.definirModoNoturno(modo) },
        onTemaChange = { chave -> temaViewModel.selecionarTema(chave) }
    )
}

@Composable
private fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    temaUiState: TemaUiState,
    onModoNoturnoChange: (ModoNoturno) -> Unit,
    onTemaChange: (ChaveTema) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item { SectionTitle(stringResource(R.string.configuracoes_aparencia_titulo)) }

        item {
            SettingRow(
                icon = Icones.Lampada,
                title = stringResource(R.string.configuracoes_modo_escuro_titulo),
                description = stringResource(R.string.configuracoes_modo_escuro_descricao)
            ) {
                SegmentedButton(
                    items = ModoNoturno.entries.map { it.name.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() } },
                    selectedIndex = temaUiState.modoNoturno.ordinal,
                    onItemSelected = { index -> onModoNoturnoChange(ModoNoturno.entries[index]) }
                )
            }
        }

        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }

        item {
            SettingRow(
                icon = Icones.PaletaDeCores,
                title = stringResource(R.string.configuracoes_tema_sazonal_titulo),
                description = stringResource(R.string.configuracoes_tema_sazonal_descricao)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                temaUiState.catalogo.forEachIndexed { index, theme ->
                    val isSelected = temaUiState.tema.chave == theme.chave
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                                else Color.Transparent
                            )
                            .clickable { onTemaChange(theme.chave) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = theme.chave.name.take(3),
                            color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                            else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    if (index < temaUiState.catalogo.size - 1) {
                        VerticalDivider(
                            modifier = Modifier.height(48.dp),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    description: String? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (trailingContent != null) {
            trailingContent()
        }
    }
}

@Composable
private fun SegmentedButton(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                        else Color.Transparent
                    )
                    .clickable { onItemSelected(index) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
            if (index < items.size - 1) {
                VerticalDivider(
                    modifier = Modifier.height(48.dp),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Configurações - Primavera/Sistema")
@Composable
private fun SettingsScreenPreview_Default() {
    SettingsScreenContent(
        temaUiState = TemaUiState(
            tema = CatalogoTemas.obter(ChaveTema.PRIMAVERA),
            modoNoturno = ModoNoturno.SISTEMA,
            catalogo = CatalogoTemas.obterTodos()
        ),
        onModoNoturnoChange = {},
        onTemaChange = {}
    )
}

@Preview(showBackground = true, name = "Configurações - Inverno/Escuro")
@Composable
private fun SettingsScreenPreview_WinterDark() {
    SettingsScreenContent(
        temaUiState = TemaUiState(
            tema = CatalogoTemas.obter(ChaveTema.INVERNO),
            modoNoturno = ModoNoturno.ESCURO,
            catalogo = CatalogoTemas.obterTodos()
        ),
        onModoNoturnoChange = {},
        onTemaChange = {}
    )
}
