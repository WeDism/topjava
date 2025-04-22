const userAjaxUrl = "user/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#meals-datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Update",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function saveFilter() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: formMeals.serialize()
    }).done(function (data) {
        $("#editRow").modal("hide");
        plainUpdateTable(data);
        successNoty("Saved");
    });
}