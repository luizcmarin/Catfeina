# Guia de Contribuição para o Catfeina

Primeiramente, agradecemos seu interesse em contribuir para o projeto **Catfeina**! Sua colaboração é muito valiosa.

## Código de Conduta

Este projeto adota um [Código de Conduta](CODE_OF_CONDUCT.md). Ao participar, você concorda em seguir este código.

## Como Reportar um Bug

Antes de relatar um problema, por favor:

1.  **Verifique as Issues existentes:** Pode ser que o bug já tenha sido reportado. Você pode pesquisar nas [Issues Abertas](https://github.com/luizcmarin/Catfeina/issues).
2.  **Use a versão mais recente:** Tente reproduzir o problema usando o código mais recente do branch `main`.

Se o bug parecer novo, sinta-se à vontade para [abrir uma nova Issue](https://github.com/luizcmarin/Catfeina/issues/new/choose), utilizando o template de **Bug Report**.

## Como Sugerir uma Funcionalidade

1.  **Verifique as sugestões existentes:** Pesquise nas [Issues](https://github.com/luizcmarin/Catfeina/issues?q=is%3Aopen+is%3Aissue+label%3Aenhancement) para ver se sua ideia já foi discutida.
2.  **Abra uma nova Issue para discussão:** Se sua ideia for nova, [abra uma Issue](https://github.com/luizcmarin/Catfeina/issues/new/choose) usando o template de **Feature Request** para descrever a proposta.

## Desenvolvimento Local

Para configurar o ambiente de desenvolvimento local do **Catfeina**, siga as instruções detalhadas no arquivo `README.md` principal do repositório.

## Submetendo Pull Requests (PRs)

Após implementar sua funcionalidade ou correção em um branch separado:

1.  **Siga os Padrões de Codificação:** Certifique-se de que seu código adere aos padrões descritos abaixo.
2.  **Atualize a Documentação:** Se suas alterações impactarem a documentação (KDoc), atualize-a conforme necessário.
3.  **Faça Rebase:** Mantenha seu branch atualizado com o `main` usando `git rebase main` para garantir um histórico limpo.
4.  **Abra o Pull Request:**
    *   Use um título claro e uma descrição detalhada. Se o PR resolver uma Issue, mencione-a com "Closes #123".
    *   Preencha o checklist do template do Pull Request.

## Padrões de Codificação

*   **Linguagem:** [Kotlin](https://kotlinlang.org/)
*   **Framework de UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Arquitetura:** MVI (Model-View-Intent) com Clean Architecture
*   **Estilo de Código e Formatação:** O projeto segue as [convenções oficiais do Kotlin](https://kotlinlang.org/docs/coding-conventions.html) e as diretrizes do Android. A formatação é gerenciada pelo Android Studio e pelo `ktlint`.

## Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a [GNU General Public License v3.0](LICENSE.md).
