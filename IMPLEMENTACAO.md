LEIA @DESIGN.md e @IMPLEMENTACAO.md . NÃO SE ESQUEÇA DESSES DOIS ARQUIVOS. DECORE. LEMBRE-SE SEMPRE
DAS INSTRUÇÕES E INFORMAÇÕES QUE ELES CONTEM.

# Aplicativo Catfeina - Plano de Implementação (Migração Flutter)

Este documento descreve o plano de implementação em fases para migrar o aplicativo Catfeina de
Kotlin para Flutter. Os fontes do projeto kotlin estão na pasta raiz chamada 'legado' e servirão
para consulta se necessário.

**NUNCA USAR BIBLIOTECAS OU CODIGOS DEFASADOS. SEMPRE VERIFICAR A DOCUMENTAÇÃO DA ÚLTIMA VERSÃO DE
CADA BIBLIOTECA E DO FLUTTER PORQUE VOCE TEM INFORMAÇÕES DESATUALIZADAS SOBRE MUITAS COISAS.**

## Diário

**2025-10-28: Fase 6 - UI de Configurações**
- Criada a feature de Configurações, incluindo `ConfiguracoesState`, `ConfiguracoesViewModel` e `ConfiguracoesScreen`.
- O `ConfiguracoesViewModel` agora carrega e salva a preferência de tema do usuário usando o `IPreferenciasRepository`.
- O `MaterialApp` principal foi atualizado para reagir às mudanças de tema, proporcionando uma experiência de usuário dinâmica.
- A `ConfiguracoesScreen` foi implementada com um `SegmentedButton` para a seleção de tema e foi conectada à `AppBar` da tela principal.
- Adicionado um teste unitário para o `ConfiguracoesViewModel` para validar a lógica de carregamento e salvamento de tema.

**2025-10-28: Fase 6 - UI da Busca**
- Adicionadas as queries de busca `searchPoesias` e `searchPersonagens` aos respectivos DAOs e Repositórios, corrigindo uma omissão da Fase 3.
- Criada a feature de Busca (`State`, `ViewModel` e `Screen`).
- O `BuscaViewModel` implementa uma lógica de debounce para evitar buscas excessivas enquanto o usuário digita.
- A `BuscaScreen` foi conectada à navegação principal na `ShellScreen`.
- Criado um teste unitário para o `BuscaViewModel` para validar a lógica de busca e debounce.

**2025-10-28: Fase 5 - UI - Telas Principais e Navegação**
- Implementada a `ShellScreen` com a `BottomNavigationBar` para servir como estrutura de navegação principal do aplicativo.
- A `ShellScreen` foi definida como a tela inicial no `main.dart`.
- Criada a `ListaPoesiasScreen`, que consome o `ListaPoesiasViewModel` para exibir o estado de carregamento, erro ou sucesso com a lista de poesias.
- A `ListaPoesiasScreen` foi conectada à segunda aba da `BottomNavigationBar`.
- Criada a `LeitorPoesiaScreen` para exibir os detalhes de uma única poesia, com seu respectivo `State` e `ViewModel` (`family` provider).
- Implementada a navegação da `ListaPoesiasScreen` para a `LeitorPoesiaScreen` ao tocar em um item, passando o ID da poesia.
- Criado um teste de widget para a `ListaPoesiasScreen` para garantir a correta renderização dos diferentes estados.

**2025-10-28: Fase 4 - Lógica de Negócios e ViewModels**
- Implementada a estrutura inicial para o `SyncRepository`, que irá lidar com a sincronização de dados da API externa.
- Criado o arquivo `data_providers.dart` para centralizar a injeção de dependência de toda a camada de dados (Banco, DAOs, Repositórios) com Riverpod.
- Criado o primeiro ViewModel da aplicação (`ListaPoesiasViewModel`) com seu respectivo estado (`ListaPoesiasState`), seguindo os padrões do Riverpod.
- Adicionada a dependência `equatable` para facilitar a comparação de objetos de estado.
- Adicionada a dependência de desenvolvimento `mockito` e criado o primeiro teste unitário para um ViewModel, simulando o repositório e testando a lógica de negócios de forma isolada.

