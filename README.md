# Catverso - Um Santuário Digital para Poesia ✒️

[![Build Status](https://github.com/luizcmarin/catverso/actions/workflows/build.yml/badge.svg)](https://github.com/luizcmarin/catverso/actions)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg?style=flat-square)](https://www.gnu.org/licenses/gpl-3.0)

**Catverso** é um aplicativo Android nativo para amantes de poesia, construído com as tecnologias mais modernas do ecossistema Kotlin, incluindo Jetpack Compose e Gradle, e desenvolvido no Android Studio. O projeto visa oferecer uma experiência de leitura e escrita imersiva, com personalização e um mecanismo de sincronização de conteúdo.

## ✨ Funcionalidades Principais

*   **Leitura Imersiva:** Explore poesias com uma interface nativa, limpa e focada no conteúdo.
*   **Personalização:** Alterne entre temas (claro/escuro) e ajuste o tamanho da fonte.
*   **Recursos de Acessibilidade:** Ouça as poesias com a funcionalidade nativa de Texto-para-Fala (TTS).
*   **Atelier Criativo:** Escreva e gerencie suas próprias anotações e criações.
*   **Universo Compartilhado:** Conheça os personagens e o lore do Catverso.
*   **Sincronização:** Mantenha seu conteúdo sempre atualizado a partir de uma fonte externa.
*   **Mascote Interativo:** Interaja com o Romrom, o guardião dos versos, através de animações Lottie.

## 🛠️ Stack Tecnológica

O projeto Catverso é um aplicativo Android nativo, construído com as tecnologias mais modernas do ecossistema Kotlin. Ele é projetado para oferecer uma experiência de usuário fluida, reativa e de alta performance, com uma arquitetura robusta e escalável baseada em Clean Architecture e MVI.

*   **Linguagem Principal:** [Kotlin](https://kotlinlang.org/)
    *   Todo o código-fonte da aplicação é escrito em Kotlin, aproveitando seus recursos modernos como coroutines, imutabilidade e segurança de nulos.
*   **Framework de UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
    *   A interface do usuário (UI) é construída de forma 100% declarativa com o Jetpack Compose, o kit de ferramentas moderno do Android para a criação de UIs nativas. Isso elimina a necessidade de XML e promove um desenvolvimento mais rápido e eficiente.
*   **Arquitetura e Padrões:** Clean Architecture + MVI (Model-View-Intent)
    *   O projeto adota uma arquitetura limpa, separando o código em camadas de Domínio (regras de negócio puras), Dados (fontes de dados e repositórios) e Apresentação (UI e ViewModels).
    *   A camada de Apresentação utiliza o padrão MVI para gerenciar o estado da UI de forma unidirecional e previsível, garantindo consistência e facilitando a depuração.
*   **Injeção de Dependência:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
    *   Hilt, a solução recomendada pelo Google, é utilizada para gerenciar a injeção de dependências em todo o aplicativo, simplificando a criação e o fornecimento de objetos e melhorando a testabilidade.
*   **Programação Assíncrona:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html) & Flow
    *   Todas as operações assíncronas (rede, banco de dados) são gerenciadas com Coroutines e Flow, garantindo que a UI nunca seja bloqueada e que os fluxos de dados sejam tratados de forma reativa e eficiente.
*   **Banco de Dados Local:** SQLite com [SQLDelight](https://cash.app/sqldelight)
    *   Em vez de um ORM tradicional, o projeto usa SQLDelight. Ele gera interfaces Kotlin type-safe a partir de queries SQL, garantindo que todas as interações com o banco de dados sejam verificadas em tempo de compilação.
*   **Acesso à Rede:** [Ktor](https.ktor.io/) para comunicação HTTP.

## 🚀 Como Começar (Desenvolvimento)

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/luizcmarin/catverso.git
    cd catverso
    ```

2.  **Abra o projeto no Android Studio:**
    *   Use a versão mais recente do Android Studio (ex: Jellyfish ou superior).

3.  **Sincronize o Gradle:**
    *   O Android Studio deve fazer isso automaticamente ao abrir o projeto. Se não, clique em "Sync Project with Gradle Files".

4.  **Execute a aplicação:**
    *   Selecione um dispositivo ou emulador e clique em "Run 'app'" (Shift + F10).

## 📝 Diretrizes de Desenvolvimento

- **Commits e Código:** Escreva em português.
- **Versões:** As versões de dependências estão defasadas; consulte a documentação oficial para as sintaxes mais recentes. O arquivo `TOML` contém as versões mais atuais.
- **Estrutura:** Não altere o arquivo `TOML` e evite apagar conteúdo sem permissão.
- **Cabeçalho de Arquivos:** Todos os arquivos de código (`.kt`, `.kts`) devem incluir um cabeçalho de direitos autorais padronizado.

## 🐛 Reportando Bugs

Se encontrar algum bug, por favor, abra uma [Issue](https://github.com/luizcmarin/catverso/issues) detalhando o problema e os passos para reproduzi-lo.

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor, leia o nosso `CONTRIBUTING.md` para entender como você pode participar do projeto.

## 📜 Licença

Este projeto é licenciado sob a **GNU General Public License v3.0**. Veja o arquivo `LICENSE` para mais detalhes.

---

**Desenvolvido com ❤️ por Luiz Marin, Ivete Gielow Marin e Caroline Gielow Marin.**
