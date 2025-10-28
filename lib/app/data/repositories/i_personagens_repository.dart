/*
Projeto: Catfeina
Arquivo: i_personagens_repository.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Define a interface (contrato) para o repositório de personagens.
*/

import 'package:catfeina/app/data/sources/app_database.dart';

abstract class IPersonagensRepository {
  Future<List<Personagem>> getTodosPersonagens();

  Future<Personagem?> getPersonagemPorId(int id);

  Future<void> upsertPersonagem(PersonagensCompanion personagem);

  Future<void> deletePersonagem(int id);

  Future<List<Personagem>> searchPersonagens(String query);
}