**2025-10-28: Fase 3 - Modelos de Dados e Repositórios**
- Definidas todas as tabelas do banco de dados (`Poesias`, `Personagens`, etc.) em `tabelas_db.dart` usando a sintaxe do Drift.
- Configurada a classe principal `AppDatabase` e os `DAOs` (Data Access Objects) para cada tabela.
- A ferramenta `build_runner` foi executada para gerar todo o código necessário para o Drift.
- Criadas as interfaces (`i_*_repository.dart`) e implementações (`*_repository.dart`) para todos os repositórios, estabelecendo a camada de abstração de dados.
- Configurado o `PreferenciasRepository` para gerenciar dados simples com `shared_preferences`.
- A camada de dados está agora totalmente estruturada, pronta para ser usada pela lógica de negócios na próxima fase.

**2025-10-28: Fase 2 - Configuração de Flavors**
- Adicionados os `productFlavors` (`catverso`, `catmoney`, `catpersonal`) ao `android/app/build.gradle.kts`.
- Criados os pontos de entrada `main_<flavor>.dart` para cada flavor.
- Implementada a classe base `AppConfig` e as configurações específicas para cada flavor.
- Configurado o `appConfigProvider` com Riverpod para injeção de dependência da configuração.
- A estrutura principal do app foi refatorada para aceitar a configuração via `ProviderScope`.
- A fase foi concluída conforme o plano, preparando o app para ter múltiplas versões.

**2025-10-28: Fase 1 - Configuração do Projeto**
- O projeto Flutter foi inicializado.
- O código boilerplate foi removido e substituído por uma estrutura mínima.
- As dependências iniciais foram adicionadas ao `pubspec.yaml`, incluindo Riverpod, Drift e outras.
- A estrutura completa de diretórios foi criada conforme o `DESIGN.md`.
- Um workflow de CI básico foi configurado em `.github/workflows/flutter_ci.yml`.
- O ano do copyright foi corrigido para 2025 após um erro inicial.
- O processo ocorreu sem maiores surpresas, seguindo o plano.

---

### Procedimento Padrão Pós-Fase

Após concluir cada fase, eu irei:

- [ ] Criar/modificar testes unitários para o código adicionado/modificado.
- [ ] Executar a ferramenta `dart fix --apply` para limpar o código.
- [ ] Executar `flutter analyze` e corrigir todos os problemas.
- [ ] Executar todos os testes para garantir a aprovação.
- [ ] Executar `dart format .` para garantir a formatação correta.
- [ ] Reler este arquivo `IMPLEMENTACAO.md` para verificar o plano para a próxima fase.
- [ ] Atualizar a seção "Diário" com o progresso e aprendizados.
- [ ] Marcar as caixas de seleção dos itens concluídos.
- [ ] Usar `git diff` para revisar as alterações e preparar uma mensagem de commit para sua
  aprovação.
- [ ] Aguardar sua aprovação antes de fazer o commit e avançar para a próxima fase.

---

## Fase 1: Configuração do Projeto e Dependências

- [x] Criar o novo projeto Flutter `Catfeina`.
- [x] Limpar o código boilerplate do `main.dart` e remover o widget de exemplo e seus testes.
- [x] Atualizar `pubspec.yaml` com a descrição do projeto e versão `0.1.0`.
- [x] Criar os arquivos `.md` existentes na pasta raiz 'legado' atualizando para a nova realidade.
- [x] Adicionar as dependências iniciais já decididas.
- [x] Configurar a estrutura de diretórios inicial conforme o `DESIGN.md`.
- [x] Criar um novo workflow de CI (Integração Contínua) em `.github/workflows/flutter_ci.yml`. Este
  arquivo substituirá os workflows do Kotlin e irá conter jobs para instalar o Flutter, rodar
  `flutter analyze` e `flutter test` a cada commit/pull request. Criar os arquivos .md e ajustá-los.
  A pasta `legado/.github` pode ser removida ou arquivada.

## Fase 2: Configuração de Flavors

