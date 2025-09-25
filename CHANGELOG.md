# Catfeina - Registro de Atualizações (Change Log)


## [0.2.0] - 2025-09-23

### Funcionalidades e Melhorias

*   **Sistema de Temas Refatorado:**
    *   Separada a lógica de seleção de tema em "Tema Base" (ex: Primavera, Verão) e "Estado do Tema" (Claro, Escuro, Automático do Sistema).
    *   Introduzidos os enums `BaseTheme` e `ThemeState` para gerenciar essas seleções.
    *   `UserPreferencesRepository` atualizado para salvar e carregar o tema base e o estado do tema como preferências distintas no DataStore.
    *   `ThemeViewModel` atualizado para expor `StateFlow`s separados para `currentBaseTheme` e `currentThemeState`, e para fornecer funções para modificar cada um independentemente.
    *   `CatfeinaAppTheme` (Composable principal do tema) modificado para aceitar `selectedBaseTheme` e `selectedThemeState` e aplicar a combinação correta de paletas de cores e tipografia.
    *   Corrigida a lógica de aplicação de cores dinâmicas (Material You) e temas base dentro do `CatfeinaAppTheme`.
    *   Melhorada a manipulação da barra de status no `CatfeinaAppTheme` para usar abordagens modernas, evitando APIs depreciadas e garantindo compatibilidade com o design de ponta a ponta (`enableEdgeToEdge`).

*   **Menu de Opções (`MenuTresPontinhos`):**
    *   Refatorado para desacoplar a lógica do menu da `InformativoScreen`.
    *   Movido o Composable `MenuTresPontinhos` para o pacote `com.marin.catfeina.dominio`.
    *   Atualizada a funcionalidade "Alterar Tema" dentro do menu para alternar o `ThemeState` (Claro, Escuro, Auto) através do `ThemeViewModel`.
    *   Ícone do item "Alterar Tema" agora reflete dinamicamente o `ThemeState` atual (Claro, Escuro, Auto).

### Correções de Bugs e Ajustes

*   **`InformativoScreen.kt`:**
    *   Corrigida a importação depreciada de `hiltViewModel` para usar `androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel`.
    *   Revisado o uso de `LocalContext.current` para garantir que seja mantido onde necessário (ex: `ImageRequest.Builder`).
    *   Clarificado o uso de `uiState` para garantir que todos os acessos sejam considerados pela IDE.
    *   Adicionada consistência na cor do ícone de navegação da `TopAppBar`.

*   **`MainActivity.kt`:**
    *   Atualizado o Composable `CatfeinaApp` para injetar e usar o `ThemeViewModel` modificado, coletando `baseTheme` e `themeState` separadamente e passando-os para `CatfeinaAppTheme`.
    *   Removida a dependência do `PreferenciasViewModel` para seleção de tema, se esta responsabilidade foi totalmente transferida para o `ThemeViewModel`.

### Documentação e Padrões

*   Adicionada e revisada documentação KDoc e cabeçalhos de arquivo para `ThemeViewModel.kt`, `UserPreferencesRepository.kt`, `MenuTresPontinhos.kt`, e `CatfeinaAppTheme.kt` para refletir as mudanças e manter a conformidade com os padrões do projeto.


## [0.1.0] - 2025-09-22

### Adicionado 🎉
- Implementada a funcionalidade completa de seleção de tema do aplicativo (Claro, Escuro, Padrão do Sistema).
- Criada a `PreferenciasScreen` permitindo ao usuário escolher e persistir o tema desejado.
- Adicionado `PreferenciasViewModel` para gerenciar o estado da seleção de tema.
- Implementado `UserPreferencesRepository` utilizando Jetpack DataStore para salvar e carregar as preferências de tema do usuário.
- Configurado `CatfeinaAppTheme` para aplicar dinamicamente o tema selecionado em todo o aplicativo.
- Integrada a tela de Preferências ao `NavigationDrawer` na `MainActivity`.
- Definida a estrutura inicial da `MainActivity` com `Scaffold`, `TopAppBar` dinâmica e `ModalNavigationDrawer`.
- Configuração inicial do Hilt para injeção de dependência.
- Configuração inicial do Jetpack Navigation Compose para navegação entre telas.

### Corrigido 🛠️
- Resolvido problema onde uma versão placeholder da `PreferenciasScreen` estava sendo incorretamente utilizada, impedindo a exibição das opções de tema. A tela de preferências funcional agora é corretamente invocada.

### Alterado ⚙️
- Roteiro de desenvolvimento (`GEMINI.MD`) atualizado para refletir o progresso nas Fases 0, 1 e 2.
- Refinada a lógica de navegação na `MainActivity` para interagir com o `NavigationDrawer` e `TopAppBar`.

---