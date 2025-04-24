const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
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

function updateUserStatus(current, id) {
    debugger;
    $.ajax({
        type: "PUT",
        url: ctx.ajaxUrl + "status/" + id,
        contentType: 'application/x-www-form-urlencoded',
        data: {isEnable: current.checked}
    }).done(function () {
        if (!current.checked) $(current).closest('tr').css("background-color", "lightcoral");
        else $(current).closest('tr').css("background-color", "lightgreen");
        successNoty("Saved");
    });
}