<?php
require_once '../includes/db.php';

header('Content-Type: application/json');

$response = ['status' => 'error', 'message' => 'Ação inválida.'];

$action = $_POST['action'] ?? $_GET['action'] ?? null;

if ($action) {
    switch ($action) {
        case 'add':
            try {
                // CORREÇÃO: Usando a tabela 'tbl_informativo' e o formato de data correto.
                $sql = "INSERT INTO tbl_informativo (chave, titulo, conteudo, imagem, atualizadoem) VALUES (?, ?, ?, ?, ?)";
                $stmt = $pdo->prepare($sql);
                $stmt->execute([
                    $_POST['chave'] ?? '',
                    $_POST['titulo'] ?? '',
                    $_POST['conteudo'] ?? '',
                    $_POST['imagem'] ?: null,
                    (new DateTime())->format('Y-m-d H:i:s')
                ]);
                $response = ['status' => 'success', 'message' => 'Informativo adicionado com sucesso!'];
            } catch (PDOException $e) {
                $response = ['status' => 'error', 'message' => 'Erro ao adicionar informativo: ' . $e->getMessage()];
            }
            break;

        case 'delete':
            if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_informativo'.
                    $stmt = $pdo->prepare("DELETE FROM tbl_informativo WHERE id = ?");
                    $stmt->execute([$_POST['id']]);
                    $response = ['status' => 'success', 'message' => 'Informativo excluído com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao excluir informativo: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'get':
            if (isset($_GET['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_informativo'.
                    $stmt = $pdo->prepare("SELECT * FROM tbl_informativo WHERE id = ?");
                    $stmt->execute([$_GET['id']]);
                    $info = $stmt->fetch();
                    if ($info) {
                        $response = ['status' => 'success', 'data' => $info];
                    } else {
                        $response = ['status' => 'error', 'message' => 'Informativo não encontrado.'];
                    }
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao buscar informativo: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'update':
            if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_informativo' e o formato de data correto.
                    $sql = "UPDATE tbl_informativo SET chave = ?, titulo = ?, conteudo = ?, imagem = ?, atualizadoem = ? WHERE id = ?";
                    $stmt = $pdo->prepare($sql);
                    $stmt->execute([
                        $_POST['chave'] ?? '',
                        $_POST['titulo'] ?? '',
                        $_POST['conteudo'] ?? '',
                        $_POST['imagem'] ?: null,
                        (new DateTime())->format('Y-m-d H:i:s'),
                        $_POST['id']
                    ]);
                    $response = ['status' => 'success', 'message' => 'Informativo atualizado com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao atualizar informativo: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido para atualização.'];
            }
            break;
    }
}

echo json_encode($response);
?>
