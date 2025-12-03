<?php
$page_title = isset($page_title) ? $page_title : 'Poesias';
?>
<!doctype html>
<html lang="pt-br">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Catfeina - Catverso | <?php echo htmlspecialchars($page_title); ?></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://unpkg.com/easymde/dist/easymde.min.css">
    <link href="css/style.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="index.php">Catfeina - Catverso</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link <?php echo ($page_title == 'Poesias') ? 'active' : ''; ?>" href="index.php">Poesias</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <?php echo ($page_title == 'Personagens') ? 'active' : ''; ?>" href="personagens.php">Personagens</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <?php echo ($page_title == 'Atelier') ? 'active' : ''; ?>" href="atelier.php">Atelier</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <?php echo ($page_title == 'Histórico') ? 'active' : ''; ?>" href="historico.php">Histórico</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <?php echo ($page_title == 'Informativos') ? 'active' : ''; ?>" href="informativos.php">Informativos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <?php echo ($page_title == 'Meow') ? 'active' : ''; ?>" href="meow.php">Meow</a>
                </li>
            </ul>
            <ul class="navbar-nav">
                 <li class="nav-item">
                    <a class="nav-link <?php echo ($page_title == 'Importador de Poesias') ? 'active' : ''; ?>" href="importador.php">Importador</a>
                </li>
                 <li class="nav-item">
                    <a class="nav-link <?php echo ($page_title == 'Exportar') ? 'active' : ''; ?>" href="export.php">Exportar</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="container">
