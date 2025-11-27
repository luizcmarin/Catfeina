# GUIA MESTRE DO PROJETO CATVERSO PARA O ASSISTENTE GEMINI

## 📜 1. DIRETRIZES OBRIGATÓRIAS

- **Idioma:** A comunicação e o código devem ser em **português**.
- **Atualização de Dependências:** Meu banco de dados de conhecimento pode estar defasado (Novembro de 2025). **SEMPRE VERIFIQUE A SINTAXE E AS VERSÕES** na documentação oficial. O arquivo `libs.versions.toml` é a fonte da verdade para as versões das dependências.
- **Estrutura de Arquivos:**
    - A pasta `legado/` deve ser ignorada, exceto quando solicitado para consulta.
    - A pasta `pesquisa/` contém o projeto antigo e deve ser usada como referência para layouts e funcionalidades (CRUDs, etc.).
    - Manter a estrutura de pastas atual, conforme definido na seção de arquitetura.
- **Qualidade de Código:**
    - **NÃO** usar gambiarras ou código defasado. Siga as melhores práticas de programação moderna.
    - Valores fixos devem ser centralizados no arquivo `core/util/Constantes.kt`.
- **Gerenciamento de Arquivos:**
    - **NUNCA** altere o arquivo `libs.versions.toml` sem permissão.
    - **NUNCA** apague conteúdo de arquivos sem permissão explícita.
- **Cabeçalho Obrigatório:** Todos os arquivos de código (`.kt`, `.kts`) devem ter o seguinte cabeçalho:
    ```kotlin
    /*
    *  Projeto: Catfeina/Catverso
    *  Arquivo: [CAMINHO/NOME_DO_ARQUIVO.EXTENSÃO]
    *
    *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
    *
    *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
    *
    *  Este arquivo faz parte do projeto Catfeina.
    *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
    *  dele, é estritamente proibida.
    *
    *  Nota: [DESCRIÇÃO DO CONTEÚDO DO ARQUIVO].
    *
    */
    ```
- **Diário de Bordo:** Manter o arquivo `diario.md` na raiz do projeto atualizado com um changelog claro e elegante.

##  Catverso - Um Santuário Digital para Poesia ✒️

**Catverso** é um aplicativo Android nativo para amantes de poesia, construído com as tecnologias mais modernas do ecossistema Kotlin, incluindo Jetpack Compose e Gradle.

### ✨ Funcionalidades Principais

*   **Leitura Imersiva:** UI limpa e focada no conteúdo.
*   **Personalização:** Temas claro/escuro e ajuste de tamanho da fonte.
*   **Acessibilidade:** Texto-para-Fala (TTS) nativo.
*   **Atelier Criativo:** Escrita e gerenciamento de notas.
*   **Universo Compartilhado:** Lore e personagens do Catverso.
*   **Sincronização:** Conteúdo sempre atualizado a partir de uma fonte externa.
*   **Mascote Interativo:** Animações Lottie para o mascote Romrom.

---

## 🛠️ 3. STACK TÉCNICA E ARQUITETURA

### 3.1. Visão Geral

- **Linguagem:** **Kotlin** (Coroutines, Flow, Imutabilidade, Null-safety).
- **UI:** **Jetpack Compose** (100% declarativo).
- **Arquitetura:** **Clean Architecture + MVI (Model-View-Intent)**.
- **Injeção de Dependência:** **Hilt**.
- **Banco de Dados:** **SQLite** com **SQLDelight** para código type-safe.
- **Rede:** **Ktor** para cliente HTTP.
- **Serialização:** **Kotlinx Serialization**.
- **Build:** **Gradle** com **Version Catalog** (`libs.versions.toml`).

### 3.2. Estrutura de Pacotes (Clean Architecture em Português)

- **`dados` (Camada de Dados):** Repositórios, Fontes de Dados (Ktor, SQLDelight), DTOs, Mappers.
- **`dominio` (Camada de Domínio):** Lógica de negócio, Casos de Uso (Use Cases), Modelos de Domínio.
- **`ui` (Camada de Apresentação):** Telas (Composables), ViewModels, Estados da UI (MVI).
- **`core`:** Código compartilhado (Tema, Navegação, Constantes).
- **`util`:** Funções e classes utilitárias.

### 3.3. Product Flavors (`catverso` e `catmoney`)

- A mesma base de código gera dois apps distintos, diferenciados por recursos (`res/`) e `applicationIdSuffix` definidos no `app/build.gradle.kts`.

---

## 💾 4. BANCO DE DADOS (SQLDELIGHT)

O schema é definido em arquivos `.sq`.

- **`tbl_atelier`**: `id`, `titulo`, `texto`, `atualizadoem`, `fixada`.
- **`tbl_historico`**: `id`, `tipoconteudo`, `conteudoid`, `titulo`, `vistoem`.
- **`tbl_informativo`**: `id`, `chave`, `titulo`, `conteudo`, `imagem`, `atualizadoem`.
- **`tbl_meow`**: `id`, `texto`, `atualizadoem` (frases do mascote).
- **`tbl_personagem`**: `id`, `nome`, `biografia`, `imagem`, `atualizadoem`.
- **`tbl_poesianota`**: `poesiaid` (PK), `favorita`, `lida`, `dataleitura`, `notausuario`.
- **`tbl_poesia`**: `id`, `titulo`, `textobase`, `texto`, `textofinal`, `imagem`, `autor`, `nota`, `anterior`, `proximo`, `atualizadoem`.

---

## 🔄 5. FLUXO DE SINCRONIZAÇÃO

1.  **Download do Manifesto:** Ktor baixa o `manifest.json`.
2.  **Comparação de Versões:** Compara a versão do manifesto com a local (salva no **DataStore**).
3.  **Sincronização Incremental:**
    - Baixa o JSON do módulo.
    - **Desserializa** com Kotlinx.Serialization.
    - **Exclui** registros (`ids_excluidos`) via SQLDelight.
    - **Insere/Atualiza** (`upsert`) registros (`registros_atualizados`) via SQLDelight.
    - Atualiza a versão no DataStore.
4.  **Imagens:** Baixa `imagens.zip` se a versão mudou e extrai para o armazenamento interno.
5.  **Atualização do App (OTA):** Compara `versionCode`, exibe `AlertDialog` com changelog e usa `DownloadManager` para baixar o novo APK.

---

## 🎨 6. UI E RECURSOS ESPECIAIS

- **Navegação:** `Scaffold` com `BottomAppBar`.
- **Listas:** `LazyColumn` para performance.
- **Busca:** `TextField` com lógica de `debounce` no ViewModel usando Kotlin Flow.
- **Configurações:** `Switch` e `Slider` para tema e fonte, salvos no **DataStore**.
- **TTS e Som Ambiente:** Lógica encapsulada em `Services` e controlada por ViewModels.
- **Mascote Romrom:** Composable customizado com a biblioteca `lottie-compose` e interatividade via `clickable`.
- **Tela de Depuração:** Acessível em builds `debug`, exibe estatísticas do banco, info da build e permite limpar o banco de dados.

