const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function () {
        $.get(userAjaxUrl, fillTableByData)
    }
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

    $("input:checkbox").click(function () {
        setActivity($(this).closest('tr').attr("id"), $(this));
    });
});

function setActivity(id, checkbox) {
    let enable = checkbox.is(":checked");
    $.ajax({
        type: "PATCH",
        url: ctx.ajaxUrl + id + "?enable=" + enable
    }).done(function () {
        ctx.updateTable();
        successNoty(enable ? "Record enabled" : "Record disabled");
    }).fail(function () {
        checkbox.prop("checked", !enable);
    });
}