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

package com.marin.catfeina.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MilitaryTech
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.VisibilityOff

object Icones {
    // 🎧 Player de TTS (Text-to-Speech)
    val ReiniciarTTS = Icons.Filled.Replay
    val ContinuarTTS = Icons.Filled.PlayArrow
    val PausarTTS = Icons.Filled.Pause
    val TocarTTS = Icons.Filled.Headset
    val PararTTS = Icons.Filled.Stop

    // 🗂️ Navegação e Interface
    val Inicio = Icons.Filled.Home
    val Menu = Icons.Filled.Menu
    val Voltar = Icons.AutoMirrored.Filled.ArrowBack
    val Pesquisa = Icons.Filled.Search
    val Mais = Icons.Filled.Add
    val Menos = Icons.Filled.Remove
    val Atualizar = Icons.Filled.Sync
    val Prefacio = Icons.Filled.Description

    // 🧩 Conteúdo
    val Atelier = Icons.Outlined.AutoStories
    val Personagem = Icons.Outlined.Pets
    val Poesia = Icons.Outlined.Pets

    // 🧠 Acessibilidade e Idioma
    val Idioma = Icons.Filled.Translate
    val QualidadeVozBoa = Icons.Filled.NetworkWifi
    val QualidadeVozPadrao = Icons.Filled.SignalWifiOff
    val QualidadeVozDesconhecida = Icons.Filled.PhoneAndroid

    // 🧹 Ações e Utilidades
    val Check = Icons.Filled.Check
    val Excluir = Icons.Filled.Delete
    val Lixeira = Icons.Filled.Delete
    val Deletar = Icons.Filled.Delete
    val Copiar = Icons.Outlined.ContentCopy
    val Compartilhar = Icons.Default.Share
    val QrCode = Icons.Filled.QrCode
    val Salvar = Icons.Outlined.Check

    // 📌 Marcadores e Favoritos
    val MarcadorVazio = Icons.Filled.BookmarkBorder
    val MarcadorCheio = Icons.Filled.Bookmark
    val FavoritoVazio = Icons.Filled.Favorite
    val FavoritoCheio = Icons.Filled.FavoriteBorder

    // 👁️ Visibilidade e Leitura
    val Ver = Icons.Filled.Visibility
    val NaoVer = Icons.Filled.VisibilityOff
    val JaLido = Icons.Filled.Visibility
    val NaoLido = Icons.Outlined.VisibilityOff

    // 🎨 Personalização e Aparência
    val PaletaDeCores = Icons.Filled.Palette
    val Temas = Icons.Filled.Palette
    val Lampada = Icons.Filled.Lightbulb

    // 🔐 Privacidade e Informações
    val Privacidade = Icons.Filled.Policy
    val Sobre = Icons.Filled.Info

    // 🏆 Conquistas e Recompensas
    val Conquista = Icons.Filled.MilitaryTech
    val Diamante = Icons.Filled.Diamond
    val Like = Icons.Default.ThumbUp

    // 🖼️ Recursos Visuais
    val SemImagem = Icons.Filled.BrokenImage
}