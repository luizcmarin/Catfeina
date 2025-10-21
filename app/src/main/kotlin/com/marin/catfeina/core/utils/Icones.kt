/*
 *  Projeto: Catfeina
 *  Arquivo: Icones.kt
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
 *  Arquivo: Icones.kt
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
 * //  Arquivo: Icones.kt
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
// Arquivo: Icones.kt
// Descrição: Objeto central que contém todas as referências aos ícones do
//            Material Design utilizados no aplicativo. Facilita a consistência
//            e a manutenção dos ícones em um único local.
// ===================================================================================

package com.marin.catfeina.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Pets

object Icones {
    // Ícones para o Player de TTS (Text-to-Speech)
    val ReiniciarTTS = Icons.Filled.Replay
    val ContinuarTTS = Icons.Filled.PlayArrow
    val PausarTTS = Icons.Filled.Pause
    val TocarTTS = Icons.Filled.Headset
    val PararTTS = Icons.Filled.Stop

    val Atelier = Icons.Outlined.AutoStories
    val Categoria = Icons.Filled.Category
    val Check = Icons.Filled.Check
    val QualidadeVozBoa = Icons.Filled.NetworkWifi
    val QualidadeVozPadrao = Icons.Filled.SignalWifiOff
    val QualidadeVozDesconhecida = Icons.Filled.PhoneAndroid
    val Idioma = Icons.Filled.Translate
    val FavoritoCheio = Icons.Filled.FavoriteBorder
    val Dinheiro = Icons.Filled.LocalAtm
    val Excluir = Icons.Filled.Delete
    val FavoritoVazio = Icons.Filled.Favorite
    val Inicio = Icons.Filled.Home
    val Lampada = Icons.Filled.Lightbulb
    val Lixeira = Icons.Filled.Delete
    val Mais = Icons.Filled.Add
    val Menos = Icons.Filled.Remove
    val Menu = Icons.Filled.Menu
    val NaoVer = Icons.Filled.VisibilityOff
    val PaletaDeCores = Icons.Filled.Palette
    val Personagem = Icons.Outlined.Pets
    val Poesia = Icons.Outlined.Pets
    val Privacidade = Icons.Filled.Policy
    val QrCode = Icons.Filled.QrCode
    val SemImagem = Icons.Filled.BrokenImage
    val Sobre = Icons.Filled.Info
    val Temas = Icons.Filled.Palette
    val TresPontosVertical = Icons.Filled.MoreVert
    val Ver = Icons.Filled.Visibility
    val Voltar = Icons.AutoMirrored.Filled.ArrowBack
    val MarcadorVazio = Icons.Filled.BookmarkBorder
    val MarcadorCheio = Icons.Filled.Bookmark
    val Deletar = Icons.Filled.Delete
    val Pesquisa = Icons.Filled.Search
    val SomAmbiente = Icons.AutoMirrored.Filled.VolumeUp
    val Conquista = Icons.Filled.MilitaryTech
    val Diamante = Icons.Filled.Diamond
    val Atualizar = Icons.Filled.Sync
    val JaLido = Icons.Filled.Visibility
}
