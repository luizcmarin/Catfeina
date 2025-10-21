# Changelog - Catfeina

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [1.0.0] - LANÇAMENTO INICIAL

### Adicionado

- **Arquitetura Inicial:** Projeto configurado com Arquitetura Limpa, MVVM, Hilt, Room, e Jetpack Compose.
- **Feature de Poesias:**
  - Listagem completa de poesias a partir de um banco de dados local.
  - Tela de detalhes para cada poesia com texto formatado.
  - Funcionalidade para marcar poesias como favoritas e lidas.
  - Sistema de anotações pessoais por poesia.
- **Feature de Personagens:**
  - Listagem e tela de detalhes para os personagens do universo Catfeina.
- **Sistema de Formatação:** Parser customizado para renderizar texto com tags (cabeçalhos, imagens, citações, etc.) usando `AnnotatedString`.
- **Navegação Completa:**
  - Navegação entre telas usando Jetpack Navigation for Compose.
  - `BottomAppBar` para acesso rápido às seções principais.
  - `NavigationDrawer` para acesso a todas as features e informações.
- **Funcionalidades Adicionais:**
  - **Pesquisa Global:** Tela de pesquisa que busca em poesias e personagens.
  - **Histórico de Visitas:** Tela que mostra os últimos conteúdos acessados.
  - **Marcadores (Atalhos):** Sistema de 10 slots para salvar atalhos para telas específicas.
  - **Text-to-Speech (TTS):** Leitura em voz alta do conteúdo das poesias.
  - **Ambientes Sonoros:** Reprodução de sons ambientes (ex: chuva) na tela de detalhes da poesia.
  - **Atelier:** Seção para o usuário criar e gerenciar suas próprias anotações.
- **Configurações do Usuário (Vozes):**
  - Tela de temas para alternar entre modo claro, escuro e padrão do sistema.
  - Persistência das preferências do usuário usando Jetpack DataStore.
- **Sincronização Remota:**
  - Lógica para buscar atualizações de conteúdo de uma fonte remota (JSON em Google Drive).
  - `WorkManager` configurado para agendar sincronizações periódicas em segundo plano.
- **UI/UX:**
  - Tela de Splash com animação.
  - Tratamento de estados de carregamento e vazio na maioria das telas.