- [x] Definir os `productFlavors` no arquivo `android/app/build.gradle` (ex: `catverso`, `catmoney`,
  `catpersonal`).
- [x] Criar arquivos de ponto de entrada separados em `lib/` para cada flavor.
- [x] Criar uma classe de configuração `AppConfig` que conterá variáveis específicas do flavor (nome
  do app, cores, URL da API, etc).
- [x] Criar um `Provider` no Riverpod para disponibilizar a instância de `AppConfig` para todo o
  aplicativo.
- [x] Atualizar cada arquivo `main_<flavor>.dart` para inicializar a `AppConfig` correta e passá-la
  para o `Provider` antes de executar o app.
- [x] Criar as configurações de execução (Launch configurations) na IDE para rodar e depurar cada
  flavor separadamente.

## Fase 3: Modelos de Dados e Repositórios

- [x] Criar todos os modelos de dados e tabelas do Drift em Dart (`.dart`) conforme a estrutura
  definida no `Anexo 1` deste documento.
- [x] Implementar a camada de repositório (`abstract class` e implementação), como
  `PoesiasRepository`, `SyncRepository`, etc.
- [x] Configurar o serviço de banco de dados (`Drift`) e os `DAOs` (Data Access Objects) para as
  queries.
- [x] Configurar o serviço de `shared_preferences` para as configurações do usuário.

## Fase 4: Lógica de Negócios e ViewModels

- [x] Implementar a lógica de portabilidade da sincronização de dados no `SyncRepository`, fazendo
  com que ele leia a URL da API a partir da `AppConfig` fornecida pelo provider de flavors.
- [x] Criar os ViewModels que usarão os repositórios para obter e gerenciar os dados.
- [x] Configurar a injeção de dependência com Riverpod para fornecer os repositórios aos ViewModels.

## Fase 5: UI - Telas Principais e Navegação

- [x] Implementar a estrutura de navegação principal (ex: `BottomNavigationBar`, será comum em todas
  as telas).
- [x] Criar a UI da tela de cada tabela, conectando-a à navegação (exceto a tabela 'PoesiaNotas'
  cujo campo 'nota' será anexado ao final de cada poesia para alguma anotação do usuário).
- [x] Implementar a navegação entre listas e as telas de detalhes.

## Fase 6: UI - Funcionalidades Adicionais

- [x] Criar a UI da tela de Busca e implementar sua lógica.
- [x] Criar a UI da tela de Configurações para tema, fonte, etc., lendo os valores padrão da
  `AppConfig`. (Verificar os itens a colocar conforme '/legado/,,,/MenuDrawer.kt')

## Fase 7: Funcionalidades Específicas (TTS e Debug)

- [ ] Integrar o `flutter_tts` na tela de leitura, com controles de play/pause/stop gerenciados por
  um ViewModel.
- [ ] Integrar o `just_audio` para os sons de ambiente.
- [ ] Criar a tela de Depuração (`DebugScreen`) e seu `DebugViewModel`, exibindo o status da
  sincronização e outras informações internas.

## Fase 8: Finalização e Testes

- [ ] Escrever testes de widget para as telas principais (e me explique como usar).
- [ ] Refinar a implementação de troca de tema e tamanho de fonte.
- [ ] Realizar uma revisão completa do código e da UI.
- [ ] Pedir a sua inspeção final do aplicativo e do código para garantir que tudo está conforme o
  esperado e realizar os ajustes necessários.

## Anexo 1: Estrutura do banco de dados

-- =============================================
-- Tabela de Poesias
-- =============================================
CREATE TABLE Poesias (
id INTEGER NOT NULL PRIMARY KEY,
titulo TEXT NOT NULL,
textoBase TEXT NOT NULL,
texto TEXT NOT NULL,
textoFinal TEXT,
imagem TEXT,
autor TEXT,
nota TEXT,
dataCriacao INTEGER NOT NULL
);
CREATE UNIQUE INDEX idx_poesia_titulo ON poesias(titulo);

