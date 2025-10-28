LEIA @DESIGN.md e @IMPLEMENTACAO.md . NÃO SE ESQUEÇA DESSES DOIS ARQUIVOS. DECORE. LEMBRE-SE SEMPRE
DAS INSTRUÇÕES E INFORMAÇÕES QUE ELES CONTEM.

# Aplicativo Catfeina - Documento de Design (Migração para Flutter)

## 1. Visão Geral

Catfeina é um aplicativo para dispositivos móveis desenvolvido para amantes de poesia,
proporcionando uma experiência serena e imersiva para leitura e interação. O objetivo desta
documentação é guiar a migração do aplicativo existente em Kotlin para uma nova base de código em
Flutter, mantendo todas as funcionalidades atuais e estabelecendo uma arquitetura robusta para o
futuro.

O aplicativo continuará a oferecer uma coleção de poemas, conversão de texto em fala (TTS),
personalização e recursos de interação, além de uma funcionalidade chave de sincronização de dados e
uma tela de depuração para facilitar o desenvolvimento e os testes.

## 2. Análise Detalhada

O objetivo principal é recriar o Catfeina em Flutter, garantindo paridade de recursos com a versão
Kotlin e aproveitando a natureza multiplataforma do Flutter para futuras expansões. O público-alvo
permanece o mesmo: leitores de poesia que valorizam um ambiente focado e acessível, incluindo
pessoas com deficiência visual.

### Principais Recursos:

* **Biblioteca de Poesia:** Uma coleção de poemas, provavelmente carregada e gerenciada através de
  um banco de dados local.
* **Tela de Leitura:** Uma interface limpa e sem distrações para a leitura de poemas.
* **Conversão de Texto em Fala (TTS):** Capacidade de ouvir poemas, gerenciada por um
  `TtsController` e seu `ViewModel`.
* **Favoritos e Histórico:** Funcionalidades para marcar poemas e rever os lidos recentemente.
* **Notas Pessoais (Atelier):** Um espaço para os usuários escreverem e salvarem suas próprias
  criações ou anotações.
* **Busca:** Uma função de busca global para encontrar poemas.
* **Personalização:** Opções para tema (claro/escuro), tamanho da fonte, tela cheia e configurações
  de voz do TTS.
* **Sincronização de Dados:** Um mecanismo para sincronizar dados do usuário (preferências, notas,
  etc.), conforme indicado pelos arquivos `SyncDataModels.kt` e `SyncPreferencesRepository.kt`. A
  lógica exata será portada do Kotlin.
* **Tela de Depuração:** Uma tela especial (`DebugScreen`) para visualizar informações internas do
  aplicativo, como o status da sincronização, logs ou outras métricas úteis para desenvolvimento e
  solução de problemas.

## 3. Tecnologias

Esta seção lista as tecnologias para cada necessidade do projeto.

* **Gerenciamento de Estado e Injeção de Dependência:**
    * **Riverpod:** Evolução do Provider, oferece mais flexibilidade, segurança em tempo de
      compilação. Ideal para projetos que podem crescer em complexidade.

* **Persistência de Dados (Banco de Dados SQL):**
    * **Drift (anteriormente Moor):** Um wrapper reativo e type-safe em torno do `sqflite`. Permite
      escrever queries em Dart ou SQL e gera o código boilerplate, garantindo segurança e
      modernidade.

* **Persistência de Dados (Simples e NoSQL):**
    * **shared_preferences:** Padrão para armazenamento simples de chave-valor (configurações de
      tema, preferências de TTS).

* **Comunicação de Rede (para Sincronização):**
    * **dio:** Um cliente HTTP poderoso com suporte a interceptadores, configuração global,
      tratamento de erros avançado e muito mais.

* **Conversão de Texto em Fala (TTS):**
    * **flutter_tts:** A biblioteca mais comum e completa para funcionalidade TTS em Flutter.

* **Reprodução de Áudio (Som Ambiente):**
    * **just_audio:** Um player de áudio rico em recursos que suporta uma variedade de formatos e
      oferece controle detalhado sobre a reprodução.

* **Imagens e Animações:**
    * **flutter_svg:** Para renderizar imagens vetoriais (SVG), ideal para ícones.
    * **lottie:** Para renderizar animações complexas do After Effects (JSON).

* **Componentes de UI Adicionais:**
    * **flutter_native_splash:** Para gerar a tela de splash screen nativa, evitando a tela branca
      inicial.
    * **introduction_screen:** Para criar um fluxo de onboarding de forma rápida e customizável.
    * **infinite_scroll_pagination:** Para lidar com a paginação em listas longas de maneira
      eficiente.

## 4. Design Detalhado

### 4.1. Arquitetura

Manteremos a arquitetura **Model-View-ViewModel (MVVM)**, que se alinha bem com os padrões modernos
de UI declarativa do Flutter.

* **Modelo (Model):** Representa os dados e a lógica de negócios, repositórios de dados).
* **Visão (View):** A UI do aplicativo, construída com widgets Flutter. A View reage às mudanças de
  estado expostas pelo ViewModel.
* **ViewModel:** Gerencia o estado da UI e a lógica de apresentação, interagindo com os
  repositórios (Model) e notificando a View sobre as atualizações.
* **Cabeçalhos e Documentação:** Todos os arquivos criados devem ser documentados e ter o modelo de
  cabeçalho abaixo:

```
Projeto: Catfeina
Arquivo: $file.fileName

Direitos autorais (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "", "$today.year")$today.year Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:

```

### 4.2. Estrutura do Projeto Proposta

