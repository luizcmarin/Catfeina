<?php
$page_title = 'Poesias';
require_once 'includes/db.php';
require_once 'includes/header.php';

// Função para extrair o título do Markdown para exibição na tabela
function extrair_titulo_do_markdown($markdown) {
    $linhas = explode("\n", $markdown);
    foreach ($linhas as $linha) {
        if (strpos(trim($linha), '# ') === 0) {
            return trim(substr(trim($linha), 2));
        }
    }
    return 'Poesia sem título';
}

// CORREÇÃO: Query ajustada para a nova estrutura da tabela.
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
                    <!-- CORREÇÃO: Exibindo o título extraído do Markdown. -->
                    <td><?php echo htmlspecialchars(extrair_titulo_do_markdown($poesia['texto'])); ?></td>
                     <!-- CORREÇÃO: Formatando o timestamp diretamente. -->
                    <td><?php echo htmlspecialchars(date('d/m/Y H:i', $poesia['atualizadoem'])); ?></td>
                    <td>
                        <!-- CORREÇÃO: Atributos de modal removidos para permitir que o main.js controle a ação. -->
                        <button type="button" class="btn btn-sm btn-outline-secondary edit-poesia" data-id="<?php echo $poesia['id']; ?>">Editar</button>
                        <button type="button" class="btn btn-sm btn-outline-danger delete-poesia" data-id="<?php echo $poesia['id']; ?>">Excluir</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<!-- Modal Adicionar Poesia (CORREÇÃO: Campos alinhados com a nova estrutura) -->
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
              <label for="add_titulo" class="form-label">Título</label>
              <input type="text" class="form-control" id="add_titulo" name="titulo" required>
            </div>
            <div class="mb-3">
              <label for="add_texto" class="form-label">Texto (Corpo da Poesia)</label>
              <textarea class="form-control" id="add_texto" name="texto" rows="10"></textarea>
            </div>
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="add_autor" class="form-label">Autor</label>
                <input type="text" class="form-control" id="add_autor" name="autor">
              </div>
              <div class="col-md-6 mb-3">
                <label for="add_nota" class="form-label">Nota</label>
                <input type="text" class="form-control" id="add_nota" name="nota">
              </div>
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

<!-- Modal Editar Poesia (CORREÇÃO: Campos alinhados com a nova estrutura) -->
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
            <label for="edit_titulo" class="form-label">Título</label>
            <input type="text" class="form-control" id="edit_titulo" name="titulo" required>
          </div>
          <div class="mb-3">
            <label for="edit_texto" class="form-label">Texto (Corpo da Poesia)</label>
            <textarea class="form-control" id="edit_texto" name="texto" rows="10"></textarea>
          </div>
          <div class="row">
            <div class="col-md-6 mb-3">
              <label for="edit_autor" class="form-label">Autor</label>
              <input type="text" class="form-control" id="edit_autor" name="autor">
            </div>
            <div class="col-md-6 mb-3">
              <label for="edit_nota" class="form-label">Nota</label>
              <input type="text" class="form-control" id="edit_nota" name="nota">
            </div>
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
