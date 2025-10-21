/*
 *  Projeto: Catfeina
 *  Arquivo: InformativoScreen.kt
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
 *  Arquivo: InformativoScreen.kt
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
 * //  Arquivo: InformativoScreen.kt
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

package com.marin.catfeina.ui.informativo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marin.catfeina.AppDestinationsArgs
import com.marin.catfeina.R
import com.marin.catfeina.core.ui.CatAnimation
import com.marin.catfeina.core.ui.UiState
import com.marin.catfeina.core.utils.Icones
import com.marin.catfeina.sqldelight.Informativos
import com.marin.catfeina.ui.components.TtsPlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// /////////////////////////////////////////////////////////////////////////////
// ViewModel
// /////////////////////////////////////////////////////////////////////////////

@HiltViewModel
class InformativoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
    // Futuramente, injete aqui seu repositório:
    // private val informativoRepository: InformativoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Informativos>>(UiState.Loading)
    val uiState: StateFlow<UiState<Informativos>> = _uiState

    init {
        val chaveInformativo: String? = savedStateHandle[AppDestinationsArgs.INFORMATIVO_CHAVE_ARG]
        if (chaveInformativo == null) {
            _uiState.value = UiState.Error("Chave do informativo não encontrada.")
        } else {
            // Em um app real, você chamaria o repositório aqui.
            // Por enquanto, vamos simular a busca.
            // val informativo = informativoRepository.getInformativoByChave(chaveInformativo).firstOrNull()
            // if (informativo != null) _uiState.value = UiState.Success(informativo)
            // else _uiState.value = UiState.Error("Informativo não encontrado.")

            // SIMULAÇÃO:
            when (chaveInformativo) {
                "termos_de_uso" -> {
                    _uiState.value = UiState.Success(
                        Informativos(
                            chave = "termos_de_uso",
                            titulo = "Termos de Uso",
                            conteudo = "Este é o conteúdo dos termos de uso do aplicativo Catfeina. Ao usar este aplicativo, você concorda com todos os termos descritos aqui.",
                            dataAtualizacao = System.currentTimeMillis()
                        )
                    )
                }

                "politica_de_privacidade" -> {
                    _uiState.value = UiState.Success(
                        Informativos(
                            chave = "politica_de_privacidade",
                            titulo = "Política de Privacidade",
                            conteudo = "Este é o conteúdo da política de privacidade. Respeitamos a sua privacidade e não compartilhamos seus dados com terceiros para fins comerciais. As informações coletadas são usadas apenas para melhorar o aplicativo.",
                            dataAtualizacao = System.currentTimeMillis()
                        )
                    )
                }

                else -> {
                    _uiState.value =
                        UiState.Error("Informativo '$chaveInformativo' não encontrado.")
                }
            }
        }
    }
}

// /////////////////////////////////////////////////////////////////////////////
// UI Screen
// /////////////////////////////////////////////////////////////////////////////

@Composable
fun InformativoScreen(
    onNavigateBack: () -> Unit,
    viewModel: InformativoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Loading -> {
            CatAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 16.dp),
                animationResId = R.raw.cat_carregando
            )
        }

        is UiState.Success -> {
            InformativoSuccessScreen(
                informativo = state.data,
                onNavigateBack = onNavigateBack
            )
        }

        is UiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InformativoSuccessScreen(
    informativo: Informativos,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(informativo.titulo) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icones.Voltar, contentDescription = "Voltar")
                    }
                }
            )
        },
        // Adicionando o player na BottomBar >>>>>
        bottomBar = {
            TtsPlayerController(
                textToPlay = "${informativo.titulo}. ${informativo.conteudo}",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        // A BottomBar é herdada do Scaffold principal na MainActivity, então não a definimos aqui.
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // TODO: Aplicar formatação customizada ao conteúdo se necessário.
            Text(
                text = informativo.conteudo,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Última atualização: ${formatTimestamp(informativo.dataAtualizacao)}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        "Data inválida"
    }
}