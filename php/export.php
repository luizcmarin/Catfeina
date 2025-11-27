<?php
$page_title = 'Exportar';
require_once 'includes/header.php';
require_once 'includes/db.php';

// --- CONFIGURAÇÕES ---
$source_images_dir = 'imagens';
$sync_export_dir = 'export';
$final_zip_name = 'CatfeinaSync.zip';
$sync_manifest_name = 'manifest.json';
$sync_data_folder = 'data/';
$sync_images_folder = 'images/';

$app_update_info = [
    "versionCode" => 210,
    "versionName" => "2.1.0-catverso",
    "changelog" => "• Correção de bugs na tela de leitura.\n• Novas poesias de Outono!",
    "url" => "https://seuservidor.com/atualizacoes/catfeina-v2.1.0.apk"
];

$modules = [
    'poesias' => ['table' => 'tbl_poesia', 'file' => 'poesias.json'],
    'personagens' => ['table' => 'tbl_personagem', 'file' => 'personagens.json'],
    'meows' => ['table' => 'tbl_meow', 'file' => 'meows.json'],
    'informativos' => ['table' => 'tbl_informativo', 'file' => 'informativos.json'],
    'atelier' => ['table' => 'tbl_atelier', 'file' => 'atelier.json'],
];

$log_messages = [];
$zip_extension_enabled = class_exists('ZipArchive');

// Lógica de gerenciamento de versão
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    try {
        if (isset($_POST['increment_module'])) {
            $module = $_POST['increment_module'];
            $stmt = $pdo->prepare('UPDATE tbl_versoes SET versao = versao + 1 WHERE modulo = :modulo');
            $stmt->execute([':modulo' => $module]);
            header('Location: export.php?status=version_incremented');
            exit;
        } elseif (isset($_POST['reset_module'])) {
            $module = $_POST['reset_module'];
            $stmt = $pdo->prepare('UPDATE tbl_versoes SET versao = MAX(100, versao - 1) WHERE modulo = :modulo');
            $stmt->execute([':modulo' => $module]);
            header('Location: export.php?status=version_reset');
            exit;
        }
    } catch (PDOException $e) {
        $log_messages[] = ['type' => 'danger', 'text' => 'Erro ao gerenciar versão: ' . $e->getMessage()];
    }
}

// Lógica de exportação
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['export'])) {
    if (!$zip_extension_enabled) {
        $log_messages[] = ['type' => 'danger', 'text' => 'Erro Crítico: A extensão ZipArchive do PHP não está habilitada.'];
    } else {
        try {
            if (!file_exists($sync_export_dir)) mkdir($sync_export_dir, 0777, true);

            // 1. Gerar e salvar o manifest.json independente
            $stmt = $pdo->query("SELECT modulo, versao FROM tbl_versoes");
            $versions_from_db = $stmt->fetchAll(PDO::FETCH_KEY_PAIR);
            $manifest_modulos = [];
            foreach ($modules as $name => $info) {
                $manifest_modulos[] = ['nome' => $name, 'versao' => (int)($versions_from_db[$name] ?? 100), 'arquivo' => $sync_data_folder . $info['file']];
            }
            $manifest_imagens = ['versao' => (int)($versions_from_db['imagens'] ?? 100), 'arquivo' => $sync_images_folder];
            $manifest_content_array = ['modulos' => $manifest_modulos, 'imagens' => $manifest_imagens, 'app_update' => $app_update_info];
            $manifest_json_content = json_encode($manifest_content_array, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
            file_put_contents($sync_export_dir . '/' . $sync_manifest_name, $manifest_json_content);
            $log_messages[] = ['type' => 'success', 'text' => "Arquivo '{$sync_manifest_name}' gerado com sucesso."];

            // 2. Gerar e salvar o CatfeinaSync.zip com os dados e imagens
            $final_zip_path = $sync_export_dir . '/' . $final_zip_name;
            $zip = new ZipArchive();
            
            // Nota de compactação: ZipArchive usa o método DEFLATE por padrão, que já é um excelente
            // algoritmo de compressão (semelhante ao de um ZIP padrão). Não é possível setar um "nível" (1-9)
            // de compressão, mas o padrão já oferece o melhor balanço entre tamanho e velocidade.
            if ($zip->open($final_zip_path, ZipArchive::CREATE | ZipArchive::OVERWRITE) === TRUE) {
                // Adicionar arquivos JSON na pasta 'data/'
                $zip->addEmptyDir($sync_data_folder);
                foreach ($modules as $name => $info) {
                    $stmt = $pdo->query("SELECT * FROM {$info['table']}");
                    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
                    $processed_data = array_map(function($row) {
                        if (isset($row['atualizadoem'])) { $row['atualizadoem'] = strtotime($row['atualizadoem']) * 1000; }
                        if (isset($row['fixada'])) { $row['fixada'] = (bool)$row['fixada']; }
                        return $row;
                    }, $data);
                    $zip->addFromString($sync_data_folder . $info['file'], json_encode($processed_data, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));
                }
                $log_messages[] = ['type' => 'info', 'text' => count($modules) . " arquivos JSON adicionados à pasta '{$sync_data_folder}' no ZIP."];

                // Adicionar imagens na pasta 'images/'
                if (is_dir($source_images_dir)) {
                    $zip->addEmptyDir($sync_images_folder);
                    $image_files = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($source_images_dir), RecursiveIteratorIterator::LEAVES_ONLY);
                    $image_count = 0;
                    foreach ($image_files as $name => $file) {
                        if (!$file->isDir()) {
                            $filePath = $file->getRealPath();
                            $relativePath = substr($filePath, strlen(realpath($source_images_dir)) + 1);
                            $zip->addFile($filePath, $sync_images_folder . $relativePath);
                            $image_count++;
                        }
                    }
                    $log_messages[] = ['type' => 'info', 'text' => "{$image_count} imagens adicionadas à pasta '{$sync_images_folder}' no ZIP."];
                } else {
                    $log_messages[] = ['type' => 'warning', 'text' => "Pasta de imagens de origem '{$source_images_dir}' não encontrada."];
                }

                $zip->close();
                $log_messages[] = ['type' => 'success', 'text' => "Arquivo '{$final_zip_name}' gerado com sucesso."];
            } else {
                $log_messages[] = ['type' => 'danger', 'text' => "Falha ao criar o arquivo ZIP final."];
            }
        } catch (Exception $e) {
            $log_messages[] = ['type' => 'danger', 'text' => 'Erro durante a exportação: ' . $e->getMessage()];
        }
    }
}

