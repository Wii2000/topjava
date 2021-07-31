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

    $("input:checkbox").click(function () {
        setActivity($(this).closest('tr').attr("id"), $(this).is(':checked'));
    });
});

function setActivity(id, activity) {
    $.ajax({
        type: "PUT",
        url: ctx.ajaxUrl + id + "?activity=" + activity
    }).done(function () {
        updateTable();
        successNoty(activity ? "Record enabled" : "Record disabled");
    });
}