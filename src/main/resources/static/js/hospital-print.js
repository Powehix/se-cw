jQuery(document).ready(function($) {
    mainEntityClass = $("body").attr('data-main-class');

    loadAllObjectsToPage(); // loading all objects from database to page
});

function loadAllObjectsToPage() {
    $.ajax({
        url: '/' + mainEntityClass + '/all/print',
        type: 'post',
        success: function (data) {
            $("#body-content").html(data);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function getSqlResult() {
    var query = $("input:checked").eq(0).attr('value');

    $.ajax({
        url: '/' + mainEntityClass + '/sql',
        type: 'post',
        data: "query=" + escape(query),
        success: function (data) {
            $("#body-content").html(data);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function modifyQuery(queryNum) {
    var span = $("#string" + queryNum);
    var input = $("#query" + queryNum);
    var param = $("#param" + queryNum);
    var paramValue = param.eq(0).val();

    var sampleQuery = input.eq(0).attr('data-sample');
    var newQuery = sampleQuery.replace('?', paramValue)
    span.text(newQuery);
    input.val(newQuery);
}