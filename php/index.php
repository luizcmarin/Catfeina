<?php
$page_title = 'Poesias';
require_once 'includes/db.php';
require_once 'includes/header.php';

// Leitura dos dados da tabela correta: tbl_poesia
$stmt = $pdo->query('SELECT id, titulo, autor, atualizadoem FROM tbl_poesia ORDER BY id DESC');
$poesias = $stmt->fetchAll();

?>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h2">Poesias</h1>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addPoesiaModal">Adicionar Nova Poesia</button>
</div>

<div class="table-responsive">
    <table class="table table-striped table-sm">
        <thead>
            <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Autor</th>
                <th>Atualizado Em</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody id="poesiasTableBody">
            <?php foreach ($poesias as $poesia): ?>
                <tr id="poesia-<?php echo $poesia['id']; ?>">
                    <td><?php echo htmlspecialchars($poesia['id']); ?></td>
                    <td class="poesia-titulo"><?php echo htmlspecialchars($poesia['titulo']); ?></td>
                    <td class="poesia-autor"><?php echo htmlspecialchars($poesia['autor']); ?></td>
                    <!-- Ajuste para formatar a data a partir do texto -->
                    <td class="poesia-atualizadoem"><?php echo htmlspecialchars(date('d/m/Y H:i', strtotime($poesia['atualizadoem']))); ?></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-outline-secondary edit-poesia" data-id="<?php echo $poesia['id']; ?>" data-bs-toggle="modal" data-bs-target="#editPoesiaModal">Editar</button>
                        <button type="button" class="btn btn-sm btn-outline-danger delete-poesia" data-id="<?php echo $poesia['id']; ?>">Excluir</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<!-- Modal Adicionar Poesia (Estrutura Mantida) -->
<div class="modal fade" id="addPoesiaModal" tabindex="-1" aria-labelledby="addPoesiaModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addPoesiaModalLabel">Adicionar Nova Poesia</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="addPoesiaForm">
          <div class="mb-3">
            <label for="titulo" class="form-label">Título</label>
            <input type="text" class="form-control" id="titulo" name="titulo" required>
          </div>
          <div class="mb-3">
            <label for="autor" class="form-label">Autor</label>
            <input type="text" class="form-control" id="autor" name="autor">
          </div>
          <div class="mb-3">
            <label for="textobase" class="form-label">Texto Base</label>
            <textarea class="form-control" id="textobase" name="textobase" rows="3"></textarea>
          </div>
          <div class="mb-3">
            <label for="texto" class="form-label">Texto</label>
            <textarea class="form-control" id="texto" name="texto" rows="5"></textarea>
          </div>
           <div class="mb-3">
            <label for="textofinal" class="form-label">Texto Final</label>
            <textarea class="form-control" id="textofinal" name="textofinal" rows="2"></textarea>
          </div>
          <div class="mb-3">
            <label for="imagem" class="form-label">Imagem (URL)</label>
            <input type="text" class="form-control" id="imagem" name="imagem">
          </div>
           <div class="mb-3">
            <label for="nota" class="form-label">Nota</label>
            <textarea class="form-control" id="nota" name="nota" rows="2"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="addPoesiaForm">Salvar</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal Editar Poesia (Estrutura Mantida) -->
<div class="modal fade" id="editPoesiaModal" tabindex="-1" aria-labelledby="editPoesiaModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editPoesiaModalLabel">Editar Poesia</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editPoesiaForm">
          <input type="hidden" id="edit_poesia_id" name="id">
          <div class="mb-3">
            <label for="edit_titulo" class="form-label">Título</label>
            <input type="text" class="form-control" id="edit_titulo" name="titulo" required>
          </div>
          <div class="mb-3">
            <label for="edit_autor" class="form-label">Autor</label>
            <input type="text" class="form-control" id="edit_autor" name="autor">
          </div>
          <div class="mb-3">
            <label for="edit_textobase" class="form-label">Texto Base</label>
            <textarea class="form-control" id="edit_textobase" name="textobase" rows="3"></textarea>
          </div>
          <div class="mb-3">
            <label for="edit_texto" class="form-label">Texto</label>
            <textarea class="form-control" id="edit_texto" name="texto" rows="5"></textarea>
          </div>
           <div class="mb-3">
            <label for="edit_textofinal" class="form-label">Texto Final</label>
            <textarea class="form-control" id="edit_textofinal" name="textofinal" rows="2"></textarea>
          </div>
          <div class="mb-3">
            <label for="edit_imagem" class="form-label">Imagem (URL)</label>
            <input type="text" class="form-control" id="edit_imagem" name="imagem">
          </div>
           <div class="mb-3">
            <label for="edit_nota" class="form-label">Nota</label>
            <textarea class="form-control" id="edit_nota" name="nota" rows="2"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
        <button type="submit" class="btn btn-primary" form="editPoesiaForm">Salvar Alterações</button>
      </div>
    </div>
  </div>
</div>

<?php
require_once 'includes/footer.php';
?>
