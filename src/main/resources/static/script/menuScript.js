const preloader = document.getElementById("preloader-block");
if (preloader != null) {
    preloader.hidden = true;
}

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