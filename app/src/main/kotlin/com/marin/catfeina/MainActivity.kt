/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/MainActivity.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Ponto de entrada principal (Activity) da aplicação.
*
*/
package com.marin.catfeina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.ui.GlobalUiEventManager
import com.marin.catfeina.ui.TemaViewModel
import com.marin.core.tema.CatfeinaTema
import com.marin.core.tema.ModoNoturno
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var globalUiEventManager: GlobalUiEventManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: TemaViewModel = hiltViewModel()
            val estado by viewModel.uiState.collectAsStateWithLifecycle()

            val usarModoEscuro = when (estado.modoNoturno) {
                ModoNoturno.CLARO -> false
                ModoNoturno.ESCURO -> true
                ModoNoturno.SISTEMA -> isSystemInDarkTheme()
            }

            CatfeinaTema(
                chaveTema = estado.tema.chave,
                useDarkTheme = usarModoEscuro
            ) {
                CatfeinaApp(globalUiEventManager = globalUiEventManager)
            }
        }
    }
}
