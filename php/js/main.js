document.addEventListener('DOMContentLoaded', function () {

    /**
     * Função genérica para submeter formulários de adição e edição via AJAX.
     */
    function submitForm(form, actionFile, action) {
        const formData = new FormData(form);
        formData.append('action', action);
        fetch(actionFile, { method: 'POST', body: formData })
            .then(res => res.json())
            .then(handleServerResponse);
    }

    /**
     * Manipula a resposta do servidor após submissão de formulário (Adicionar/Editar).
     * Resolve o problema de acessibilidade esperando o modal fechar antes de mostrar o alerta.
     */
    function handleServerResponse(data) {
        const activeModalEl = document.querySelector('.modal.show');
        
        if (data.status === 'success') {
            if (activeModalEl) {
                const modalInstance = bootstrap.Modal.getInstance(activeModalEl);
                activeModalEl.addEventListener('hidden.bs.modal', () => {
                    Swal.fire({ icon: 'success', title: 'Sucesso!', text: data.message })
                        .then(() => location.reload());
                }, { once: true });
                
                modalInstance.hide();
            } else {
                Swal.fire({ icon: 'success', title: 'Sucesso!', text: data.message })
                    .then(() => location.reload());
            }
        } else {
            Swal.fire({ icon: 'error', title: 'Erro!', text: data.message || 'Ocorreu um erro.' });
        }
    }

    // Anexa os listeners de submissão para todos os formulários da aplicação.
    document.getElementById('addPoesiaForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/poesias_actions.php', 'add'); });
    document.getElementById('editPoesiaForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/poesias_actions.php', 'update'); });
    document.getElementById('addPersonagemForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/personagens_actions.php', 'add'); });
    document.getElementById('editPersonagemForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/personagens_actions.php', 'update'); });
    document.getElementById('addAtelierForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/atelier_actions.php', 'add'); });
    document.getElementById('editAtelierForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/atelier_actions.php', 'update'); });
    document.getElementById('addInformativoForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/informativos_actions.php', 'add'); });
    document.getElementById('editInformativoForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/informativos_actions.php', 'update'); });
    document.getElementById('addMeowForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/meow_actions.php', 'add'); });
    document.getElementById('editMeowForm')?.addEventListener('submit', (e) => { e.preventDefault(); submitForm(e.target, 'ajax/meow_actions.php', 'update'); });

    /**
     * Delegação de eventos para todos os cliques em botões de ação (Editar/Excluir).
     */
    document.body.addEventListener('click', function(e) {
        const button = e.target.closest('button[data-id]');
        if (!button) return;

        const id = button.dataset.id;
        let type, actionFile;

        // Lógica para botões de Edição
        if (button.matches('.edit-poesia, .edit-personagem, .edit-atelier, .edit-informativo, .edit-meow')) {
            if (button.matches('.edit-poesia')) { type = 'poesia'; actionFile = 'poesias_actions.php'; }
            else if (button.matches('.edit-personagem')) { type = 'personagem'; actionFile = 'personagens_actions.php'; }
            else if (button.matches('.edit-atelier')) { type = 'atelier'; actionFile = 'atelier_actions.php'; }
            else if (button.matches('.edit-informativo')) { type = 'informativo'; actionFile = 'informativos_actions.php'; }
            else if (button.matches('.edit-meow')) { type = 'meow'; actionFile = 'meow_actions.php'; }
            
            fetch(`ajax/${actionFile}?action=get&id=${id}`)
                .then(res => res.json())
                .then(res => {
                    if (res.status === 'success') {
                        const modalId = `edit${type.charAt(0).toUpperCase() + type.slice(1)}Modal`;
                        const modalEl = document.getElementById(modalId);
                        if (!modalEl) return;

                        for (const key in res.data) {
                            const input = modalEl.querySelector(`[name="${key}"]`);
                            if (input) {
                                if (input.type === 'checkbox') input.checked = res.data[key] == 1;
                                else input.value = res.data[key];
                            }
                        }
                        bootstrap.Modal.getOrCreateInstance(modalEl).show();
                    } else {
                        Swal.fire('Erro!', res.message, 'error');
                    }
                });
        }

        // Lógica para botões de Exclusão
        if (button.matches('.delete-poesia, .delete-personagem, .delete-atelier, .delete-historico, .delete-informativo, .delete-meow')) {
            if (button.matches('.delete-poesia')) { type = 'poesia'; actionFile = 'poesias_actions.php'; }
            else if (button.matches('.delete-personagem')) { type = 'personagem'; actionFile = 'personagens_actions.php'; }
            else if (button.matches('.delete-atelier')) { type = 'atelier'; actionFile = 'atelier_actions.php'; }
            else if (button.matches('.delete-historico')) { type = 'historico'; actionFile = 'historico_actions.php'; }
            else if (button.matches('.delete-informativo')) { type = 'informativo'; actionFile = 'informativos_actions.php'; }
            else if (button.matches('.delete-meow')) { type = 'meow'; actionFile = 'meow_actions.php'; }

            Swal.fire({
                title: 'Você tem certeza?',
                text: "Esta ação não pode ser desfeita!",
                icon: 'warning', showCancelButton: true, confirmButtonColor: '#d33', confirmButtonText: 'Sim, excluir!', cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    const formData = new FormData();
                    formData.append('action', 'delete');
                    formData.append('id', id);
                    fetch(`ajax/${actionFile}`, { method: 'POST', body: formData })
                    .then(res => res.json())
                    .then(res => {
                        if (res.status === 'success') {
                            document.getElementById(`${type}-${id}`)?.remove();
                            Swal.fire('Excluído!', res.message, 'success');
                        } else { 
                            Swal.fire('Erro!', res.message, 'error'); 
                        }
                    });
                }
            });
        }
    });
});
