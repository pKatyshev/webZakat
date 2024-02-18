function add_func(task_id) {
    let url = "js_add?count=" + task_id;

    $.ajax({
        url: url,
        type: 'GET'
    })

    setTimeout( () => {document.location.reload();}, 300)
}