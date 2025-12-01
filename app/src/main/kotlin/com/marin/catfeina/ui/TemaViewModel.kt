/*
 *  Projeto: Catfeina/Catverso
 *  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/TemaViewModel.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota: ViewModel responsável por gerenciar o estado do tema da aplicação.
 *
 */
package com.marin.catfeina.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.core.tema.CatalogoTemas
import com.marin.core.tema.ChaveTema
import com.marin.core.tema.ModoNoturno
import com.marin.core.tema.PreferenciasDeTemaRepository
import com.marin.core.tema.Tema
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TemaUiState(
    val tema: Tema = CatalogoTemas.obter(ChaveTema.PRIMAVERA),
    val modoNoturno: ModoNoturno = ModoNoturno.SISTEMA,
    val escalaFonte: Float = 1.0f,
    val catalogo: List<Tema> = emptyList()
)

@HiltViewModel
class TemaViewModel @Inject constructor(
    private val prefs: PreferenciasDeTemaRepository
) : ViewModel() {

    val uiState: StateFlow<TemaUiState> = combine(
        prefs.chaveTema,
        prefs.modoNoturno,
        prefs.escalaFonte
    ) { chave, modo, escala ->
        TemaUiState(
            tema = CatalogoTemas.obter(chave),
            modoNoturno = modo,
            escalaFonte = escala,
            catalogo = CatalogoTemas.obterTodos()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TemaUiState(catalogo = CatalogoTemas.obterTodos())
    )

    fun selecionarTema(chave: ChaveTema) {
        viewModelScope.launch {
            prefs.setChaveTema(chave)
        }
    }

    fun definirModoNoturno(modo: ModoNoturno) {
        viewModelScope.launch {
            prefs.setModoNoturno(modo)
        }
    }

    fun definirEscalaFonte(escala: Float) {
        viewModelScope.launch {
            prefs.setEscalaFonte(escala)
        }
    }
}
