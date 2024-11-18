function add_func(task_id) {
    let url = "js_add?count=" + task_id;

    $.ajax({
        url: url,
        type: "GET"
    })

    setTimeout( () => {document.location.reload();}, 300)
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

document.addEventListener("keydown", function (event) {
    if (event.key === "ArrowDown" || event.key === "ArrowUp") {

        let elementId = document.activeElement.id;
        let nextId;

        if (elementId.startsWith("count_field_")) {
            let iterator = elementId.replace("count_field_", "");
            if (event.key === "ArrowDown") {
                nextId = "count_field_" + (++iterator);
            } else {
                nextId = "count_field_" + (--iterator);
            }
            document.getElementById(nextId).focus();
        } else {
            document.getElementById("count_field_0").focus();
        }
    }
})