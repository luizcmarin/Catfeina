<?php
require_once '../includes/db.php';

header('Content-Type: application/json');

$response = ['status' => 'error', 'message' => 'Ação inválida.'];

$action = $_POST['action'] ?? $_GET['action'] ?? null;

if ($action) {
    switch ($action) {
        case 'add':
            try {
                // CORREÇÃO: Usando a tabela 'tbl_personagem' e o formato de data correto.
                $sql = "INSERT INTO tbl_personagem (nome, biografia, imagem, atualizadoem) VALUES (?, ?, ?, ?)";
                $stmt = $pdo->prepare($sql);
                $stmt->execute([
                    $_POST['nome'] ?? '',
                    $_POST['biografia'] ?? '',
                    $_POST['imagem'] ?: null,
                    (new DateTime())->format('Y-m-d H:i:s')
                ]);
                $response = ['status' => 'success', 'message' => 'Personagem adicionado com sucesso!'];
            } catch (PDOException $e) {
                $response = ['status' => 'error', 'message' => 'Erro ao adicionar personagem: ' . $e->getMessage()];
            }
            break;

        case 'delete':
            if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_personagem'.
                    $stmt = $pdo->prepare("DELETE FROM tbl_personagem WHERE id = ?");
                    $stmt->execute([$_POST['id']]);
                    $response = ['status' => 'success', 'message' => 'Personagem excluído com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao excluir personagem: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'get':
            if (isset($_GET['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_personagem'.
                    $stmt = $pdo->prepare("SELECT * FROM tbl_personagem WHERE id = ?");
                    $stmt->execute([$_GET['id']]);
                    $personagem = $stmt->fetch();
                    if ($personagem) {
                        $response = ['status' => 'success', 'data' => $personagem];
                    } else {
                        $response = ['status' => 'error', 'message' => 'Personagem não encontrado.'];
                    }
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao buscar personagem: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'update':
            if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_personagem' e o formato de data correto.
                    $sql = "UPDATE tbl_personagem SET nome = ?, biografia = ?, imagem = ?, atualizadoem = ? WHERE id = ?";
                    $stmt = $pdo->prepare($sql);
                    $stmt->execute([
                        $_POST['nome'] ?? '',
                        $_POST['biografia'] ?? '',
                        $_POST['imagem'] ?: null,
                        (new DateTime())->format('Y-m-d H:i:s'),
                        $_POST['id']
                    ]);
                    $response = ['status' => 'success', 'message' => 'Personagem atualizado com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao atualizar personagem: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido para atualização.'];
            }
            break;
    }
}

echo json_encode($response);
?>
