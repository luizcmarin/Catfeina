/*
Projeto: Catfeina
Arquivo: poesia_dao.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
DAO (Data Access Object) para acessar e gerenciar os dados da tabela de Poesias
e suas notas relacionadas (PoesiaNotas).
*/

import 'package:catfeina/app/data/models/tabelas_db.dart';
import 'package:catfeina/app/data/sources/app_database.dart';
import 'package:drift/drift.dart';

part 'poesia_dao.g.dart';

@DriftAccessor(tables: [Poesias, PoesiaNotas])
class PoesiaDao extends DatabaseAccessor<AppDatabase> with _$PoesiaDaoMixin {
  final AppDatabase db;

  PoesiaDao(this.db) : super(db);

  // --- Métodos para Poesias ---

  Future<int> upsertPoesia(PoesiasCompanion entity) =>
      into(poesias).insertOnConflictUpdate(entity);

  Future<List<Poesia>> getTodasPoesias() => select(poesias).get();

  Future<Poesia?> getPoesiaPorId(int id) =>
      (select(poesias)..where((tbl) => tbl.id.equals(id))).getSingleOrNull();

  Future<int> deletePoesia(int id) =>
      (delete(poesias)..where((tbl) => tbl.id.equals(id))).go();
      
  Future<List<Poesia>> searchPoesias(String query) {
    return customSelect(
      'SELECT * FROM poesias WHERE titulo LIKE ?1 OR texto LIKE ?1',
      variables: [Variable.withString('%$query%')],
      readsFrom: {poesias},
    ).map((row) => poesias.map(row.data)).get();
  }

  // --- Métodos para PoesiaNotas ---

  Future<int> upsertNota(PoesiaNotasCompanion entity) =>
      into(poesiaNotas).insertOnConflictUpdate(entity);

  Future<PoesiaNota?> getNotaPorPoesiaId(int poesiaId) =>
      (select(poesiaNotas)..where((tbl) => tbl.poesiaId.equals(poesiaId)))
          .getSingleOrNull();
}
