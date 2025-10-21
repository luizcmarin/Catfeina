/*
 *  Projeto: Catfeina
 *  Arquivo: TextoFormatadoViewModel.kt
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

package com.marin.catfeina.core.formatador

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.marin.catfeina.core.formatador.parser.ParserTextoFormatado
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextoFormatadoViewModel @Inject constructor(
    val parser: ParserTextoFormatado
) : ViewModel() {

    private val _elementos = mutableStateOf<List<ElementoConteudo>>(emptyList())
    val elementos: State<List<ElementoConteudo>> = _elementos

    fun parsearTexto(textoCru: String) {
        _elementos.value = parser.parse(textoCru)
    }
}
