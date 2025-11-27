<?php
$page_title = 'Informativos';
require_once 'includes/db.php';
require_once 'includes/header.php';

// Leitura dos dados da tabela correta: tbl_informativo
$stmt = $pdo->query('SELECT id, chave, titulo, atualizadoem FROM tbl_informativo ORDER BY chave');
$informativos = $stmt->fetchAll();

?>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h2">Informativos</h1>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addInformativoModal">Adicionar Novo Informativo</button>
</div>

<div class="table-responsive">
    <table class="table table-striped table-sm">
        <thead>
            <tr>
                <th>ID</th>
                <th>Chave</th>
                <th>Título</th>
                <th>Atualizado Em</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody id="informativosTableBody">
            <?php foreach ($informativos as $info): ?>
                <tr id="informativo-<?php echo $info['id']; ?>">
                    <td><?php echo htmlspecialchars($info['id']); ?></td>
                    <td class="informativo-chave"><?php echo htmlspecialchars($info['chave']); ?></td>
                    <td class="informativo-titulo"><?php echo htmlspecialchars($info['titulo']); ?></td>
                     <!-- Ajuste para formatar a data a partir do texto -->
                    <td class="informativo-atualizadoem"><?php echo htmlspecialchars(date('d/m/Y H:i', strtotime($info['atualizadoem']))); ?></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-outline-secondary edit-informativo" data-id="<?php echo $info['id']; ?>" data-bs-toggle="modal" data-bs-target="#editInformativoModal">Editar</button>
                        <button type="button" class="btn btn-sm btn-outline-danger delete-informativo" data-id="<?php echo $info['id']; ?>">Excluir</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<!-- Modal Adicionar Informativo (Estrutura Mantida) -->
<div class="modal fade" id="addInformativoModal" tabindex="-1" aria-labelledby="addInformativoModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addInformativoModalLabel">Adicionar Novo Informativo</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="addInformativoForm">
          <div class="mb-3">
            <label for="chave" class="form-label">Chave (identificador único)</label>
            <input type="text" class="form-control" id="chave" name="chave" required>
          </div>
          <div class="mb-3">
            <label for="titulo" class="form-label">Título</label>
            <input type="text" class="form-control" id="titulo" name="titulo" required>
          </div>
          <div class="mb-3">
            <label for="conteudo" class="form-label">Conteúdo</label>
            <textarea class="form-control" id="conteudo" name="conteudo" rows="8"></textarea>
          </div>
          <div class="mb-3">
            <label for="imagem" class="form-label">Imagem (URL)</label>
            <input type="text" class="form-control" id="imagem" name="imagem">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="addInformativoForm">Salvar</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal Editar Informativo (Estrutura Mantida) -->
<div class="modal fade" id="editInformativoModal" tabindex="-1" aria-labelledby="editInformativoModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editInformativoModalLabel">Editar Informativo</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editInformativoForm">
          <input type="hidden" id="edit_informativo_id" name="id">
          <div class="mb-3">
            <label for="edit_chave" class="form-label">Chave (identificador único)</label>
            <input type="text" class="form-control" id="edit_chave" name="chave" required>
          </div>
          <div class="mb-3">
            <label for="edit_titulo" class="form-label">Título</label>
            <input type="text" class="form-control" id="edit_titulo" name="titulo" required>
          </div>
          <div class="mb-3">
            <label for="edit_conteudo" class="form-label">Conteúdo</label>
            <textarea class="form-control" id="edit_conteudo" name="conteudo" rows="8"></textarea>
          </div>
          <div class="mb-3">
            <label for="edit_imagem" class="form-label">Imagem (URL)</label>
            <input type="text" class="form-control" id="edit_imagem" name="imagem">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="editInformativoForm">Salvar Alterações</button>
      </div>
    </div>
  </div>
</div>

<?php
require_once 'includes/footer.php';
?>
