/*
Projeto: Catfeina
Arquivo: configuracoes_state.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Define a classe de estado para a tela de configurações, gerenciando as
opções de personalização do usuário, como o tema e o tamanho da fonte do aplicativo.
*/

import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';

enum Status { initial, loading, success, failure }

class ConfiguracoesState extends Equatable {
  final Status status;
  final ThemeMode themeMode;
  final double fatorEscalaTexto;
  final String? errorMessage;

  const ConfiguracoesState({
    this.status = Status.initial,
    this.themeMode = ThemeMode.system,
    this.fatorEscalaTexto = 1.0,
    this.errorMessage,
  });

  ConfiguracoesState copyWith({
    Status? status,
    ThemeMode? themeMode,
    double? fatorEscalaTexto,
    String? errorMessage,
  }) {
    return ConfiguracoesState(
      status: status ?? this.status,
      themeMode: themeMode ?? this.themeMode,
      fatorEscalaTexto: fatorEscalaTexto ?? this.fatorEscalaTexto,
      errorMessage: errorMessage ?? this.errorMessage,
    );
  }

  @override
  List<Object?> get props => [status, themeMode, fatorEscalaTexto, errorMessage];
}
