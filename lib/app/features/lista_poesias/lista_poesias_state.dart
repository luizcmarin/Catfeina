/*
Projeto: Catfeina
Arquivo: lista_poesias_state.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Define a classe de estado (State) para a feature da lista de poesias. Esta classe
imutável contém todos os dados necessários para renderizar a UI.
*/

import 'package:equatable/equatable.dart';

enum ListaPoesiasStatus { initial, loading, success, failure }

class ListaPoesiasState extends Equatable {
  final ListaPoesiasStatus status;
  final List<Poesia> poesias;
  final String? errorMessage;

  const ListaPoesiasState({
    this.status = ListaPoesiasStatus.initial,
    this.poesias = const [],
    this.errorMessage,
  });

  ListaPoesiasState copyWith({
    ListaPoesiasStatus? status,
    List<Poesia>? poesias,
    String? errorMessage,
  }) {
    return ListaPoesiasState(
      status: status ?? this.status,
      poesias: poesias ?? this.poesias,
      errorMessage: errorMessage ?? this.errorMessage,
    );
  }

  @override
  List<Object?> get props => [status, poesias, errorMessage];
}
