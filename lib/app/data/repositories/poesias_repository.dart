/*
Projeto: Catfeina
Arquivo: poesias_repository.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Implementação concreta do repositório de poesias. Esta classe utiliza o PoesiaDao
para interagir com o banco de dados e executar as operações de dados solicitadas.
*/

import 'package:catfeina/app/data/repositories/i_poesias_repository.dart';
import 'package:catfeina/app/data/sources/app_database.dart';
import 'package:catfeina/app/data/sources/daos/poesia_dao.dart';

class PoesiasRepository implements IPoesiasRepository {
  final PoesiaDao _poesiaDao;

  PoesiasRepository(this._poesiaDao);

  @override
  Future<void> deletePoesia(int id) {
    return _poesiaDao.deletePoesia(id);
  }

  @override
  Future<Poesia?> getPoesiaPorId(int id) {
    return _poesiaDao.getPoesiaPorId(id);
  }

  @override
  Future<List<Poesia>> getTodasPoesias() {
    return _poesiaDao.getTodasPoesias();
  }

  @override
  Future<void> upsertPoesia(PoesiasCompanion poesia) {
    return _poesiaDao.upsertPoesia(poesia);
  }

  @override
  Future<List<Poesia>> searchPoesias(String query) {
    return _poesiaDao.searchPoesias(query);
  }
}
