/*
Projeto: Catfeina
Arquivo: i_poesias_repository.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Define a interface (contrato) para o repositório de poesias. Esta abstração
permite que a camada de ViewModel dependa de uma interface, e não de uma
implementação concreta, facilitando testes e manutenção.
*/

import 'package:catfeina/app/data/sources/app_database.dart';

abstract class IPoesiasRepository {
  Future<List<Poesia>> getTodasPoesias();

  Future<Poesia?> getPoesiaPorId(int id);

  Future<void> upsertPoesia(PoesiasCompanion poesia);

  Future<void> deletePoesia(int id);

  Future<List<Poesia>> searchPoesias(String query);
}
