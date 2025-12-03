<?php
$page_title = 'Poesias';
require_once 'includes/db.php';
require_once 'includes/header.php';

// CORREÇÃO: Função atualizada para encontrar o título em qualquer lugar do texto.
function extrair_titulo_do_markdown($markdown) {
    if (preg_match('/^#\s+(.*)/m', $markdown, $matches)) {
        return $matches[1];
    }
    return 'Poesia sem título';
}

$stmt = $pdo->query('SELECT id, texto, atualizadoem FROM tbl_poesia ORDER BY id DESC');
$poesias = $stmt->fetchAll(PDO::FETCH_ASSOC);

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
                <th>Atualizado Em</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody id="poesiasTableBody">
            <?php foreach ($poesias as $poesia): ?>
                <tr id="poesia-<?php echo $poesia['id']; ?>">
                    <td><?php echo htmlspecialchars($poesia['id']); ?></td>
                    <td><?php echo htmlspecialchars(extrair_titulo_do_markdown($poesia['texto'])); ?></td>
                    <td><?php echo htmlspecialchars(date('d/m/Y H:i', $poesia['atualizadoem'])); ?></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-outline-secondary edit-poesia" data-id="<?php echo $poesia['id']; ?>">Editar</button>
                        <button type="button" class="btn btn-sm btn-outline-danger delete-poesia" data-id="<?php echo $poesia['id']; ?>">Excluir</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<!-- Modal Adicionar Poesia -->
<div class="modal fade" id="addPoesiaModal" tabindex="-1" aria-labelledby="addPoesiaModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addPoesiaModalLabel">Adicionar Nova Poesia</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="addPoesiaForm">
            <div class="mb-3">
              <textarea class="form-control" id="add_texto" name="texto" rows="15" required spellcheck="true" lang="pt-BR"></textarea>
            </div>
             <div class="row">
              <div class="col-md-6 mb-3">
                <label for="add_anterior" class="form-label">Anterior (ID)</label>
                <input type="number" class="form-control" id="add_anterior" name="anterior">
              </div>
              <div class="col-md-6 mb-3">
                <label for="add_proximo" class="form-label">Próximo (ID)</label>
                <input type="number" class="form-control" id="add_proximo" name="proximo">
              </div>
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

<!-- Modal Editar Poesia -->
<div class="modal fade" id="editPoesiaModal" tabindex="-1" aria-labelledby="editPoesiaModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editPoesiaModalLabel">Editar Poesia</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editPoesiaForm">
          <input type="hidden" name="id">
          <div class="mb-3">
            <textarea class="form-control" id="edit_texto" name="texto" rows="15" required spellcheck="true" lang="pt-BR"></textarea>
          </div>
           <div class="row">
            <div class="col-md-6 mb-3">
              <label for="edit_anterior" class="form-label">Anterior (ID)</label>
              <input type="number" class="form-control" id="edit_anterior" name="anterior">
            </div>
            <div class="col-md-6 mb-3">
              <label for="edit_proximo" class="form-label">Próximo (ID)</label>
              <input type="number" class="form-control" id="edit_proximo" name="proximo">
            </div>
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
