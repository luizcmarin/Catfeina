<?php
$page_title = 'Personagens';
require_once 'includes/db.php';
require_once 'includes/header.php';

// Leitura dos dados da tabela correta: tbl_personagem
$stmt = $pdo->query('SELECT id, nome, atualizadoem FROM tbl_personagem ORDER BY nome');
$personagens = $stmt->fetchAll();

?>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h2">Personagens</h1>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addPersonagemModal">Adicionar Novo Personagem</button>
</div>

<div class="table-responsive">
    <table class="table table-striped table-sm">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Atualizado Em</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody id="personagensTableBody">
            <?php foreach ($personagens as $personagem): ?>
                <tr id="personagem-<?php echo $personagem['id']; ?>">
                    <td><?php echo htmlspecialchars($personagem['id']); ?></td>
                    <td class="personagem-nome"><?php echo htmlspecialchars($personagem['nome']); ?></td>
                    <!-- Ajuste para formatar a data a partir do texto -->
                    <td class="personagem-atualizadoem"><?php echo htmlspecialchars(date('d/m/Y H:i', strtotime($personagem['atualizadoem']))); ?></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-outline-secondary edit-personagem" data-id="<?php echo $personagem['id']; ?>" data-bs-toggle="modal" data-bs-target="#editPersonagemModal">Editar</button>
                        <button type="button" class="btn btn-sm btn-outline-danger delete-personagem" data-id="<?php echo $personagem['id']; ?>">Excluir</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<!-- Modal Adicionar Personagem (Estrutura Mantida) -->
<div class="modal fade" id="addPersonagemModal" tabindex="-1" aria-labelledby="addPersonagemModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addPersonagemModalLabel">Adicionar Novo Personagem</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="addPersonagemForm">
          <div class="mb-3">
            <label for="add_nome" class="form-label">Nome</label>
            <input type="text" class="form-control" id="add_nome" name="nome" required>
          </div>
          <div class="mb-3">
            <label for="add_biografia" class="form-label">Biografia</label>
            <textarea class="form-control" id="add_biografia" name="biografia" rows="5"></textarea>
          </div>
          <div class="mb-3">
            <label for="add_imagem" class="form-label">Imagem (URL)</label>
            <input type="text" class="form-control" id="add_imagem" name="imagem">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="addPersonagemForm">Salvar</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal Editar Personagem (Estrutura Mantida) -->
<div class="modal fade" id="editPersonagemModal" tabindex="-1" aria-labelledby="editPersonagemModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editPersonagemModalLabel">Editar Personagem</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editPersonagemForm">
          <input type="hidden" name="id">
          <div class="mb-3">
            <label for="edit_nome" class="form-label">Nome</label>
            <input type="text" class="form-control" id="edit_nome" name="nome" required>
          </div>
          <div class="mb-3">
            <label for="edit_biografia" class="form-label">Biografia</label>
            <textarea class="form-control" id="edit_biografia" name="biografia" rows="5"></textarea>
          </div>
          <div class="mb-3">
            <label for="edit_imagem" class="form-label">Imagem (URL)</label>
            <input type="text" class="form-control" id="edit_imagem" name="imagem">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="editPersonagemForm">Salvar Alterações</button>
      </div>
    </div>
  </div>
</div>

<?php
require_once 'includes/footer.php';
?>
