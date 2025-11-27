<?php
require_once '../includes/db.php';

header('Content-Type: application/json');

$response = ['status' => 'error', 'message' => 'Ação inválida.'];

if (isset($_POST['action']) && $_POST['action'] == 'delete') {
    if (isset($_POST['id'])) {
        try {
            // CORREÇÃO: Usando a tabela 'tbl_historico'.
            $stmt = $pdo->prepare("DELETE FROM tbl_historico WHERE id = ?");
            $stmt->execute([$_POST['id']]);
            
            if ($stmt->rowCount() > 0) {
                $response = ['status' => 'success', 'message' => 'Registro de histórico excluído com sucesso!'];
            } else {
                $response = ['status' => 'error', 'message' => 'Registro não encontrado.'];
            }
        } catch (PDOException $e) {
            $response = ['status' => 'error', 'message' => 'Erro ao excluir registro: ' . $e->getMessage()];
        }
    } else {
        $response = ['status' => 'error', 'message' => 'ID do registro não fornecido.'];
    }
}

echo json_encode($response);
?>
