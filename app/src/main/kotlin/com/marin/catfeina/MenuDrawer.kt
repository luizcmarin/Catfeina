/*
 *  Projeto: Catfeina
 *  Arquivo: MenuDrawer.kt
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

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DividerDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.marin.catfeina.core.ui.CatAnimation
import com.marin.catfeina.core.utils.Icones
import com.marin.catfeina.ui.temas.TemasViewModel
import kotlinx.coroutines.launch

@Composable
fun MenuDrawerContent(
    navController: NavController,
    currentRoute: String?,
    temasViewModel: TemasViewModel,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp)
        ) {
            item { DrawerHeader(onCloseDrawer) }
            item {
                NavigationDrawerSection(
                    navController = navController,
                    currentRoute = currentRoute,
                    onCloseDrawer = onCloseDrawer
                )
            }
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp)) }

            item { ThemeSettingsSection(temasViewModel) }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
            }
            item { TextSettingsSection(temasViewModel) }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
            }
            item { PrivacySection() }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
            }
            item { ContactSection() }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
            }
            item {
                LegalSection(
                    navController = navController,
                    onCloseDrawer = onCloseDrawer
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
            }
            item { AboutSection() }
            item { Spacer(Modifier.height(24.dp)) }
        }
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
            Icon(Icones.Voltar, contentDescription = "Voltar")
        }
        Spacer(Modifier.width(16.dp))
        Icon(
            Icones.Diamante,
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text("Catfeina", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun NavigationDrawerSection(
    navController: NavController,
    currentRoute: String?,
    onCloseDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        drawerNavItems.forEach { screen ->
            NavigationDrawerItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    scope.launch {
                        onCloseDrawer() // Fecha o drawer
                        // Navega para a tela clicada
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
private fun ThemeSettingsSection(viewModel: TemasViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Convertemos o mapa para uma lista para ter uma ordem estável
    val themes = uiState.temasDisponiveis.entries.toList()

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("Tema e Aparência", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))

        ListItem(
            headlineContent = { Text("Modo Escuro") },
            trailingContent = {
                Switch(
                    checked = uiState.isDarkMode,
                    onCheckedChange = { viewModel.onDarkModeChange(it) }
                )
            }
        )

        Spacer(Modifier.height(12.dp))

        // --- Início do Grupo de Botões do tema ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            themes.forEachIndexed { index, (themeKey, _) ->
                val isSelected = uiState.temaAtualKey == themeKey

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                            else Color.Transparent
                        )
                        .clickable { viewModel.onThemeSelected(themeKey) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = themeKey.name.take(3), // Ex: VER, OUT, INV, PRI
                        color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }

                // Adiciona uma divisória vertical entre os botões
                if (index < themes.size - 1) {
                    VerticalDivider(
                        modifier = Modifier.height(32.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
        // --- Fim do Grupo de Botões ---
    }
}

@Composable
private fun TextSettingsSection(viewModel: TemasViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            "Configurações de texto",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("A", fontSize = 12.sp)
            Slider(
                value = uiState.textSize,
                onValueChange = { viewModel.onTextSizeChange(it) },
                modifier = Modifier.weight(1f)
            )
            Text("A", fontSize = 20.sp)
        }
        ListItem(
            headlineContent = { Text("Tela cheia") },
            trailingContent = {
                Switch(
                    checked = uiState.isFullScreen,
                    onCheckedChange = { viewModel.onFullScreenChange(it) }
                )
            }
        )
    }
}

@Composable
private fun ContactSection() {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            "CONTATO",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        ListItem(
            headlineContent = { Text("Enviar uma sugestão") },
            leadingContent = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = "E-mail"
                )
            },
            modifier = Modifier.clickable {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data =
                        "mailto:".toUri() // Only email apps should handle this
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("luizcmarin@gmail.com"))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        "Contato - App Catfeina v${BuildConfig.VERSION_NAME}"
                    )
                }
                try {
                    context.startActivity(emailIntent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Nenhum aplicativo de e-mail encontrado.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        ListItem(
            headlineContent = { Text("Saiba mais em jw.org") },
            leadingContent = {
                Icon(
                    Icons.Filled.Language,
                    contentDescription = "jw.org"
                )
            },
            modifier = Modifier.clickable {
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    "https://jw.org".toUri()
                )
                try {
                    context.startActivity(webIntent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Nenhum navegador encontrado para abrir o link.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
            "PRIVACIDADE",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Algumas informações essenciais precisam ser transferidas entre nós e o seu dispositivo para que o aplicativo funcione corretamente. Nenhuma dessas informações vai ser vendida ou usada para fazer propaganda.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ListItem(
            headlineContent = { Text("Enviar informações de diagnóstico") },
            supportingContent = { Text("Este aplicativo coleta informações de diagnóstico e uso para melhorar sua experiência. Você pode escolher se deseja compartilhar esses dados. Nenhuma informação será vendida ou usada para fins publicitários.") },
            trailingContent = {
                Switch(checked = diagnosticEnabled, onCheckedChange = { diagnosticEnabled = it })
            }
        )
        ListItem(
            headlineContent = { Text("Enviar informações de uso") },
            supportingContent = { Text("Ajuda-nos a melhorar o design, o desempenho e a estabilidade.") },
            trailingContent = {
                Switch(checked = usageEnabled, onCheckedChange = { usageEnabled = it })
            }
        )
    }
}

@Composable
private fun LegalSection(
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            "LEGAL",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Algumas informações essenciais podem ser transferidas entre nós e o seu dispositivo para que o aplicativo funcione corretamente. Nenhuma dessas informações vai ser vendida ou usada para fazer propaganda.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ListItem(
            headlineContent = { Text("Termos de uso") },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(AppScreenRoutes.informativoDetail("termos_de_uso"))
                }
            }
        )
        ListItem(
            headlineContent = { Text("Política de privacidade") },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(AppScreenRoutes.informativoDetail("politica_de_privacidade"))
                }
            }
        )
        ListItem(
            headlineContent = { Text("Aviso de Isenção de Responsabilidade") },
            modifier = Modifier.clickable {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(AppScreenRoutes.informativoDetail("aviso-isencao-responsabilidade"))
                }
            }
        )
        ListItem(
            headlineContent = { Text("Licenças de código aberto") },
            modifier = Modifier.clickable {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
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
            "SOBRE",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )
        ListItem(
            headlineContent = { Text("Catfeina", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            supportingContent = {
                // Use os valores dinâmicos do BuildConfig
                Text(
                    "Versão: ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE}) - ${
                        BuildConfig.BUILD_TIME.substringBefore(
                            " "
                        )
                    }"
                )
            }
        )
        CatAnimation(
            modifier = Modifier.size(200.dp),
            assetName = "cat_carregando.lottie",
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "As obras aqui expostas têm um valor artístico, literário, baseado nas crenças das Testemunhas de Jeová. Seu objetivo é enaltecer a Jeová, o verdadeiro Deus e legítimo Soberano Universal, fortalecer a esperança em Suas maravilhosas e incomparáveis promessas, ocupar nossos pensamentos com “tudo que é verdadeiro, tudo que é de séria preocupação, tudo que é justo, tudo que é casto, tudo que é amável, tudo de que se fala bem, tudo que é virtuoso e tudo que é digno de louvor.” (Fil 4:8) A glória por qualquer benefício daqui derivado cabe a Jeová, o dador de todo presente perfeito, inclusive nossa capacidade de pensar e amar.\n" +
                    "Todos os personagens usados nesta obra são fictícios. Os nomes referem-se às raças de gatos conhecidas, não de pessoas.\n" +
                    "Obra produzida sem fins lucrativos.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "© 2025 Marin. Todos os direitos reservados.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))
    }
}
