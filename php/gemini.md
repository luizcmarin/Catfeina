
### Tags de Bloco (Criam elementos próprios)

Estas tags criam elementos que ocupam sua própria linha ou espaço, como um título ou uma imagem.

- **Cabeçalhos (`t1` a `t6`)**
    - **Descrição:** Cria um título. `t1` é o maior e `t6` o menor.
    - **Uso:** `{t1|Este é um Título Principal}`
    - **Uso:** `{t2|Este é um Subtítulo}`

- **Citação (`cit`)**
    - **Descrição:** Formata um bloco de texto como uma citação, geralmente com um estilo diferente
      e recuo.
    - **Uso:** `{cit|Esta é uma citação famosa.}`

- **Imagem (`imagem`)**
    - **Descrição:** Exibe uma imagem a partir da pasta de `assets`. O primeiro parâmetro é o nome
      do arquivo, e o segundo é o texto alternativo.
    - **Uso:** `{imagem|gato_fofo.png|Um gato fofo dormindo}`

- **Item de Lista (`li`)**
    - **Descrição:** Cria um item de uma lista com um marcador (•). Pode conter outras tags de
      formatação em linha.
    - **Uso:** `{li|Primeiro item da lista com texto em {n|negrito}.}`

- **Linha Horizontal (`linha`)**
    - **Descrição:** Insere uma linha divisória horizontal. Não possui conteúdo.
    - **Uso:** `{linha}`

### Tags em Linha (Formatam texto dentro de um parágrafo)

Estas tags são aplicadas a um trecho de texto sem quebrar o fluxo do parágrafo.

- **Negrito (`n`)**
    - **Descrição:** Aplica o estilo de negrito ao texto.
    - **Uso:** `Este é um texto com uma palavra em {n|negrito}.`

- **Destaque (`d`)**
    - **Descrição:** Destaca o texto, geralmente com uma cor de fundo.
    - **Uso:** `Esta informação é {d|muito importante}.`

- **Itálico (`i`)**
    - **Descrição:** Aplica o estilo de itálico ao texto.
    - **Uso:** `Uma palavra em {i|itálico}.`

- **Negrito e Itálico (`ni`)**
    - **Descrição:** Aplica ambos os estilos, negrito e itálico.
    - **Uso:** `Texto com {ni|ênfase dupla}.`

- **Sublinhado (`s`)**
    - **Descrição:** Sublinha o texto.
    - **Uso:** `Uma palavra {s|sublinhada}.`

- **Link (`link` ou `url`)**
    - **Descrição:** Cria um hiperlink clicável. O primeiro parâmetro é a URL, e o segundo é o texto
      que será exibido.
    - **Uso:** `{link|https://www.google.com|Clique aqui para pesquisar}`

- **Tooltip/Dica (`tooltip`)**
    - **Descrição:** Cria um texto clicável que exibe uma dica (tooltip). O primeiro parâmetro é o
      texto da dica, e o segundo é o texto que ficará visível no parágrafo.
    - **Uso:** `A {tooltip|Esta é a dica!|palavra} tem uma dica.`

