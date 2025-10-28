/*
Projeto: Catfeina
Arquivo: personagem_dao.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
DAO (Data Access Object) para acessar e gerenciar os dados da tabela de Personagens.
*/

import 'package:catfeina/app/data/models/tabelas_db.dart';
import 'package:catfeina/app/data/sources/app_database.dart';
import 'package:drift/drift.dart';

part 'personagem_dao.g.dart';

@DriftAccessor(tables: [Personagens])
class PersonagemDao extends DatabaseAccessor<AppDatabase> with _$PersonagemDaoMixin {
  final AppDatabase db;

  PersonagemDao(this.db) : super(db);

  Future<int> upsertPersonagem(PersonagensCompanion entity) =>
      into(personagens).insertOnConflictUpdate(entity);

  Future<List<Personagem>> getTodosPersonagens() => select(personagens).get();

  Future<Personagem?> getPersonagemPorId(int id) =>
      (select(personagens)..where((tbl) => tbl.id.equals(id))).getSingleOrNull();

  Future<int> deletePersonagem(int id) =>
      (delete(personagens)..where((tbl) => tbl.id.equals(id))).go();
      
  Future<List<Personagem>> searchPersonagens(String query) {
    return customSelect(
      'SELECT * FROM personagens WHERE nome LIKE ?1 OR biografia LIKE ?1',
      variables: [Variable.withString('%$query%')],
      readsFrom: {personagens},
    ).map((row) => personagens.map(row.data)).get();
  }
}
