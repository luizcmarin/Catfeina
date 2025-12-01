/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/telas/poesias/PoesiasViewModel.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: ViewModel para a tela de listagem de poesias.
*
*/
package com.marin.catfeina.ui.telas.poesias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.marin.catfeina.ui.telas.inicio.PoesiaUiModel
import com.marin.catfeina.data.repositories.PoesiaRepository
import com.marin.catfeina.usecases.GetPoesiasPaginadasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PoesiasViewModel @Inject constructor(
    getPoesiasPaginadasUseCase: GetPoesiasPaginadasUseCase,
    private val repository: PoesiaRepository
) : ViewModel() {

    val poesias: Flow<PagingData<PoesiaUiModel>> = getPoesiasPaginadasUseCase()
        .map { pagingData ->
            pagingData.map { poesia ->
                PoesiaUiModel(
                    id = poesia.id,
                    titulo = repository.extrairTitulo(poesia),
                    resumo = repository.extrairResumo(poesia),
                    imagem = repository.extrairImagemPrincipal(poesia)
                )
            }
        }
        .cachedIn(viewModelScope)
}
