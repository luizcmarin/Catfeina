/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/splash/SplashScreen.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: Tela de inicialização que gerencia a verificação de atualizações e a navegação inicial.
 *
 */
package com.marin.catfeina.ui.telas.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.ui.componentes.UpdateDialog
import com.marin.core.R
import com.marin.core.ui.CatfeinaLogoAnimation
import com.marin.core.ui.UiState

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appUpdateInfo by viewModel.appUpdateInfo.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            onNavigateToMain()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CatfeinaLogoAnimation(
                modifier = Modifier.size(200.dp),
                drawableId = R.drawable.catfeina_logo_avd
            )

            if (uiState is UiState.Loading) {
                val message = (uiState as UiState.Loading).message
                if (message != null) {
                    Text(text = message, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }

    appUpdateInfo?.let {
        UpdateDialog(
            updateInfo = it,
            onDismiss = { viewModel.onUpdateDialogDismissed() }
        )
    }
}
