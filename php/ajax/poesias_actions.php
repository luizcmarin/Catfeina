<?php
require_once '../includes/db.php';

header('Content-Type: application/json');

// --- LÓGICA PRINCIPAL ---

$response = ['status' => 'error', 'message' => 'Ação inválida.'];
$action = $_POST['action'] ?? $_GET['action'] ?? null;

if ($action) {
    switch ($action) {
        case 'add':
            try {
                $texto_markdown = $_POST['texto'] ?? '';
                $novo_id = ($_POST['id'] ?? $pdo->query('SELECT MAX(id) FROM tbl_poesia')->fetchColumn() + 1);

                $sql = "INSERT INTO tbl_poesia (id, texto, anterior, proximo, atualizadoem) VALUES (?, ?, ?, ?, ?)";
                $stmt = $pdo->prepare($sql);
                $stmt->execute([
                    $novo_id,
                    $texto_markdown,
                    $_POST['anterior'] ?: null,
                    $_POST['proximo'] ?: null,
                    time()
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
                        // CORREÇÃO: Envia o registro completo, sem desmontar o campo 'texto'.
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
                    $texto_markdown = $_POST['texto'] ?? '';
                    $sql = "UPDATE tbl_poesia SET texto = ?, anterior = ?, proximo = ?, atualizadoem = ? WHERE id = ?";
                    $stmt = $pdo->prepare($sql);
                    $stmt->execute([
                        $texto_markdown,
                        $_POST['anterior'] ?: null,
                        $_POST['proximo'] ?: null,
                        time(),
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
