<?php
require_once '../includes/db.php';

header('Content-Type: application/json');

$response = ['status' => 'error', 'message' => 'Ação inválida.'];

$action = $_POST['action'] ?? $_GET['action'] ?? null;

if ($action) {
    switch ($action) {
        case 'add':
            try {
                // CORREÇÃO: Usando a tabela 'tbl_meow' e o formato de data correto.
                $sql = "INSERT INTO tbl_meow (texto, atualizadoem) VALUES (?, ?)";
                $stmt = $pdo->prepare($sql);
                $stmt->execute([
                    $_POST['texto'] ?? '',
                    (new DateTime())->format('Y-m-d H:i:s')
                ]);
                $response = ['status' => 'success', 'message' => 'Meow adicionado com sucesso!'];
            } catch (PDOException $e) {
                $response = ['status' => 'error', 'message' => 'Erro ao adicionar meow: ' . $e->getMessage()];
            }
            break;

        case 'delete':
            if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_meow'.
                    $stmt = $pdo->prepare("DELETE FROM tbl_meow WHERE id = ?");
                    $stmt->execute([$_POST['id']]);
                    $response = ['status' => 'success', 'message' => 'Meow excluído com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao excluir meow: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'get':
            if (isset($_GET['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_meow'.
                    $stmt = $pdo->prepare("SELECT * FROM tbl_meow WHERE id = ?");
                    $stmt->execute([$_GET['id']]);
                    $meow = $stmt->fetch();
                    if ($meow) {
                        $response = ['status' => 'success', 'data' => $meow];
                    } else {
                        $response = ['status' => 'error', 'message' => 'Meow não encontrado.'];
                    }
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao buscar meow: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido.'];
            }
            break;

        case 'update':
             if (isset($_POST['id'])) {
                try {
                    // CORREÇÃO: Usando a tabela 'tbl_meow' e o formato de data correto.
                    $sql = "UPDATE tbl_meow SET texto = ?, atualizadoem = ? WHERE id = ?";
                    $stmt = $pdo->prepare($sql);
                    $stmt->execute([
                        $_POST['texto'] ?? '',
                        (new DateTime())->format('Y-m-d H:i:s'),
                        $_POST['id']
                    ]);
                    $response = ['status' => 'success', 'message' => 'Meow atualizado com sucesso!'];
                } catch (PDOException $e) {
                    $response = ['status' => 'error', 'message' => 'Erro ao atualizar meow: ' . $e->getMessage()];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'ID não fornecido para atualização.'];
            }
            break;
    }
}

echo json_encode($response);
?>
