/*
*  Projeto: Catfeina/Catverso
*  Arquivo: core/src/main/kotlin/com/marin/core/util/CatKit.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Arquivo consolidado de utilitários, extensões e helpers para o projeto.
*  Organizado em categorias para facilitar a manutenção e reutilização.
*/

package com.marin.core.util

import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.marin.core.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// =============================================
//  Utilitários de UI (Compose UI)
// =============================================

/**
 * Exibe um Toast na tela a partir de um Composable.
 */
@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

/**
 * Renderiza uma String de texto em Markdown como texto formatado.
 */
@Composable
fun RenderizadorMarkdown(modifier: Modifier = Modifier, markdown: String) {
    val parser = Parser.builder().build()
    val document = parser.parse(markdown)
    val renderer = HtmlRenderer.builder().build()
    val html = renderer.render(document)

    AndroidView(
        modifier = modifier,
        factory = { TextView(it).apply { movementMethod = LinkMovementMethod.getInstance() } },
        update = { it.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT) }
    )
}

// =============================================
//  Modificadores (Compose Modifiers)
// =============================================

/**
 * Previne cliques duplos ou múltiplos em um curto intervalo de tempo (debounce).
 */
fun Modifier.cliqueSeguro(debounceInterval: Long = 500L, onClick: () -> Unit): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    clickable {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceInterval) {
            lastClickTime = currentTime
            onClick()
        }
    }
}

/**
 * Aplica um efeito de brilho (shimmer) a um Composable. Esta é uma versão aprimorada,
 * inspirada em implementações de referência, que permite maior customização.
 */
fun Modifier.placeholder(
    isLoading: Boolean,
    corDoBrilho: Color = Color.White,
    corDeFundo: Color = Color.LightGray.copy(alpha = 0.6f),
    larguraDoBrilho: Float = 400f,
    duracao: Int = 1000,
    angulo: Float = 20f
): Modifier = composed {
    if (!isLoading) return@composed this

    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmerTransition")

    val anguloRad = Math.toRadians(angulo.toDouble())
    val xShimmer = (size.width + larguraDoBrilho) * cos(anguloRad).toFloat()
    val yShimmer = (size.height + larguraDoBrilho) * sin(anguloRad).toFloat()

    val startOffsetX by transition.animateFloat(
        initialValue = -xShimmer,
        targetValue = xShimmer,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = duracao)),
        label = "shimmerStartOffsetX"
    )

    val startOffsetY by transition.animateFloat(
        initialValue = -yShimmer,
        targetValue = yShimmer,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = duracao)),
        label = "shimmerStartOffsetY"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                corDeFundo,
                corDoBrilho.copy(alpha = 0.4f),
                corDeFundo
            ),
            start = Offset(startOffsetX - xShimmer / 2, startOffsetY - yShimmer / 2),
            end = Offset(startOffsetX + xShimmer / 2, startOffsetY + yShimmer / 2)
        )
    ).onGloballyPositioned { size = it.size }
}


// =============================================
//  Utilitários de Coroutines e Dados
// =============================================

/**
 * Executa uma query de banco de dados de forma segura, retornando um UiState.
 */
suspend fun <T> safeQuery(query: suspend () -> T): UiState<T> {
    return withContext(Dispatchers.IO) {
        try {
            UiState.Success(query())
        } catch (e: Exception) {
            CatLog.e("Erro ao executar a query no banco de dados", e)
            UiState.Error("Ocorreu um erro ao acessar o banco de dados: ${e.message}")
        }
    }
}

// =============================================
//  Utilitários de Sistema (System Utils)
// =============================================

/**
 * Objeto utilitário para criar e disparar Intents comuns de forma segura.
 * As funções recebem as strings de UI como parâmetros para desacoplar do Context.
 */
object IntentHelper {
    fun abrirLink(context: Context, url: String, mensagemErro: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        dispararIntentSeSeguro(context, intent, mensagemErro)
    }

    fun compartilharTexto(context: Context, texto: String, chooserTitle: String, mensagemErro: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, texto)
        }
        val chooser = Intent.createChooser(intent, chooserTitle)
        dispararIntentSeSeguro(context, chooser, mensagemErro)
    }

    fun enviarEmail(context: Context, email: String, assunto: String = "", mensagemErro: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, assunto)
        }
        dispararIntentSeSeguro(context, intent, mensagemErro)
    }

    private fun dispararIntentSeSeguro(context: Context, intent: Intent, mensagemErro: String) {
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            CatLog.w(mensagemErro)
            Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show()
        }
    }
}

