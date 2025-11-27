/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/MenuDrawer.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Conteúdo do menu lateral (Drawer) da aplicação, incluindo as configurações.
*
*/
package com.marin.catfeina

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.marin.catfeina.ui.TemaViewModel
import com.marin.core.tema.ModoNoturno
import com.marin.core.ui.AnimatedAsset
import com.marin.core.ui.Icones
import com.marin.core.util.IntentHelper
import kotlinx.coroutines.launch

@Composable
fun CatfeinaDrawerContent(
    navController: NavController,
    currentRoute: String?,
    onCloseDrawer: () -> Unit,
    viewModel: TemaViewModel = hiltViewModel()
) {
    val drawerSections = remember {
        mutableListOf<@Composable () -> Unit>(
            { NavigationDrawerSection(navController, currentRoute, onCloseDrawer) },
            { ThemeSettingsSection(viewModel) },
            // { TextSettingsSection(viewModel) }, // TODO: Reativar quando o estado de texto for implementado
            { ContactSection() },
            { PrivacySection() },
            { AboutSection() },
            { LegalSection(navController, onCloseDrawer) }
        ).apply {
            if (BuildConfig.DEBUG) {
                add { DebugSection(navController, onCloseDrawer) }
            }
        }
    }

    ModalDrawerSheet {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp)
        ) {
            item { DrawerHeader(onCloseDrawer) }

            items(drawerSections.size) { index ->
                if (index > 0) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }
                drawerSections[index]()
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

private fun NavController.navigateToScreen(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun DrawerHeader(onClose: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        IconButton(onClick = onClose) {
            Icon(Icones.Voltar, contentDescription = stringResource(R.string.drawer_voltar))
        }
        Spacer(Modifier.width(16.dp))
        Icon(
            Icones.Diamante,
            contentDescription = stringResource(R.string.drawer_logo_descricao),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun NavigationDrawerSection(
    navController: NavController,
    currentRoute: String?,
    onCloseDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerNavItems = listOf(Screen.Inicio, Screen.Busca, Screen.Atelier, Screen.Personagens)

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        drawerNavItems.forEach { screen ->
            val icon = when (screen) {
                is Screen.Inicio -> Icones.Inicio
                is Screen.Busca -> Icones.Pesquisa
                is Screen.Atelier -> Icones.Atelier
                is Screen.Personagens -> Icones.Personagem
                else -> Icones.SemImagem
            }
            NavigationDrawerItem(
                icon = { Icon(icon, contentDescription = screen.route) },
                label = { Text(screen.route.replaceFirstChar { it.titlecase() }) },
                selected = currentRoute == screen.route,
                onClick = {
                    scope.launch {
                        onCloseDrawer()
                        navController.navigateToScreen(screen.route)
                    }
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
private fun ThemeSettingsSection(viewModel: TemaViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            stringResource(R.string.drawer_tema_aparencia),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))

        // Controle de Modo Noturno de 3 estados
        SegmentedButton( 
            items = ModoNoturno.entries.map { it.name.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() } },
            selectedIndex = uiState.modoNoturno.ordinal,
            onItemSelected = { index ->
                val modo = ModoNoturno.entries[index]
                viewModel.definirModoNoturno(modo)
            }
        )

        Spacer(Modifier.height(16.dp))

        // Seletor de Tema Sazonal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            uiState.catalogo.forEachIndexed { index, tema ->
                val isSelected = uiState.tema.chave == tema.chave
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                            else Color.Transparent
                        )
                        .clickable { viewModel.selecionarTema(tema.chave) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tema.chave.name.take(3),
                        color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
                if (index < uiState.catalogo.size - 1) {
                    VerticalDivider(
                        modifier = Modifier.height(48.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

// TODO: Implementar estado e lógica para TextSettingsSection no TemaViewModel
@Composable
private fun TextSettingsSection(viewModel: TemaViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            stringResource(R.string.drawer_configuracoes_texto),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("A", fontSize = 12.sp)
            Slider(
                value = 0.5f, // Valor Fixo - TODO
                onValueChange = { }, // Ação Vazia - TODO
                modifier = Modifier.weight(1f)
            )
            Text("A", fontSize = 20.sp)
        }
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_tela_cheia)) },
            trailingContent = {
                Switch(
                    checked = false, // Valor Fixo - TODO
                    onCheckedChange = { } // Ação Vazia - TODO
                )
            }
        )
    }
}

@Composable
private fun ContactSection() {
    val context = LocalContext.current
    val noAppEmail = stringResource(R.string.intent_helper_nenhum_app_email)
    val noBrowser = stringResource(R.string.intent_helper_nenhum_navegador)

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            stringResource(R.string.drawer_contato),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
            color = MaterialTheme.colorScheme.primary
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_enviar_sugestao)) },
            leadingContent = {
                Icon(Icones.Email, contentDescription = stringResource(R.string.drawer_contato_email))
            },
            modifier = Modifier.clickable {
                IntentHelper.enviarEmail(
                    context = context,
                    email = "luizcmarin@gmail.com",
                    assunto = "Contato - App Catfeina v${BuildConfig.VERSION_NAME}",
                    mensagemErro = noAppEmail
                )
            }
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_saiba_mais_jw)) },
            leadingContent = {
                Icon(Icones.Language, contentDescription = stringResource(R.string.drawer_link_jw))
            },
            modifier = Modifier.clickable { 
                IntentHelper.abrirLink(
                    context, 
                    "https://jw.org", 
                    mensagemErro = noBrowser
                ) 
            }
        )
    }
}

