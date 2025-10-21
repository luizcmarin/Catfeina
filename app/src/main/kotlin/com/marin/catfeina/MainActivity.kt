/*
 *  Projeto: Catfeina
 *  Arquivo: MainActivity.kt
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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.marin.catfeina.core.temas.CatfeinaTheme
import com.marin.catfeina.ui.main.MainScreenViewModel
import com.marin.catfeina.ui.main.MainViewModel
import com.marin.catfeina.ui.temas.TemasViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val temasViewModel: TemasViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val mainScreenViewModel: MainScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            // Aguarda o estado não estar mais carregando para obter a contagem real
            val uiState = mainScreenViewModel.uiState.first { !it.isLoading }
            if (uiState.poesiasCount == 0L) {
                mainViewModel.iniciarSincronizacao()
            }
        }

        enableEdgeToEdge()
        setContent {
            CatfeinaTheme(gerenciadorTemas = temasViewModel.gerenciadorTemas) {
                CatfeinaApp(temasViewModel)
            }
        }
    }
}
