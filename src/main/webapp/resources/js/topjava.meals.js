const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$('#startDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});
$('#endDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});
$('#startTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
});
$('#endTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
});
$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i',
});

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.replace('T', ' ').substring(0, 16);
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ], "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
});

function checkerChangerDate(input) {
    const startDate = $('#startDate').val();
    const endDate = $('#endDate').val();
    if (new Date(startDate).getTime() > new Date(endDate).getTime()) {
        const jInput = $(input);
        const id = jInput.attr('id');
        if (id === 'startDate')
            failNoty(i18n["endDate.isEarlier"]);
        else failNoty(i18n["startDate.isLater"]);
        jInput.val('');
    }
}

function checkerChangerTime(input) {
    const startTime = $('#startTime').val();
    const endTime = $('#endTime').val();
    if (startTime && endTime && startTime > endTime) {
        const jInput = $(input);
        const id = jInput.attr('id');
        if (id === 'startTime')
            failNoty(i18n["endTime.isEarlier"]);
        else failNoty(i18n["startTime.isLater"]);
        jInput.val('');
    }
}

function failNoty(message) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + message,
        type: "error",
        layout: "bottomRight"
    });
    failedNote.show()
}