```
Catfeina/
├── lib/
│   ├── app/
│   │   ├── core/
│   │   │   ├── di/                 # Injeção de Dependência
│   │   │   ├── navigation/         # Roteamento
│   │   │   └── theme/              # Temas do App
│   │   ├── data/
│   │   │   ├── models/             # Modelos de dados
│   │   │   ├── repositories/       # Repositórios
│   │   │   └── sources/            # Fontes de dados
│   │   ├── features/
│   │   │   ├── lista_poesias/      # Feature: Tela com a lista de poesias
│   │   │   ├── leitor_poesia/      # Feature: Tela de leitura de uma poesia
│   │   │   ├── ateliers/
│   │   │   ├── personagens/
│   │   │   ├── informativos/
│   │   │   ├── historicos/
│   │   │   ├── sincronizacao/
│   │   │   └── depuracao/
│   │   └── shared_widgets/         # Widgets reutilizáveis
│   └── main.dart
├── test/
└── pubspec.yaml
```

### 4.3. UI/UX

A UI será limpa e intuitiva, com navegação principal centralizada.

* **Navegação:** Uma `BottomNavigationBar` para as seções principais (Início, Poemas, Atelier,
  Busca) e uma `AppBar` com acesso às Configurações e talvez ao Histórico.
* **Telas:**
    * **Tela Inicial:** Ponto de entrada, exibindo um poema do dia e atalhos.
    * **Tela de Poemas:** Lista pesquisável e filtrável de todos os poemas.
    * **Tela de Leitura:** Exibição do poema com controles de TTS e som ambiente.
    * **Tela Atelier:** Gerenciamento de notas e criações do usuário.
    * **Tela de Configurações:** Opções de personalização.
    * **Tela de Depuração:** Acesso (talvez oculto) para visualizar o estado interno do app.

### 4.4 Modelo de Dados (Banco de Dados)

Esta seção detalha a migração do esquema do banco de dados SQLDelight para o Drift. A estrutura das
tabelas será mantida, garantindo a consistência dos dados.

#### Definição das Tabelas em Drift

As tabelas SQL serão declaradas como classes Dart. O Drift usará essas declarações para gerar todo o
código de acesso ao banco de dados.

**1. Tabela `poesias`**

    class Poesias extends Table {
      IntColumn get id => integer().autoIncrement()();
      TextColumn get titulo => text().customConstraint('UNIQUE')();
      TextColumn get textoBase => text()();
      TextColumn get texto => text()();
      TextColumn get textoFinal => text().nullable()();
      TextColumn get imagem => text().nullable()();
      TextColumn get autor => text().nullable()();
      TextColumn get nota => text().nullable()();
      DateTimeColumn get dataCriacao => dateTime()();
    }

**2. Tabela `poesia_notas`**

    class PoesiaNotas extends Table {
      IntColumn get id => integer().autoIncrement()();
      IntColumn get poesiaId => integer().references(Poesias, #id, onDelete: KeyAction.cascade)();
      BoolColumn get ehFavorita => boolean().withDefault(const Constant(false))();
      BoolColumn get foiLida => boolean().withDefault(const Constant(false))();
      DateTimeColumn get dataFavoritado => dateTime().nullable()();
      DateTimeColumn get dataLeitura => dateTime().nullable()();
      TextColumn get notaUsuario => text().nullable()();
    }

**3. Tabela `personagens`**

    class Personagens extends Table {
      IntColumn get id => integer().autoIncrement()();
      TextColumn get nome => text().customConstraint('UNIQUE')();
      TextColumn get biografia => text()();
      TextColumn get imagem => text().nullable()();
      DateTimeColumn get dataCriacao => dateTime()();
    }

**4. Tabela `ateliers`**

    class Ateliers extends Table {
      IntColumn get id => integer().autoIncrement()();
      TextColumn get titulo => text()();
      TextColumn get conteudo => text()();
      DateTimeColumn get dataAtualizacao => dateTime()();
      BoolColumn get fixada => boolean().withDefault(const Constant(false))();
    }

**5. Tabela `historicos`**

    class Historicos extends Table {
      IntColumn get id => integer().autoIncrement()();
      TextColumn get tipoConteudo => text().withDefault(const Constant('poesias'))();
      IntColumn get conteudoId => integer()();
      TextColumn get titulo => text()();
      TextColumn get imagem => text().nullable()();
      DateTimeColumn get dataVisita => dateTime()();
    }

**6. Tabela `informativos`**

    class Informativos extends Table {
      IntColumn get id => integer().autoIncrement()();
      TextColumn get chave => text().customConstraint('UNIQUE')();
      TextColumn get titulo => text()();
      TextColumn get conteudo => text()();
      DateTimeColumn get dataAtualizacao => dateTime()();
    }

#### Migração das Queries

As queries nomeadas do SQLDelight serão recriadas em Dart usando o sistema de queries do Drift
dentro de classes `Dao` (Data Access Object) e em portugues. Isso garante segurança de tipos e
melhor integração com o código Dart. Todas as tabelas, com exceção da tabela `poesia_notas`, terão
CRUD completo.

## 5. Diagramas

### 5.1. Arquitetura da Aplicação

```mermaid
graph TD
    A[View (Flutter Widgets)] --> B(ViewModel);
    B --> C{Repository};
    C --> D[Data Sources];
    D --> E[Banco de Dados Local];
    D --> F[API/Serviço de Sincronização];
    D --> G[SharedPreferences];
```

## 6. Resumo

A migração do Catfeina para Flutter é uma oportunidade de modernizar a base de código e prepará-la
para o futuro. Usando uma arquitetura MVVM clara, tecnologias modernas de Flutter e mantendo todas
as funcionalidades existentes, incluindo a lógica de sincronização e a tela de depuração, o novo
aplicativo será robusto, escalável e de fácil manutenção.
