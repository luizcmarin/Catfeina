<?php
require_once '../includes/db.php';

header('Content-Type: application/json');

$response = ['status' => 'error', 'message' => 'Ação inválida.'];

$action = $_POST['action'] ?? $_GET['action'] ?? null;

if ($action) {
    switch ($action) {
        case 'add':
            try {
                // CORREÇÃO: Usando a tabela 'tbl_atelier' e o formato de data correto.
                $sql = "INSERT INTO tbl_atelier (titulo, texto, fixada, atualizadoem) VALUES (?, ?, ?, ?)";
                $stmt = $pdo->prepare($sql);
                $stmt->execute([
                    $_POST['titulo'] ?? '',
                    $_POST['texto'] ?? '',
                    isset($_POST['fixada']) ? 1 : 0,
                    (new DateTime())->format('Y-m-d H:i:s')
                ]);
                $response = ['status' => 'success', 'message' => 'Anotação adicionada com sucesso!'];
            } catch (PDOException $e) {
                $response = ['status' => 'error', 'message' => 'Erro ao adicionar anotação: ' . $e->getMessage()];
            }
            break;

        case 'delete':
            if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_atelier'.
                    $stmt = $pdo->prepare("DELETE FROM tbl_atelier WHERE id = ?");
                    $stmt->execute([$_POST['id']]);
                    $response = ['status' => 'success', 'message' => 'Anotação excluída com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao excluir anotação: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'get':
            if (isset($_GET['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_atelier'.
                    $stmt = $pdo->prepare("SELECT * FROM tbl_atelier WHERE id = ?");
                    $stmt->execute([$_GET['id']]);
                    $item = $stmt->fetch();
                    if ($item) {
                        $response = ['status' => 'success', 'data' => $item];
                    } else {
                        $response = ['status' => 'error', 'message' => 'Anotação não encontrada.'];
                    }
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao buscar anotação: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'update':
            if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_atelier' e o formato de data correto.
                    $sql = "UPDATE tbl_atelier SET titulo = ?, texto = ?, fixada = ?, atualizadoem = ? WHERE id = ?";
                    $stmt = $pdo->prepare($sql);
                    $stmt->execute([
                        $_POST['titulo'] ?? '',
                        $_POST['texto'] ?? '',
                        isset($_POST['fixada']) ? 1 : 0,
                        (new DateTime())->format('Y-m-d H:i:s'),
                        $_POST['id']
                    ]);
                    $response = ['status' => 'success', 'message' => 'Anotação atualizada com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao atualizar anotação: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido para atualização.'];
            }
            break;
    }
}

echo json_encode($response);
?>