-- =============================================
-- Tabela de Estado da Poesia (Notas do Usuário)
-- =============================================
CREATE TABLE PoesiaNotas (
id INTEGER NOT NULL PRIMARY KEY,
poesiaId INTEGER NOT NULL,
ehFavorita INTEGER AS Boolean NOT NULL DEFAULT 0,
foiLida INTEGER AS Boolean NOT NULL DEFAULT 0,
dataFavoritado INTEGER,
dataLeitura INTEGER,
notaUsuario TEXT,
FOREIGN KEY(poesiaId) REFERENCES poesias(id) ON DELETE CASCADE
);

-- =============================================
-- Tabela de Personagens
-- =============================================
CREATE TABLE Personagens (
id INTEGER NOT NULL PRIMARY KEY,
nome TEXT NOT NULL UNIQUE,
biografia TEXT NOT NULL,
imagem TEXT,
dataCriacao INTEGER NOT NULL
);
CREATE UNIQUE INDEX idx_personagem_nome ON personagens(nome);

-- =============================================
-- Tabela do Ateliers
-- =============================================
CREATE TABLE Ateliers (
id INTEGER NOT NULL PRIMARY KEY,
titulo TEXT NOT NULL,
conteudo TEXT NOT NULL,
dataAtualizacao INTEGER NOT NULL,
fixada INTEGER AS Boolean NOT NULL DEFAULT 0
);

-- =============================================
-- Tabela de Histórico
-- =============================================
CREATE TABLE Historicos (
id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
tipoConteudo TEXT NOT NULL DEFAULT 'poesias',
conteudoId INTEGER NOT NULL,
titulo TEXT NOT NULL,
imagem TEXT,
dataVisita INTEGER NOT NULL
);

-- =============================================
-- Tabela de Informativos (Textos Gerais)
-- =============================================
CREATE TABLE Informativos (
id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
chave TEXT NOT NULL UNIQUE,
titulo TEXT NOT NULL,
conteudo TEXT NOT NULL,
dataAtualizacao INTEGER NOT NULL
);

-- =============================================
-- Queries para Poesias
-- =============================================
upsertPoesia: INSERT OR REPLACE INTO poesias(id, categoria, titulo, textoBase, texto, textoFinal,
imagem, autor, nota, campoUrl, dataCriacao) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
upsertNota: INSERT OR REPLACE INTO poesia_notas(poesiaId, ehFavorita, foiLida, dataFavoritado,
dataLeitura, notaUsuario) VALUES(?, ?, ?, ?, ?, ?);
deletePoesiaById: DELETE FROM poesias WHERE id = ?;
getPoesiasCompletas: SELECT p.id, p.categoria, p.titulo, p.texto, p.imagem, p.autor, p.nota,
p.textoBase, p.textoFinal, p.campoUrl, p.dataCriacao, COALESCE(pn.ehFavorita, 0) AS isFavorito,
pn.dataFavoritado, COALESCE(pn.foiLida, 0) AS isLido, pn.dataLeitura, pn.notaUsuario FROM poesias AS
p LEFT JOIN poesia_notas AS pn ON p.id = pn.poesiaId ORDER BY p.dataCriacao DESC;
getPoesiaCompletaById: SELECT p.id, p.categoria, p.titulo, p.texto, p.imagem, p.autor, p.nota,
p.textoBase, p.textoFinal, p.campoUrl, p.dataCriacao, COALESCE(pn.ehFavorita, 0) AS isFavorito,
pn.dataFavoritado, COALESCE(pn.foiLida, 0) AS isLido, pn.dataLeitura, pn.notaUsuario FROM poesias AS
p LEFT JOIN poesia_notas AS pn ON p.id = pn.poesiaId WHERE p.id = ?;
getPoesiaAleatoria: SELECT p.id, p.categoria, p.titulo, p.texto, p.imagem, p.autor, p.nota,
p.textoBase, p.textoFinal, p.campoUrl, p.dataCriacao, COALESCE(pn.ehFavorita, 0) AS isFavorito,
pn.dataFavoritado, COALESCE(pn.foiLida, 0) AS isLido, pn.dataLeitura, pn.notaUsuario FROM poesias AS
p LEFT JOIN poesia_notas AS pn ON p.id = pn.poesiaId ORDER BY RANDOM() LIMIT 1;
getPoesiasFavoritas: SELECT p.id, p.categoria, p.titulo, p.texto, p.imagem, p.autor, p.nota,
p.textoBase, p.textoFinal, p.campoUrl, p.dataCriacao, COALESCE(pn.ehFavorita, 0) AS isFavorito,
pn.dataFavoritado, COALESCE(pn.foiLida, 0) AS isLido, pn.dataLeitura, pn.notaUsuario FROM poesias AS
p INNER JOIN poesia_notas AS pn ON p.id = pn.poesiaId WHERE pn.ehFavorita = 1 ORDER BY
pn.dataFavoritado DESC;
getUltimaPoesiaAdicionada: SELECT * FROM poesias ORDER BY dataCriacao DESC LIMIT 1;
countPoesias: SELECT COUNT(id) FROM poesias;
getPoesiasPaginadas:
SELECT p.id, p.categoria, p.titulo, p.texto, p.imagem, p.autor, p.nota, p.textoBase, p.textoFinal,
p.campoUrl, p.dataCriacao, COALESCE(pn.ehFavorita, 0) AS isFavorito, pn.dataFavoritado, COALESCE(
pn.foiLida, 0) AS isLido, pn.dataLeitura, pn.notaUsuario
FROM poesias AS p
LEFT JOIN poesia_notas AS pn ON p.id = pn.poesiaId
ORDER BY p.dataCriacao DESC
LIMIT ? OFFSET ?;

