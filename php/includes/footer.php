</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="https://unpkg.com/easymde/dist/easymde.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="js/main.js"></script>

<script>
document.addEventListener('DOMContentLoaded', function () {
    let easyMDE_add, easyMDE_edit;

    const addModal = document.getElementById('addPoesiaModal');
    addModal.addEventListener('shown.bs.modal', function () {
        if (!easyMDE_add) {
            easyMDE_add = new EasyMDE({element: document.getElementById('add_texto')});
        }
    });

    const editModal = document.getElementById('editPoesiaModal');
    editModal.addEventListener('shown.bs.modal', function () {
        const editTextArea = document.getElementById('edit_texto');
        if (!easyMDE_edit) {
            easyMDE_edit = new EasyMDE({element: editTextArea});
        }
        // Garante que o valor mais recente seja exibido ao reabrir
        easyMDE_edit.value(editTextArea.value);
    });

    // Atualiza o textarea antes da submissão do formulário
    document.getElementById('editPoesiaForm').addEventListener('submit', function(){
        if(easyMDE_edit) {
            const textarea = document.getElementById('edit_texto');
            textarea.value = easyMDE_edit.value();
        }
    });
    document.getElementById('addPoesiaForm').addEventListener('submit', function(){
        if(easyMDE_add) {
            const textarea = document.getElementById('add_texto');
            textarea.value = easyMDE_add.value();
        }
    });
});
</script>

</body>
</html>
