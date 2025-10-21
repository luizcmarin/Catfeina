<?php

// =============================================================================
// Arquivo: converter.php
// Descrição: Versão final e corrigida, com lógica de extração robusta para
//            separar corretamente textoBase do texto principal.
// =============================================================================

error_reporting(E_ALL);
ini_set('display_errors', 1);

// --- CONFIGURAÇÃO ---
const INPUT_MARKDOWN_FILE = 'gatofeina_original.md';
const SOURCE_PICTURES_DIR = 'Pictures';
const OUTPUT_DIR = 'output';
const OUTPUT_DATA_DIR = OUTPUT_DIR . '/dados';
const OUTPUT_IMAGES_DIR = OUTPUT_DIR . '/imagens';

// =============================================================================
// FUNÇÕES AUXILIARES
// =============================================================================

function setupOutputDirectory()
{
    if (is_dir(OUTPUT_DIR)) {
        $it = new RecursiveDirectoryIterator(OUTPUT_DIR, RecursiveDirectoryIterator::SKIP_DOTS);
        $files = new RecursiveIteratorIterator($it, RecursiveIteratorIterator::CHILD_FIRST);
        foreach ($files as $file) {
            $file->isDir() ? rmdir($file->getRealPath()) : unlink($file->getRealPath());
        }
        rmdir(OUTPUT_DIR);
    }
    mkdir(OUTPUT_DIR, 0777, true);
    mkdir(OUTPUT_DATA_DIR, 0777, true);
    mkdir(OUTPUT_IMAGES_DIR, 0777, true);
}

/**
 * FASE 1: Limpa o conteúdo bruto do Pandoc antes do processamento.
 */
