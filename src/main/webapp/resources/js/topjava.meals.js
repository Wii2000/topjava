const mealAjaxUrl = "profile/meals/";
let filterForm;

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
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

    filterForm = $('#filterForm');

    $("#startDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $("#endDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $("#startTime").datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
    $("#endTime").datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
    $("#dateTime").datetimepicker({
        format:'Y-m-d H:i'
    });
});

function filter() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: filterForm.serialize()
    }).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
        successNoty("Filtered");
    });
}

function resetFilter() {
    filterForm[0].reset();
    updateTable();
}