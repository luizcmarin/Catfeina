/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/debug/DebugViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela de depuração.
*
*/
package com.marin.catfeina.ui.telas.debug

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.data.models.Poesia
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.catfeina.data.repositories.UserPreferencesRepository
import com.marin.catfeina.usecases.GetDbStatsUseCase
import com.marin.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.commonmark.node.Heading
import org.commonmark.node.Image
import org.commonmark.node.Node
import org.commonmark.node.Paragraph
import org.commonmark.parser.Parser
import java.io.File
import javax.inject.Inject

data class DebugUiState(
    val buildInfo: List<Pair<String, String>> = emptyList(),
    val dbStats: List<Pair<String, Long>> = emptyList(),
    val imageDiagnostics: DebugImageUiState = DebugImageUiState(),
    val poesiasPaginadas: Flow<PagingData<PoesiaStatus>> = flowOf(PagingData.empty())
)

data class DebugImageUiState(
    val imagePath: String = "",
    val imageCount: Int = 0
)

data class PoesiaStatus(
    val id: Long,
    val titulo: String,
    val imagem: String?,
    val caminhoCompleto: String,
    val existe: Boolean
)

@HiltViewModel
class DebugViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getDbStatsUseCase: GetDbStatsUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val poesiaRepository: PoesiaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebugUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadBuildInfo()
        loadDbStats()
        verificarImagens()
    }

    fun forcarRessincronizacaoCompleta() {
        viewModelScope.launch {
            userPreferencesRepository.limparRegistrosSincronizacao()
        }
    }

    private fun verificarImagens() {
        viewModelScope.launch {
            val imagesDir = File(context.filesDir, "images")
            val allImages = imagesDir.listFiles { _, name -> name.endsWith(".webp") } ?: emptyArray()

            val poesiasPaginadas = poesiaRepository.getPoesiasPaginadas().map { pagingData ->
                pagingData.map { poesia ->
                    val (titulo, imagem) = parseMarkdown(poesia.texto)
                    val imageFile = File(imagesDir, imagem ?: "")
                    PoesiaStatus(
                        id = poesia.id,
                        titulo = titulo ?: "Poesia #${poesia.id}",
                        imagem = imagem,
                        caminhoCompleto = imageFile.absolutePath,
                        existe = imageFile.exists()
                    )
                }
            }

            _uiState.update {
                it.copy(
                    imageDiagnostics = DebugImageUiState(
                        imagePath = imagesDir.absolutePath,
                        imageCount = allImages.size
                    ),
                    poesiasPaginadas = poesiasPaginadas
                )
            }
        }
    }

    private fun parseMarkdown(markdown: String): Pair<String?, String?> {
        val parser = Parser.builder().build()
        val document = parser.parse(markdown)
        var title: String? = null
        var imageUrl: String? = null
        var node: Node? = document.firstChild
        while (node != null) {
            if (node is Heading && node.level == 1 && title == null) {
                title = (node.firstChild as? org.commonmark.node.Text)?.literal
            }
            if (node is Paragraph) {
                var pChild = node.firstChild
                while (pChild != null) {
                    if (pChild is Image) {
                        imageUrl = pChild.destination
                        break
                    }
                    pChild = pChild.next
                }
            }
            if (title != null && imageUrl != null) break
            node = node.next
        }
        return Pair(title, imageUrl)
    }


    private fun loadBuildInfo() {
        _uiState.update {
            it.copy(
                buildInfo = listOf(
                    "Build Type" to BuildConfig.BUILD_TYPE,
                    "Version Name" to BuildConfig.VERSION_NAME,
                    "Version Code" to BuildConfig.VERSION_CODE.toString(),
                    "Build Time" to BuildConfig.BUILD_TIME,
                    "Sync URL" to BuildConfig.SYNC_URL
                )
            )
        }
    }

    private fun loadDbStats() {
        viewModelScope.launch {
            getDbStatsUseCase().collectLatest { result ->
                if (result is UiState.Success) {
                    val stats = result.data
                    val statsList = listOf(
                        "Poesias" to stats.totalPoesias,
                        "Ateliers" to stats.totalAteliers,
                        "Históricos" to stats.totalHistoricos,
                        "Informativos" to stats.totalInformativos
                    )
                    _uiState.update { it.copy(dbStats = statsList) }
                }
            }
        }
    }
}
