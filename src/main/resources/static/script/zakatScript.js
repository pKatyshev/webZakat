function add_func(task_id) {
    let url = "js_add?count=" + task_id;

    $.ajax({
        url: url,
        type: "GET"
    })

    setTimeout( () => {document.location.reload();}, 300)
};

document.addEventListener("keydown", function (event) {
    if (event.ctrlKey && event.key === "ArrowRight") {
        document.getElementById("down-button").click();
    }

    if (event.ctrlKey && event.key === "ArrowLeft") {
        document.getElementById("up-button").click();
    }

    if (event.key === "Escape") {
        document.getElementById("search-input-field").focus();
    }
});

document.getElementById("import-query-button").addEventListener("click", function (event) {
    if (!confirm("Вы хотите импортировать заякву?")) {
        event.preventDefault()
        event.stopPropagation();
    }
});

document.getElementById("import-price-button").addEventListener("click", function (event) {
    if (!confirm("Вы хотите импортировать прайс-листы?")) {
        event.preventDefault()
        event.stopPropagation();
    }
});

document.getElementById("delete-query-button").addEventListener("click", function (event) {
    if (!confirm("Вы хотите удалить заякву?")) {
        event.preventDefault()
        event.stopPropagation();
    }
});