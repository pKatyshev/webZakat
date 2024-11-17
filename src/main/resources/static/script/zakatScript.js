function add_func(task_id) {
    let url = "js_add?count=" + task_id;

    $.ajax({
        url: url,
        type: "GET"
    })

    setTimeout( () => {document.location.reload();}, 300)
}

const preloader = document.getElementById("preloader-block");
if (preloader != null) {
    preloader.hidden = true;
}

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
        return
    }

    event.preventDefault()
    event.stopPropagation();

    document.getElementById("preloader-block").hidden = false;

    $.get("/import/query-ajax", {}, function (data) {
        if (data.status === "good") {
            alert("Заявка загружена!");
            location.reload();
        } else {
            alert(data.status);
            document.getElementById("preloader-block").hidden = true;
        }
    });
});

document.getElementById("import-price-button").addEventListener("click", function (event) {
    if (!confirm("Вы хотите импортировать прайс-листы?")) {
        return
    }

    event.preventDefault()
    event.stopPropagation();

    document.getElementById("preloader-block").hidden = false;

    $.get("/import/prices-ajax", {}, function (data) {
        if (data.status === "good") {
            alert("Прайс-листы загружены!");
            location.reload();
        } else {
            alert(data.status);
            document.getElementById("preloader-block").hidden = true;
        }
    });
});

document.getElementById("delete-query-button").addEventListener("click", function (event) {
    if (!confirm("Вы хотите удалить заякву?")) {
        event.preventDefault()
        event.stopPropagation();
    }
});

document.getElementById("zakazTable").onclick = function (event) {
    let target = event.target.parentNode.children[7].children[0].children[0];
    target.innerHTML = "";
    target.focus();
}

function countFieldFocused() {
    event.target.select();
    let target = event.target.parentNode.parentNode.parentNode;
    target.classList.add("focused-table-row");
}

function countFieldBlur() {
    let target = event.target.parentNode.parentNode.parentNode;
    target.classList.remove("focused-table-row");
}