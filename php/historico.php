<?php
$page_title = 'Histórico';
require_once 'includes/db.php';
require_once 'includes/header.php';

// Leitura dos dados da tabela correta: tbl_historico
$stmt = $pdo->query('SELECT id, tipoconteudo, conteudoid, titulo, vistoem FROM tbl_historico ORDER BY vistoem DESC');
$historico_items = $stmt->fetchAll();

?>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h2">Histórico de Visualização</h1>
</div>

<div class="table-responsive">
    <table class="table table-striped table-sm">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tipo</th>
                <th>Conteúdo ID</th>
                <th>Título</th>
                <th>Visto Em</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody id="historicoTableBody">
            <?php foreach ($historico_items as $item): ?>
                <tr id="historico-<?php echo $item['id']; ?>">
                    <td><?php echo htmlspecialchars($item['id']); ?></td>
                    <td><?php echo htmlspecialchars($item['tipoconteudo']); ?></td>
                    <td><?php echo htmlspecialchars($item['conteudoid']); ?></td>
                    <td><?php echo htmlspecialchars($item['titulo']); ?></td>
                    <!-- Ajuste para formatar a data a partir do texto -->
                    <td><?php echo htmlspecialchars(date('d/m/Y H:i:s', strtotime($item['vistoem']))); ?></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-outline-danger delete-historico" data-id="<?php echo $item['id']; ?>">Excluir</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<?php
require_once 'includes/footer.php';
?>
