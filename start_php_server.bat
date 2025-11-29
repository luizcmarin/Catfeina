@echo off
setlocal EnableDelayedExpansion

REM --- INÍCIO DA MÁGICA DAS CORES ---
REM Esta seção define "variáveis" que podemos usar para imprimir texto colorido.
for /F "tokens=1,2 delims=#" %%a in ('"prompt #$H#$E# & echo on & for %%b in (1) do rem"') do (
  set "ESC=%%b"
)
set "verde=!ESC![92m"
set "amarelo=!ESC![93m"
set "ciano=!ESC![96m"
set "vermelho=!ESC![91m"
set "reset=!ESC![0m"
REM --- FIM DA MÁGICA DAS CORES ---

REM Define o título da janela do prompt de comando.
TITLE Servidor PHP - Catfeina

echo !verde!Iniciando ambiente de desenvolvimento Catfeina...!reset!
echo.

REM Abre o navegador. O comando "start" já executa e continua, sem bloquear.
echo !amarelo!-> Abrindo o projeto no Chrome...!reset!
start chrome http://localhost:8765

echo !ciano!-> Iniciando o servidor PHP em http://localhost:8765!reset!
echo !ciano!   (Pressione Ctrl+C nesta janela para parar o servidor)!reset!
echo.

REM Navega até a pasta do projeto PHP e inicia o servidor.
cd php
php -S localhost:8765
