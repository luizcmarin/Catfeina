/*
Projeto: Catfeina
Arquivo: personagens_repository.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Implementação concreta do repositório de personagens. Utiliza o PersonagemDao
para interagir com o banco de dados.
*/

import 'package:catfeina/app/data/repositories/i_personagens_repository.dart';
import 'package:catfeina/app/data/sources/app_database.dart';
import 'package:catfeina/app/data/sources/daos/personagem_dao.dart';

class PersonagensRepository implements IPersonagensRepository {
  final PersonagemDao _personagemDao;

  PersonagensRepository(this._personagemDao);

  @override
  Future<void> deletePersonagem(int id) {
    return _personagemDao.deletePersonagem(id);
  }

  @override
  Future<Personagem?> getPersonagemPorId(int id) {
    return _personagemDao.getPersonagemPorId(id);
  }

  @override
  Future<List<Personagem>> getTodosPersonagens() {
    return _personagemDao.getTodosPersonagens();
  }

  @override
  Future<void> upsertPersonagem(PersonagensCompanion personagem) {
    return _personagemDao.upsertPersonagem(personagem);
  }

  @override
  Future<List<Personagem>> searchPersonagens(String query) {
    return _personagemDao.searchPersonagens(query);
  }
}
