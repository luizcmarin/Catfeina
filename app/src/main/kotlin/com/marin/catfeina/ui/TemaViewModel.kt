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
 *  Ele reside no :app para usar as anotações do Hilt, mas depende de interfaces
 *  do :core para manter o desacoplamento da lógica de negócio.
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

// Representa o estado completo da UI relacionado ao tema.
data class TemaUiState(
    val tema: Tema = CatalogoTemas.obter(ChaveTema.PRIMAVERA),
    val modoNoturno: ModoNoturno = ModoNoturno.SISTEMA, // Lógica atualizada para 3 estados
    val catalogo: List<Tema> = emptyList()
)

@HiltViewModel
class TemaViewModel @Inject constructor(
    private val prefs: PreferenciasDeTemaRepository
) : ViewModel() {

    // Combina os fluxos de dados do repositório para criar um único estado de UI.
    val uiState: StateFlow<TemaUiState> = combine(
        prefs.chaveTema,
        prefs.modoNoturno // Observa o novo fluxo de ModoNoturno
    ) { chave, modo ->
        TemaUiState(
            tema = CatalogoTemas.obter(chave),
            modoNoturno = modo,
            catalogo = CatalogoTemas.obterTodos()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TemaUiState(catalogo = CatalogoTemas.obterTodos())
    )

    /**
     * Persiste a nova chave de tema selecionada pelo usuário.
     */
    fun selecionarTema(chave: ChaveTema) {
        viewModelScope.launch {
            prefs.setChaveTema(chave)
        }
    }

    /**
     * Persiste a preferência de modo noturno selecionada pelo usuário.
     */
    fun definirModoNoturno(modo: ModoNoturno) {
        viewModelScope.launch {
            prefs.setModoNoturno(modo)
        }
    }
}