function preProcessContent(string $rawContent): array
{
    $renamedImageMap = [];
    $imageCounter = 1;

    // Remove todos os atributos entre chaves, que são o principal lixo do Pandoc.
    $content = preg_replace('/\{.*?\}/s', '', $rawContent);
    // Remove barras invertidas que o Pandoc usa para escapes.
    $content = str_replace('\', '', $content);

    // Renomeia imagens com HASH para nomes limpos e atualiza o conteúdo para um formato simplificado.
    $content = preg_replace_callback(
        '/(!\[.*?\]\(Pictures\/([a-zA-Z0-9]{32})\.(jpg|png|jpeg|gif)\))/',
        function ($matches) use (&$imageCounter, &$renamedImageMap) {
            $originalFilename = $matches[2] . '.' . $matches[3];
            $newFilename = sprintf("catfeina_%04d.%s", $imageCounter, $matches[3]);
            $sourcePath = SOURCE_PICTURES_DIR . '/' . $originalFilename;

            if (file_exists($sourcePath)) {
                $renamedImageMap[$newFilename] = $sourcePath;
            } else {
                echo "AVISO [Fase 1]: Imagem de origem '$originalFilename' não encontrada!\n";
            }

            $imageCounter++;
            // Simplifica a tag da imagem para um formato consistente: !(nome_arquivo.ext)
            return '!(' . $newFilename . ')';
        },
        $content
    );
    
    // Remove colchetes vazios restantes, como os de âncoras.
    $content = str_replace('[]', '', $content);

    return ['content' => $content, 'renamedImages' => $renamedImageMap];
}

/**
 * FASE 3: Converte a sintaxe Markdown final para as tags customizadas.
 */
function convertMarkdownToCustomTags(string $text): string
{
    $text = trim($text);
    if (empty($text)) return '';

    $text = preg_replace('/\*\*\*(.*?)\*\*\*/s', '{ni|$1}', $text);
    $text = preg_replace('/\*\*(.*?)\*\*/s', '{n|$1}', $text);
    $text = preg_replace('/\*(.*?)\*/s', '{i|$1}', $text);
    $text = preg_replace('/^>\s*(.*)/m', '{cit|$1}', $text);
    return trim($text);
}

// =============================================================================
// LÓGICA PRINCIPAL
// =============================================================================

function main()
{
    echo "Iniciando processo de conversão...\n";
    setupOutputDirectory();
    echo "[PASSO 1/4] Diretório de saída preparado.\n";

    $rawContent = file_get_contents(INPUT_MARKDOWN_FILE);
    if ($rawContent === false) {
        die("ERRO: Não foi possível ler o arquivo de entrada '" . INPUT_MARKDOWN_FILE . "'\n");
    }

    // --- FASE 1: PRÉ-PROCESSAMENTO ---
    $preProcessedResult = preProcessContent($rawContent);
    $cleanContent = $preProcessedResult['content'];
    $renamedImageMap = $preProcessedResult['renamedImages'];
    echo "[PASSO 2/4] Pré-processamento concluído. " . count($renamedImageMap) . " imagens mapeadas.\n";

    // --- FASE 2: EXTRAÇÃO DOS DADOS (MÉTODO ROBUSTO) ---
    preg_match_all(
        '/(^\d+\s*\.\s*#.*?)(?=(^\d+\s*\.\s*#)|\Z)/ms',
        $cleanContent,
        $matches
    );
    $poesiaBlocks = $matches[0];

    $poesiasData = [];
    foreach ($poesiaBlocks as $block) {
        $lines = explode("\n", trim($block));
        $titleLine = array_shift($lines);
        $body = trim(implode("\n", $lines));

        if (!preg_match('/#\s*(.*)/', $titleLine, $titleMatches)) continue;
        $title = trim($titleMatches[1]);
        if (empty($title)) continue;

        $imageName = null;
        $textoBase_raw = '';
        $texto_raw = $body;

        // Lógica de extração ajustada para o novo formato de tag de imagem: !(catfeina_XXXX.ext)
        if (preg_match('/(?<full_tag>!\((?<filename>catfeina_\d{4}\.(?:jpg|png|jpeg|gif))\))/', $body, $imageMatches)) {
            $imageName = $imageMatches['filename'];
            $fullImageTag = $imageMatches['full_tag'];
            
            $parts = explode($fullImageTag, $body, 2);
            $contentAfterImage = trim($parts[1] ?? '');
            
            // Usa preg_split para lidar com \r\n e \n de forma consistente
            $paragraphs = preg_split('/\r?\n\r?\n/', $contentAfterImage, 2);
            $textoBase_raw = trim($paragraphs[0] ?? '');
            
            // O texto principal é o que sobra APÓS o texto base
            $texto_raw = trim($paragraphs[1] ?? '');
        } else {
            // Se não houver imagem, o corpo inteiro é o texto principal
            $texto_raw = $body;
        }

        $poesiasData[] = [
            'titulo' => $title,
            'textoBase_raw' => $textoBase_raw,
            'texto_raw' => $texto_raw,
            'imagem' => $imageName
        ];
    }
    echo "[PASSO 3/4] Extração concluída. " . count($poesiasData) . " poesias encontradas.\n";

    // --- FASE 3: CONVERSÃO FINAL E GERAÇÃO ---
    $finalJsonData = [];
    foreach ($poesiasData as $index => $poesia) {
        $finalJsonData[] = [
            'id' => $index + 1,
            'categoria' => 'POESIA',
            'titulo' => $poesia['titulo'],
            'textoBase' => convertMarkdownToCustomTags($poesia['textoBase_raw']),
            'texto' => convertMarkdownToCustomTags($poesia['texto_raw']),
            'textoFinal' => null,
            'imagem' => $poesia['imagem'],
            'autor' => null,
            'nota' => null,
            'campoUrl' => null,
            'dataCriacao' => time()
        ];
    }

    file_put_contents(OUTPUT_DATA_DIR . '/poesias.json', json_encode($finalJsonData, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));

    $manifestData = [
        'versao' => time(),
        'nota' => count($finalJsonData) . ' poesias processadas em ' . date('Y-m-d H:i:s'),
        'arquivos_dados' => ['poesias.json'],
        'versao_app' => null
    ];
    file_put_contents(OUTPUT_DIR . '/manifest.json', json_encode($manifestData, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));

    $copiedImages = 0;
    foreach ($renamedImageMap as $newName => $originalSource) {
        copy($originalSource, OUTPUT_IMAGES_DIR . '/' . $newName);
        $copiedImages++;
    }
    echo "[PASSO 4/4] Arquivos JSON gerados e $copiedImages imagens copiadas.\n";

    echo "\nPROCESSO CONCLUÍDO!\n";
}

main();

?>