$versions_for_display = $pdo->query('SELECT modulo, versao, descricao FROM tbl_versoes ORDER BY modulo')->fetchAll(PDO::FETCH_ASSOC);
?>

<!-- O HTML do Dashboard permanece o mesmo -->
<div class="container">
    <div class="row">
        <div class="col-lg-5">
            <div class="card mb-3">
                <div class="card-header fw-bold">Painel de Exportação</div>
                <div class="card-body">
                    <p>Clique para gerar os arquivos <code><?= $sync_manifest_name ?></code> e <code><?= $final_zip_name ?></code>.</p>
                    <form method="POST">
                        <button type="submit" name="export" class="btn btn-primary w-100" <?= !$zip_extension_enabled ? 'disabled' : '' ?>>Gerar Arquivos de Sincronização</button>
                    </form>
                </div>
            </div>
            
            <div class="card">
                <div class="card-header fw-bold">Gerenciador de Versões</div>
                <ul class="list-group list-group-flush">
                    <?php foreach ($versions_for_display as $version): ?>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <strong class="d-block"><?= htmlspecialchars(ucfirst($version['modulo'])) ?></strong>
                                <small class="text-muted"><?= htmlspecialchars($version['descricao']) ?></small>
                            </div>
                            <div class="d-flex align-items-center">
                                <span class="badge bg-primary rounded-pill me-3 fs-6"><?= htmlspecialchars($version['versao']) ?></span>
                                <div class="btn-group">
                                    <form method="POST" class="m-0">
                                        <input type="hidden" name="reset_module" value="<?= $version['modulo'] ?>">
                                        <button type="submit" class="btn btn-sm btn-outline-warning" title="Resetar Versão (-1)">-1</button>
                                    </form>
                                    <form method="POST" class="m-0">
                                        <input type="hidden" name="increment_module" value="<?= $version['modulo'] ?>">
                                        <button type="submit" class="btn btn-sm btn-outline-success" title="Incrementar Versão (+1)">+1</button>
                                    </form>
                                </div>
                            </div>
                        </li>
                    <?php endforeach; ?>
                </ul>
            </div>

        </div>
        <div class="col-lg-7">
            <div class="card">
                 <div class="card-header fw-bold">Log de Execução</div>
                 <div class="card-body" style="min-height: 400px; max-height: 500px; overflow-y: auto;">
                    <?php if (empty($log_messages) && !isset($_GET['status'])): ?>
                        <div class="text-center text-muted mt-4">Aguardando execução...</div>
                    <?php else: ?>
                        <ul class="list-group">
                             <?php 
                                if(isset($_GET['status']) && $_GET['status'] == 'version_incremented') {
                                    echo '<li class="list-group-item list-group-item-success">Versão incrementada com sucesso!</li>';
                                } else if (isset($_GET['status']) && $_GET['status'] == 'version_reset') {
                                    echo '<li class="list-group-item list-group-item-warning">Versão resetada com sucesso!</li>';
                                }
                            ?>
                            <?php foreach ($log_messages as $msg): ?>
                                <li class="list-group-item list-group-item-<?= $msg['type'] ?>"><?= htmlspecialchars($msg['text']) ?></li>
                            <?php endforeach; ?>
                        </ul>
                    <?php endif; ?>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Ativa os tooltips do Bootstrap
document.addEventListener('DOMContentLoaded', function () {
  var tooltipTriggerList = [].slice.call(document.querySelectorAll('[title]'))
  var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl)
  })
});
</script>

<?php require_once 'includes/footer.php'; ?>
