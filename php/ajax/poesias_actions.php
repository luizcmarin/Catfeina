<?php
require_once '../includes/db.php';

header('Content-Type: application/json');

// --- FUNÇÕES AUXILIARES PARA MARKDOWN ---

/**
 * Monta o conteúdo Markdown a partir dos campos do formulário.
 */
function compor_markdown_da_poesia($data) {
    $markdown = [];

    if (!empty($data['titulo'])) {
        $markdown[] = '# ' . trim($data['titulo']);
        $markdown[] = "\n";
    }

    // Combina os campos de texto em um só, como no app.
    if (!empty($data['texto'])) {
        $markdown[] = trim($data['texto']);
    }

    $meta = [];
    if (!empty($data['autor'])) {
        $meta[] = '*Autor: ' . trim($data['autor']) . '*';
    }
    if (!empty($data['nota'])) {
        $meta[] = '*Nota: ' . trim($data['nota']) . '*';
    }

    if (!empty($meta)) {
        $markdown[] = "\n---";
        $markdown = array_merge($markdown, $meta);
    }

    return implode("\n", $markdown);
}

/**
 * Desmonta o conteúdo Markdown para preencher os campos do formulário.
 */
function decompor_markdown_para_poesia($markdown_texto) {
    $linhas = explode("\n", $markdown_texto);
    $dados = [
        'titulo' => '',
        'texto' => '',
        'autor' => '',
        'nota' => ''
    ];
    $parte_atual = 'titulo'; // titulo, corpo, meta
    $corpo_texto = [];

    foreach ($linhas as $linha) {
        $linha_trim = trim($linha);
        if (strpos($linha_trim, '# ') === 0 && $parte_atual === 'titulo') {
            $dados['titulo'] = substr($linha_trim, 2);
            $parte_atual = 'corpo';
        } elseif ($linha_trim === '---') {
            $parte_atual = 'meta';
        } elseif ($parte_atual === 'corpo') {
            if($linha_trim !== '') $corpo_texto[] = $linha;
        } elseif ($parte_atual === 'meta') {
            if (strpos($linha_trim, '*Autor:') === 0) {
                $dados['autor'] = trim(str_replace('*', '', substr($linha_trim, 7)));
            } elseif (strpos($linha_trim, '*Nota:') === 0) {
                $dados['nota'] = trim(str_replace('*', '', substr($linha_trim, 6)));
            }
        }
    }
    $dados['texto'] = implode("\n", $corpo_texto);
    return $dados;
}

// --- LÓGICA PRINCIPAL ---

$response = ['status' => 'error', 'message' => 'Ação inválida.'];
$action = $_POST['action'] ?? $_GET['action'] ?? null;

if ($action) {
    switch ($action) {
        case 'add':
            try {
                $texto_markdown = compor_markdown_da_poesia($_POST);
                $novo_id = ($_POST['id'] ?? $pdo->query('SELECT MAX(id) FROM tbl_poesia')->fetchColumn() + 1);

                $sql = "INSERT INTO tbl_poesia (id, texto, anterior, proximo, atualizadoem) VALUES (?, ?, ?, ?, ?)";
                $stmt = $pdo->prepare($sql);
                $stmt->execute([
                    $novo_id,
                    $texto_markdown,
                    $_POST['anterior'] ?: null,
                    $_POST['proximo'] ?: null,
                    time() // Salva como timestamp
                ]);
                $response = ['status' => 'success', 'message' => 'Poesia adicionada com sucesso!', 'id' => $novo_id];
            } catch (PDOException $e) {
                $response = ['status' => 'error', 'message' => 'Erro ao adicionar poesia: ' . $e->getMessage()];
            }
            break;

        case 'delete':
            if (isset($_POST['id'])) {
                try {
                    $stmt = $pdo->prepare("DELETE FROM tbl_poesia WHERE id = ?");
                    $stmt->execute([$_POST['id']]);
                    $response = ['status' => 'success', 'message' => 'Poesia excluída com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao excluir poesia: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'get':
            if (isset($_GET['id'])) {
                try {
                    $stmt = $pdo->prepare("SELECT * FROM tbl_poesia WHERE id = ?");
                    $stmt->execute([$_GET['id']]);
                    $poesia = $stmt->fetch(PDO::FETCH_ASSOC);
                    if ($poesia) {
                        $dados_poesia = decompor_markdown_para_poesia($poesia['texto']);
                        // Adiciona os outros campos ao resultado
                        $dados_poesia['id'] = $poesia['id'];
                        $dados_poesia['anterior'] = $poesia['anterior'];
                        $dados_poesia['proximo'] = $poesia['proximo'];
                        $response = ['status' => 'success', 'data' => $dados_poesia];
                    } else {
                        $response = ['status' => 'error', 'message' => 'Poesia não encontrada.'];
                    }
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao buscar poesia: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'update':
             if (isset($_POST['id'])) {
                try {
                    $texto_markdown = compor_markdown_da_poesia($_POST);
                    $sql = "UPDATE tbl_poesia SET texto = ?, anterior = ?, proximo = ?, atualizadoem = ? WHERE id = ?";
                    $stmt = $pdo->prepare($sql);
                    $stmt->execute([
                        $texto_markdown,
                        $_POST['anterior'] ?: null,
                        $_POST['proximo'] ?: null,
                        time(), // Salva como timestamp
                        $_POST['id']
                    ]);
                    $response = ['status' => 'success', 'message' => 'Poesia atualizada com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao atualizar poesia: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido para atualização.'];
            }
            break;
    }
}

echo json_encode($response);
?>
