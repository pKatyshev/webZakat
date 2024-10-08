function add_func(task_id) {
    let url = "js_add?count=" + task_id;

    $.ajax({
        url: url,
        type: 'GET'
    })

    setTimeout( () => {document.location.reload();}, 300)
};

document.addEventListener('keydown', function (e) {
    if (e.code === 'ArrowRight') {
        document.getElementById('down-button').click();
    }

    if (e.code === 'ArrowLeft') {
        document.getElementById('up-button').click();
    }
});