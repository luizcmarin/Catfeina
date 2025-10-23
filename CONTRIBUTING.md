# Guia de Contribuição para o Catfeina (Kotlin)

Primeiramente, gostaríamos de agradecer seu interesse em contribuir para o projeto **Catfeina**! Sua
colaboração é valiosa.

## Código de Conduta

Este projeto adota
um [Código de Conduta](https://github.com/luizcmarin/Catfeina/blob/main/CODE_OF_CONDUCT.md). Ao
participar, você concorda em seguir este código. Por favor, reporte qualquer comportamento
inaceitável para os [mantenedores do projeto](mailto:luizcmarin@gmail.com).

## Como Perguntar (Dúvidas e Suporte)

Se você tem uma dúvida sobre como usar o **Catfeina** ou precisa de suporte, por favor, **não abra
uma Issue no GitHub para isso**. Em vez disso, você pode:

* Perguntar em
  nossa [seção de Discussões no GitHub](https://github.com/luizcmarin/Catfeina/discussions) (se
  habilitada).
* Procurar a resposta na nossa [Wiki do projeto](https://github.com/luizcmarin/Catfeina/wiki) (se
  existente e relevante).

## Como Reportar um Bug

Antes de relatar um problema, por favor:

1. **Verifique as Issues existentes:** Pode ser que o bug já tenha sido reportado ou resolvido. Você
   pode pesquisar em [Issues Abertas](https://github.com/luizcmarin/Catfeina/issues).
2. **Certifique-se de que está na versão mais recente:** Tente atualizar para a última versão do
   branch `main` e veja se o problema persiste.
3. **Colete informações relevantes:** Prepare informações sobre a versão do Android, modelo do
   dispositivo, e passos claros para reproduzir o bug.

Se o bug parecer novo, sinta-se à vontade
para [abrir uma nova Issue](https://github.com/luizcmarin/Catfeina/issues/new/choose) (idealmente
usando um template de bug, se disponível).

## Como Sugerir um Recurso (Feature)

Gostamos de novas ideias! Antes de começar a trabalhar em uma nova funcionalidade, por favor:

1. **Verifique as Issues existentes:** Pesquise
   em [Issues Abertas](https://github.com/luizcmarin/Catfeina/issues?q=is%3Aopen+is%3Aissue+label%3Aenhancement)
   para ver se sua ideia já foi sugerida ou está sendo discutida.
2. **Abra uma nova Issue para discussão:** Se sua ideia for
   nova, [abra uma Issue](https://github.com/luizcmarin/Catfeina/issues/new?assignees=&labels=enhancement%2Cfeature-request)
   para descrever a funcionalidade proposta e por que ela seria útil. Isso permite uma discussão
   antes que qualquer código seja escrito.
3. **Aguarde o feedback:** Permita que os mantenedores discutam a proposta.
4. **Crie um Pull Request:** Se a funcionalidade for aprovada e você decidir implementá-la, você
   pode então criar um Pull Request com suas alterações.

## Desenvolvimento Local

Para configurar o ambiente de desenvolvimento local do **Catfeina**, veja as instruções detalhadas
no arquivo [README.md](https://github.com/luizcmarin/Catfeina/blob/main/README.md).

## Submetendo Pull Requests (PRs)

Após ter sua funcionalidade ou correção implementada em um branch separado:

1. **Siga os Padrões de Codificação:** Certifique-se de que seu código adere aos padrões descritos
   abaixo. 
2. **Adicione Testes:** (Nota: Atualmente, os testes não são o foco principal, mas se incluídos,
   devem seguir as boas práticas).
    * Novas funcionalidades devem vir acompanhadas de testes (unitários e/ou de instrumentação).
    * Correções de bugs devem incluir um teste que demonstre o bug e verifique a correção.
3. **Atualize a Documentação:** Se suas alterações impactarem a documentação (README, comentários
   KDoc), atualize-a conforme necessário.
4. **Faça Rebase (se necessário):** Mantenha seu branch atualizado com o branch principal (ex:
   `main`) usando `git rebase main` para garantir um histórico limpo e facilitar o merge. Resolva
   quaisquer conflitos.
5. **Abra o Pull Request:**
    * Vá para a aba [Pull Requests](https://github.com/luizcmarin/Catfeina/pulls) do repositório e
      clique em "New pull request".
    * Escolha seu branch de feature e o branch base (geralmente `main`).
    * Forneça um título claro e uma descrição detalhada das suas alterações no PR. Se o PR resolver
      uma Issue existente, mencione-a no formato "Closes #123" ou "Fixes #123".
6. **Revisão:** Aguarde a revisão do seu PR. Esteja preparado para responder a perguntas e fazer
   alterações solicitadas.

## Padrões de Codificação

Para manter a consistência, legibilidade e manutenibilidade do código no Catfeina, seguimos os
padrões de codificação oficiais do Kotlin e as melhores práticas do Android. Ao contribuir, por
favor, adote estas diretrizes:

* **Linguagem:** [Kotlin](https://kotlinlang.org/docs/home.html). Aproveite as funcionalidades
  modernas do Kotlin, como Coroutines, Flow, funções de escopo, etc.
* **Framework de UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose). Siga as
  convenções e utilize os Composables e APIs de forma eficaz.

* **Estilo de Código e Formatação:**
    * Siga as diretrizes de estilo oficiais
      do [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
    
* **Nomeclatura:**
    * **Classes, Enums, Interfaces:** Use `UpperCamelCase`.
    * **Funções, Propriedades e Variáveis Locais:** Use `lowerCamelCase`.
    * **Constantes (top-level ou em `object`):** Use `UPPER_SNAKE_CASE` (ex:
      `const val DEFAULT_TIMEOUT = 5000L`).
    * **Funções `@Composable`:** Use `UpperCamelCase`.

* **Documentação KDoc:**
    * Documente todas as APIs públicas (funções, classes) usando a sintaxe KDoc.

## Licença

* Ao contribuir para este repositório, você concorda que suas contribuições serão licenciadas sob os
  termos
  da [GNU General Public License v3.0](https://github.com/luizcmarin/Catfeina/blob/main/LICENSE).
  Por favor, veja [`LICENSE`](https://github.com/luizcmarin/Catfeina/blob/main/LICENSE) para mais
  informações.