-- =============================================
-- Queries para Atelier
-- =============================================
upsertAtelier: INSERT OR REPLACE INTO atelier(id, titulo, conteudo, dataAtualizacao, fixada)
VALUES (?, ?, ?, ?, ?);
deleteAtelier: DELETE FROM atelier WHERE id = ?;
deleteAllAtelier: DELETE FROM atelier;
getAllAtelier: SELECT * FROM atelier ORDER BY dataAtualizacao DESC;
getAtelierById: SELECT * FROM atelier WHERE id = ?;

-- =============================================
-- Queries para Personagens
-- =============================================
upsertPersonagem: INSERT OR REPLACE INTO personagens(id, nome, descricao, imagem, dataCriacao)
VALUES (?, ?, ?, ?, ?);
deletePersonagemById: DELETE FROM personagens WHERE id = ?;
getAllPersonagens: SELECT * FROM personagens ORDER BY nome ASC;
getPersonagemById: SELECT * FROM personagens WHERE id = ?;

-- =============================================
-- Queries para Informativos
-- =============================================
upsertInformativo: INSERT OR REPLACE INTO informativos(chave, titulo, conteudo, dataAtualizacao)
VALUES (?, ?, ?, ?);
getAllInformativos: SELECT * FROM informativos ORDER BY chave ASC;
getInformativoByChave: SELECT * FROM informativos WHERE chave = ?;

-- =============================================
-- Queries para Histórico
-- =============================================
insertHistorico: INSERT INTO historico(tipoConteudo, conteudoId, titulo, imagem, dataVisita)
VALUES (?, ?, ?, ?, ?);
limparHistorico: DELETE FROM historico;
getHistoricoCompleto: SELECT * FROM historico ORDER BY dataVisita DESC;

-- =============================================
-- Queries para Busca (Search)
-- =============================================
searchPoesias: SELECT p.id, p.categoria, p.titulo, p.texto, p.imagem, p.autor, p.nota, p.textoBase,
p.textoFinal, p.campoUrl, p.dataCriacao, COALESCE(pn.ehFavorita, 0) AS isFavorito,
pn.dataFavoritado, COALESCE(pn.foiLida, 0) AS isLido, pn.dataLeitura, pn.notaUsuario FROM poesias AS
p LEFT JOIN poesia_notas AS pn ON p.id = pn.poesiaId WHERE p.titulo LIKE '%' || ? || '%' OR p.texto
LIKE '%' || ? || '%';
searchPersonagens: SELECT * FROM personagens WHERE nome LIKE '%' || ? || '%' OR descricao
LIKE '%' || ? || '%';

