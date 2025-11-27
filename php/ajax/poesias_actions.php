<?php
require_once '../includes/db.php';

header('Content-Type: application/json');

$response = ['status' => 'error', 'message' => 'Ação inválida.'];

$action = $_POST['action'] ?? $_GET['action'] ?? null;

if ($action) {
    switch ($action) {
        case 'add':
            try {
                // CORREÇÃO: Usando a tabela 'tbl_poesia' e o formato de data correto.
                $sql = "INSERT INTO tbl_poesia (titulo, textobase, texto, textofinal, imagem, autor, nota, atualizadoem) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                $stmt = $pdo->prepare($sql);
                $stmt->execute([
                    $_POST['titulo'] ?? '',
                    $_POST['textobase'] ?? '',
                    $_POST['texto'] ?? '',
                    $_POST['textofinal'] ?: null,
                    $_POST['imagem'] ?: null,
                    $_POST['autor'] ?: null,
                    $_POST['nota'] ?: null,
                    (new DateTime())->format('Y-m-d H:i:s')
                ]);
                $response = ['status' => 'success', 'message' => 'Poesia adicionada com sucesso!'];
            } catch (PDOException $e) {
                $response = ['status' => 'error', 'message' => 'Erro ao adicionar poesia: ' . $e->getMessage()];
            }
            break;

        case 'delete':
            if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_poesia'.
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
                    // CORREÇÃO: Usando a tabela 'tbl_poesia'.
                    $stmt = $pdo->prepare("SELECT * FROM tbl_poesia WHERE id = ?");
                    $stmt->execute([$_GET['id']]);
                    $poesia = $stmt->fetch();
                    if ($poesia) {
                        $response = ['status' => 'success', 'data' => $poesia];
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
                    // CORREÇÃO: Usando a tabela 'tbl_poesia' e o formato de data correto.
                    $sql = "UPDATE tbl_poesia SET titulo = ?, textobase = ?, texto = ?, textofinal = ?, imagem = ?, autor = ?, nota = ?, atualizadoem = ? WHERE id = ?";
                    $stmt = $pdo->prepare($sql);
                    $stmt->execute([
                        $_POST['titulo'] ?? '',
                        $_POST['textobase'] ?? '',
                        $_POST['texto'] ?? '',
                        $_POST['textofinal'] ?: null,
                        $_POST['imagem'] ?: null,
                        $_POST['autor'] ?: null,
                        $_POST['nota'] ?: null,
                        (new DateTime())->format('Y-m-d H:i:s'),
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
