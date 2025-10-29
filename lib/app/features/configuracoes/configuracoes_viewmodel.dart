/*
Projeto: Catfeina
Arquivo: configuracoes_viewmodel.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Define o ViewModel para a tela de configurações, responsável por carregar e salvar
as preferências do usuário, como o modo de tema e o tamanho da fonte.
*/

import 'package:catfeina/app/core/di/data_providers.dart';
import 'package:catfeina/app/data/repositories/i_preferencias_repository.dart';
import 'package:catfeina/app/features/configuracoes/configuracoes_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

const String keyThemeMode = 'theme_mode';
const String keyTextScaleFactor = 'text_scale_factor';

final configuracoesViewModelProvider =
    StateNotifierProvider.autoDispose<ConfiguracoesViewModel, ConfiguracoesState>(
        (ref) {
  final preferenciasRepository = ref.watch(preferenciasRepositoryProvider);
  return ConfiguracoesViewModel(preferenciasRepository);
});

class ConfiguracoesViewModel extends StateNotifier<ConfiguracoesState> {
  final IPreferenciasRepository _preferenciasRepository;

  ConfiguracoesViewModel(this._preferenciasRepository)
      : super(const ConfiguracoesState()) {
    _carregarPreferencias();
  }

  Future<void> _carregarPreferencias() async {
    try {
      state = state.copyWith(status: Status.loading);
      // Carrega o tema
      final themeIndex = await _preferenciasRepository.getInt(keyThemeMode);
      final themeMode = ThemeMode.values[themeIndex ?? ThemeMode.system.index];

      // Carrega o fator de escala de texto
      final textScaleStr = await _preferenciasRepository.getString(keyTextScaleFactor);
      final fatorEscalaTexto = double.tryParse(textScaleStr ?? '1.0') ?? 1.0;

      state = state.copyWith(
        status: Status.success,
        themeMode: themeMode,
        fatorEscalaTexto: fatorEscalaTexto,
      );
    } catch (e) {
      state = state.copyWith(status: Status.failure, errorMessage: e.toString());
    }
  }

  Future<void> mudarTema(ThemeMode novoTema) async {
    if (state.themeMode == novoTema) return;

    try {
      await _preferenciasRepository.setInt(keyThemeMode, novoTema.index);
      state = state.copyWith(themeMode: novoTema);
    } catch (e) {
      state = state.copyWith(errorMessage: e.toString());
    }
  }

  Future<void> mudarEscalaTexto(double novoFator) async {
    // Arredonda para 1 casa decimal para evitar valores excessivamente precisos
    final fatorArredondado = (novoFator * 10).round() / 10;
    if (state.fatorEscalaTexto == fatorArredondado) return;

    try {
      await _preferenciasRepository.setString(keyTextScaleFactor, fatorArredondado.toString());
      state = state.copyWith(fatorEscalaTexto: fatorArredondado);
    } catch (e) {
      state = state.copyWith(errorMessage: e.toString());
    }
  }
}
