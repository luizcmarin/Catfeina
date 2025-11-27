<?php
$page_title = 'Meow';
require_once 'includes/db.php';
require_once 'includes/header.php';

// Leitura dos dados da tabela correta: tbl_meow
$stmt = $pdo->query('SELECT id, texto, atualizadoem FROM tbl_meow ORDER BY id DESC');
$meows = $stmt->fetchAll();

?>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h2">Meow</h1>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addMeowModal">Adicionar Novo Meow</button>
</div>

<div class="table-responsive">
    <table class="table table-striped table-sm">
        <thead>
            <tr>
                <th>ID</th>
                <th>Texto</th>
                <th>Atualizado Em</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody id="meowTableBody">
            <?php foreach ($meows as $meow): ?>
                <tr id="meow-<?php echo $meow['id']; ?>">
                    <td><?php echo htmlspecialchars($meow['id']); ?></td>
                    <td class="meow-texto"><?php echo htmlspecialchars($meow['texto']); ?></td>
                    <!-- Ajuste para formatar a data a partir do texto -->
                    <td class="meow-atualizadoem"><?php echo htmlspecialchars(date('d/m/Y H:i', strtotime($meow['atualizadoem']))); ?></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-outline-secondary edit-meow" data-id="<?php echo $meow['id']; ?>" data-bs-toggle="modal" data-bs-target="#editMeowModal">Editar</button>
                        <button type="button" class="btn btn-sm btn-outline-danger delete-meow" data-id="<?php echo $meow['id']; ?>">Excluir</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<!-- Modal Adicionar Meow (Estrutura Mantida) -->
<div class="modal fade" id="addMeowModal" tabindex="-1" aria-labelledby="addMeowModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addMeowModalLabel">Adicionar Meow</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="addMeowForm">
          <div class="mb-3">
            <label for="texto" class="form-label">Texto</label>
            <textarea class="form-control" id="texto" name="texto" rows="3" required></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="addMeowForm">Salvar</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal Editar Meow (Estrutura Mantida) -->
<div class="modal fade" id="editMeowModal" tabindex="-1" aria-labelledby="editMeowModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editMeowModalLabel">Editar Meow</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editMeowForm">
          <input type="hidden" name="id">
          <div class="mb-3">
            <label for="edit_texto" class="form-label">Texto</label>
            <textarea class="form-control" id="edit_texto" name="texto" rows="3" required></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="editMeowForm">Salvar Alterações</button>
      </div>
    </div>
  </div>
</div>

<?php
require_once 'includes/footer.php';
?>
