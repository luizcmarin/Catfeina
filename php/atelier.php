<?php
$page_title = 'Atelier';
require_once 'includes/db.php';
require_once 'includes/header.php';

// Leitura dos dados da tabela correta: tbl_atelier
$stmt = $pdo->query('SELECT id, titulo, fixada, atualizadoem FROM tbl_atelier ORDER BY fixada DESC, atualizadoem DESC');
$atelier_items = $stmt->fetchAll();

?>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h2">Atelier (Notas e Rascunhos)</h1>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addAtelierModal">Adicionar Anotação</button>
</div>

<div class="table-responsive">
    <table class="table table-striped table-sm">
        <thead>
            <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Fixada</th>
                <th>Atualizado Em</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody id="atelierTableBody">
            <?php foreach ($atelier_items as $item): ?>
                <tr id="atelier-<?php echo $item['id']; ?>">
                    <td><?php echo htmlspecialchars($item['id']); ?></td>
                    <td class="atelier-titulo"><?php echo htmlspecialchars($item['titulo']); ?></td>
                    <td class="atelier-fixada"><?php echo $item['fixada'] ? 'Sim' : 'Não'; ?></td>
                     <!-- Ajuste para formatar a data a partir do texto -->
                    <td class="atelier-atualizadoem"><?php echo htmlspecialchars(date('d/m/Y H:i', strtotime($item['atualizadoem']))); ?></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-outline-secondary edit-atelier" data-id="<?php echo $item['id']; ?>" data-bs-toggle="modal" data-bs-target="#editAtelierModal">Editar</button>
                        <button type="button" class="btn btn-sm btn-outline-danger delete-atelier" data-id="<?php echo $item['id']; ?>">Excluir</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<!-- Modal Adicionar Anotação (Estrutura Mantida) -->
<div class="modal fade" id="addAtelierModal" tabindex="-1" aria-labelledby="addAtelierModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addAtelierModalLabel">Adicionar Anotação</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="addAtelierForm">
          <div class="mb-3">
            <label for="titulo" class="form-label">Título</label>
            <input type="text" class="form-control" id="titulo" name="titulo" required>
          </div>
          <div class="mb-3">
            <label for="texto" class="form-label">Texto</label>
            <textarea class="form-control" id="texto" name="texto" rows="8"></textarea>
          </div>
          <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" value="1" id="fixada" name="fixada">
            <label class="form-check-label" for="fixada">
              Fixar no topo
            </label>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="addAtelierForm">Salvar</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal Editar Anotação (Estrutura Mantida) -->
<div class="modal fade" id="editAtelierModal" tabindex="-1" aria-labelledby="editAtelierModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editAtelierModalLabel">Editar Anotação</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editAtelierForm">
          <input type="hidden" id="edit_atelier_id" name="id">
          <div class="mb-3">
            <label for="edit_titulo" class="form-label">Título</label>
            <input type="text" class="form-control" id="edit_titulo" name="titulo" required>
          </div>
          <div class="mb-3">
            <label for="edit_texto" class="form-label">Texto</label>
            <textarea class="form-control" id="edit_texto" name="texto" rows="8"></textarea>
          </div>
          <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" value="1" id="edit_fixada" name="fixada">
            <label class="form-check-label" for="edit_fixada">
              Fixar no topo
            </label>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="editAtelierForm">Salvar Alterações</button>
      </div>
    </div>
  </div>
</div>

<?php
require_once 'includes/footer.php';
?>