@Composable
private fun PrivacySection() {
    var diagnosticEnabled by remember { mutableStateOf(false) }
    var usageEnabled by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            stringResource(R.string.drawer_privacidade),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.drawer_privacidade_descricao),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_enviar_diagnostico)) },
            supportingContent = { Text(stringResource(R.string.drawer_enviar_diagnostico_descricao)) },
            trailingContent = {
                Switch(
                    checked = diagnosticEnabled,
                    onCheckedChange = { diagnosticEnabled = it })
            }
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_enviar_uso)) },
            supportingContent = { Text(stringResource(R.string.drawer_enviar_uso_descricao)) },
            trailingContent = {
                Switch(
                    checked = usageEnabled,
                    onCheckedChange = { usageEnabled = it })
            }
        )
    }
}

@Composable
private fun AboutSection() {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
    ) {
        Text(
            stringResource(R.string.drawer_sobre),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )
        ListItem(
            headlineContent = {
                Text(
                    stringResource(R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            supportingContent = {
                Text(
                    "Versão: ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE}) - ${
                        BuildConfig.BUILD_TIME.substringBefore(
                            " "
                        )
                    }"
                )
            }
        )
        AnimatedAsset(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            assetName = "cat_carregando.lottie"
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.drawer_sobre_descricao_longa),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun LegalSection(
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            stringResource(R.string.drawer_legal),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.drawer_privacidade_descricao),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_termos_uso)) },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Screen.Informativo.createRoute("termos_de_uso"))
                }
            }
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_politica_privacidade)) },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Screen.Informativo.createRoute("politica_de_privacidade"))
                }
            }
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_isencao_responsabilidade)) },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Screen.Informativo.createRoute("isencao-de-responsabilidade"))
                }
            }
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_declaracao_conformidade)) },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Screen.Informativo.createRoute("declaracao-de-conformidade"))
                }
            }
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_aviso_conteudo)) },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Screen.Informativo.createRoute("aviso-de-conteudo"))
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.drawer_direitos_reservados),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DebugSection(
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            stringResource(R.string.drawer_debug),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
            color = MaterialTheme.colorScheme.primary
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.drawer_menu_depuracao)) },
            leadingContent = {
                Icon(Icones.Bug, contentDescription = stringResource(R.string.drawer_menu_depuracao))
            },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Screen.Debug.route)
                }
            }
        )
    }
}


/**
 * Um componente de botão segmentado customizado para seleção de opções.
 */
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
