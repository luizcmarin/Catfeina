/*
Projeto: Catfeina
Arquivo: configuracoes_viewmodel_test.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Testes unitários para o ConfiguracoesViewModel, verificando a lógica de carregar
e salvar as preferências do usuário, como o modo de tema e o tamanho da fonte.
*/

import 'package:catfeina/app/data/repositories/i_preferencias_repository.dart';
import 'package:catfeina/app/features/configuracoes/configuracoes_state.dart';
import 'package:catfeina/app/features/configuracoes/configuracoes_viewmodel.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/annotations.dart';
import 'package:mockito/mockito.dart';

import 'configuracoes_viewmodel_test.mocks.dart';

@GenerateMocks([IPreferenciasRepository])
void main() {
  late MockIPreferenciasRepository mockPreferenciasRepository;
  late ConfiguracoesViewModel viewModel;

  group('ConfiguracoesViewModel Tests', () {
    setUp(() {
      mockPreferenciasRepository = MockIPreferenciasRepository();
      // Configuração padrão para todos os testes do grupo
      when(mockPreferenciasRepository.getInt(any)).thenAnswer((_) async => null);
      when(mockPreferenciasRepository.getString(any)).thenAnswer((_) async => null);
    });

    test('deve carregar o tema salvo corretamente', () async {
      // Arrange
      when(mockPreferenciasRepository.getInt(keyThemeMode))
          .thenAnswer((_) async => ThemeMode.dark.index);
      
      // Act
      viewModel = ConfiguracoesViewModel(mockPreferenciasRepository);
      
      // Assert
      await Future.delayed(Duration.zero);
      expect(viewModel.debugState.status, Status.success);
      expect(viewModel.debugState.themeMode, ThemeMode.dark);
    });

    test('deve salvar o novo tema selecionado', () async {
      // Arrange
      viewModel = ConfiguracoesViewModel(mockPreferenciasRepository);
      const novoTema = ThemeMode.light;

      // Act
      await viewModel.mudarTema(novoTema);

      // Assert
      verify(mockPreferenciasRepository.setInt(keyThemeMode, novoTema.index)).called(1);
      expect(viewModel.debugState.themeMode, novoTema);
    });

    test('deve carregar o fator de escala de texto salvo corretamente', () async {
      // Arrange
      when(mockPreferenciasRepository.getString(keyTextScaleFactor))
          .thenAnswer((_) async => '1.2');
      
      // Act
      viewModel = ConfiguracoesViewModel(mockPreferenciasRepository);
      
      // Assert
      await Future.delayed(Duration.zero);
      expect(viewModel.debugState.status, Status.success);
      expect(viewModel.debugState.fatorEscalaTexto, 1.2);
    });

    test('deve salvar o novo fator de escala de texto', () async {
      // Arrange
      viewModel = ConfiguracoesViewModel(mockPreferenciasRepository);
      const novoFator = 1.5;

      // Act
      await viewModel.mudarEscalaTexto(novoFator);

      // Assert
      verify(mockPreferenciasRepository.setString(keyTextScaleFactor, novoFator.toString())).called(1);
      expect(viewModel.debugState.fatorEscalaTexto, novoFator);
    });
  });
}