// =============================================
//  Utilitários Gerais (General Utils)
// =============================================

/**
 * Centraliza a formatação de datas usando kotlin.time.Instant.
 */
@OptIn(ExperimentalTime::class)
object ConversorData {
    private const val FORMATO_DATA_PADRAO = "dd/MM/yyyy"
    private const val FORMATO_HORA_PADRAO = "HH:mm"
    private const val FORMATO_DATA_HORA_PADRAO = "dd/MM/yyyy HH:mm"

    private fun formatar(instant: Instant, formato: String): String {
        val formatador = SimpleDateFormat(formato, Locale.getDefault())
        val data = Date(instant.toEpochMilliseconds())
        return formatador.format(data)
    }

    fun formatarData(instant: Instant): String = formatar(instant, FORMATO_DATA_PADRAO)
    fun formatarHora(instant: Instant): String = formatar(instant, FORMATO_HORA_PADRAO)
    fun formatarDataHora(instant: Instant): String = formatar(instant, FORMATO_DATA_HORA_PADRAO)
}

/**
 * Converte um Instant em uma string de tempo relativo (ex: 'agora mesmo', 'há 5 minutos').
 * Esta função foi refatorada para não depender do Context ou de recursos, recebendo
 * os formatos de string e plurais como parâmetros.
 */
@OptIn(ExperimentalTime::class)
fun Instant.toTempoRelativo(
    agoraMesmo: String,
    ontem: String,
    minutosAtras: (minutos: Int) -> String, // Lambda para tratar o plural de minutos
    horasAtras: (horas: Int) -> String      // Lambda para tratar o plural de horas
): String {
    val duracao = Clock.System.now() - this

    return when {
        duracao.inWholeMinutes < 1 -> agoraMesmo
        duracao.inWholeHours < 1 -> {
            val minutos = duracao.inWholeMinutes.toInt()
            minutosAtras(minutos)
        }
        duracao.inWholeDays < 1 -> {
            val horas = duracao.inWholeHours.toInt()
            horasAtras(horas)
        }
        duracao.inWholeDays < 2 -> ontem
        else -> ConversorData.formatarData(this)
    }
}

/**
 * Objeto que centraliza funções de validação de campos de entrada.
 */
object Validadores {
    fun isNotEmpty(text: String): Boolean = text.isNotBlank()
    fun isEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun minLength(text: String, length: Int): Boolean = text.length >= length
    fun isValidPhone(phone: String): Boolean = Patterns.PHONE.matcher(phone).matches()
    fun isValidUrl(url: String): Boolean = Patterns.WEB_URL.matcher(url).matches()

    fun isValidCPF(cpf: String): Boolean {
        val cleanCpf = cpf.replace("\\D".toRegex(), "")
        if (cleanCpf.length != 11 || cleanCpf.all { it == cleanCpf[0] }) return false

        fun calcDigit(cpf: String, factor: Int): Int {
            val sum = (0 until factor - 1).sumOf { i -> (cpf[i].toString().toInt()) * (factor - i) }
            val result = (sum * 10) % 11
            return if (result == 10) 0 else result
        }
        return calcDigit(cleanCpf, 10) == cleanCpf[9].toString().toInt() && calcDigit(cleanCpf, 11) == cleanCpf[10].toString().toInt()
    }

    fun isValidCNPJ(cnpj: String): Boolean {
        val cleanCnpj = cnpj.replace("\\D".toRegex(), "")
        if (cleanCnpj.length != 14 || cleanCnpj.all { it == cleanCnpj[0] }) return false

        val weight1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
        val weight2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

        fun calcDigit(cnpj: String, weight: IntArray): Int {
            val sum = weight.indices.sumOf { i -> (cnpj[i].toString().toInt()) * weight[i] }
            val result = sum % 11
            return if (result < 2) 0 else 11 - result
        }
        return calcDigit(cleanCnpj, weight1) == cleanCnpj[12].toString().toInt() && calcDigit(cleanCnpj, weight2) == cleanCnpj[13].toString().toInt()
    }
}
