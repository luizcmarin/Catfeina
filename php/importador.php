<?php

/**
 * Função principal para processar o arquivo Markdown e importar para o banco de dados.
 */
function importar_poesias_do_markdown() {
    $caminho_arquivo = 'teste.md';
    if (!file_exists($caminho_arquivo)) {
        return "<div class='alert alert-danger'>Erro: Arquivo <code>{$caminho_arquivo}</code> não encontrado.</div>";
    }

    $conteudo_md = file_get_contents($caminho_arquivo);
    $conteudo_md = str_replace('--', '—', $conteudo_md);
    $conteudo_md = str_replace('---', '—', $conteudo_md);
    $conteudo_md = "\n" . trim($conteudo_md);
    $blocos = preg_split('/(?=\n\d+\.\s)/', $conteudo_md, -1, PREG_SPLIT_NO_EMPTY);

    $poesias_processadas = [];
    $id_atual = 1;

    foreach ($blocos as $bloco) {
        $linhas_bloco = explode("\n", trim($bloco));

        $titulo = 'Poesia sem título';
        $imagem = '';
        $linhas_de_conteudo = [];

        // 1. Extrai título, imagem e separa as linhas de conteúdo.
        foreach ($linhas_bloco as $linha) {
            $linha_limpa = str_replace('\\', '', $linha);
            $linha_trim = trim($linha_limpa);

            if (preg_match('/^\d+\.\s+#/', $linha_trim)) {
                $temp = preg_replace(['/^\d+\.\s+#\s?/', '/(\[\])?\{#.*?\}/'], '', $linha_trim);
                $titulo = trim($temp);
            } elseif (preg_match('/!\[.*?\]\((.*?)\)(?:\{.*\})?/i', $linha_trim, $matches)) {
                $imagem_original = basename($matches[1]);
                $imagem = pathinfo($imagem_original, PATHINFO_FILENAME) . '.webp';
            } else {
                $linhas_de_conteudo[] = $linha_limpa;
            }
        }

        // 2. Separa citação e corpo do texto.
        $texto_base_cru = [];
        $conteudo_cru = [];
        $estado = 'inicio';

        $primeira_linha_de_conteudo_encontrada = false;
        foreach($linhas_de_conteudo as $linha_conteudo) {
            $linha_conteudo_trim = trim($linha_conteudo);

            if (!$primeira_linha_de_conteudo_encontrada && !empty($linha_conteudo_trim)) {
                $primeira_linha_de_conteudo_encontrada = true;
                if (strpos($linha_conteudo_trim, '"') === 0 || strpos($linha_conteudo_trim, '*') === 0) {
                    $estado = 'texto_base';
                } else {
                    $estado = 'conteudo';
                }
            }

            if ($estado === 'texto_base') {
                if (empty($linha_conteudo_trim)) {
                    $estado = 'conteudo'; // Muda de estado ao encontrar a primeira linha em branco
                    continue;
                }
                $texto_base_cru[] = $linha_conteudo_trim;
            } elseif($estado === 'conteudo') {
                $conteudo_cru[] = $linha_conteudo; // Preserva linhas em branco do corpo
            }
        }

        // 3. Formata as partes.
        $texto_base_formatado = '';
        if (!empty($texto_base_cru)) {
            $paragrafo_base = trim(implode(' ', $texto_base_cru));
            $texto_base_formatado = '> ***' . $paragrafo_base . '***';
        }

        $paragrafos_conteudo = [];
        $paragrafo_atual = '';
        foreach($conteudo_cru as $linha_paragrafo) {
            $linha_paragrafo_trim = trim($linha_paragrafo);
            if($linha_paragrafo_trim === '') {
                if(!empty($paragrafo_atual)) {
                    $paragrafos_conteudo[] = trim($paragrafo_atual);
                    $paragrafo_atual = '';
                }
            } else {
                $paragrafo_atual .= $linha_paragrafo_trim . ' ';
            }
        }
        if(!empty($paragrafo_atual)) {
            $paragrafos_conteudo[] = trim($paragrafo_atual);
        }
        $conteudo_formatado = implode("\n\n", $paragrafos_conteudo);

        // 4. Monta o texto final.
        $partes = [];
        if (!empty($imagem)) $partes[] = '![](' . $imagem . ')';
        if (!empty($titulo)) $partes[] = '# ' . $titulo . "\n"; // Adiciona espaço extra aqui
        if (!empty($texto_base_formatado)) $partes[] = $texto_base_formatado;
        if (!empty($conteudo_formatado)) $partes[] = $conteudo_formatado;

        $texto_final_db = implode("\n\n", $partes);

        $poesias_processadas[] = [
            'id' => $id_atual,
            'texto' => $texto_final_db,
            'anterior' => ($id_atual > 1) ? $id_atual - 1 : null,
            'proximo' => null
        ];
        $id_atual++;
    }

    for ($i = 0; $i < count($poesias_processadas) - 1; $i++) {
        $poesias_processadas[$i]['proximo'] = $poesias_processadas[$i]['id'] + 1;
    }

    try {
        require_once 'includes/db.php'; // $pdo
        $pdo->beginTransaction();
        $pdo->exec('DELETE FROM tbl_poesia');
        $pdo->exec('DELETE FROM sqlite_sequence WHERE name=\'tbl_poesia\'');
        $stmt = $pdo->prepare("INSERT INTO tbl_poesia (id, texto, anterior, proximo, atualizadoem) VALUES (?, ?, ?, ?, ?)");

        foreach ($poesias_processadas as $p) {
            $stmt->execute([$p['id'], $p['texto'], $p['anterior'], $p['proximo'], time()]);
        }

        $pdo->commit();
        return "<div class='alert alert-success'>" . count($poesias_processadas) . " poesias foram importadas com sucesso!</div>";
    } catch (Exception $e) {
        if (isset($pdo) && $pdo->inTransaction()) $pdo->rollBack();
        return "<div class='alert alert-danger'>Erro na importação: " . $e->getMessage() . "</div>";
    }
}

// --- Interface do Usuário ---
$page_title = 'Importador de Poesias';
require_once 'includes/header.php';

echo '<div class="container-fluid"> ';
echo '<h1 class="h2 mt-3">Importador de Poesias</h1>';
echo '<p>Este script lê o arquivo <code>teste.md</code>, processa as poesias e as insere no banco de dados, **substituindo todos os dados existentes na tabela de poesias**.</p>';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $resultado = importar_poesias_do_markdown();
    echo $resultado;
} else {
    echo '<form method="POST"><button type="submit" class="btn btn-primary">Iniciar Importação</button></form>';
}

echo '</div>';

require_once 'includes/footer.php';

?>
