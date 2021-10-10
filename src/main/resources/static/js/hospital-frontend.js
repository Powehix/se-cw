var mainEntityClass = '';

jQuery(document).ready(function($) {
    mainEntityClass = $("body").attr('data-main-class');

    loadAllObjectsToPage(); // loading all objects from database to page
});

function enableInputChecking() {
    $("input").each(
        function () {
            this.onchange = checkInputOnEntering;
        }
    )
}

function checkInputOnEntering() {
    this.classList.remove('red-border');
}

function loadAllObjectsToPage() {
    if (mainEntityClass === 'task')
        loadDepartmentBoard($("body").attr('data-department-id'));
    else {
        $.ajax({
            url: '/' + mainEntityClass +'/all',
            type: 'post',
            success: function (data) {
                $("#body-content").html(data);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }
}

function downloadReadingList() {
    window.open( '/' + mainEntityClass + '/print');
}

function entityRemoving(objectId, specificClass = false) {
    var deletingClass = '';
    specificClass ? deletingClass = specificClass : deletingClass = mainEntityClass;
    $.ajax({
        url: '/' + deletingClass +'/remove',
        type: "post",
        data: "id=" + objectId,
        success: function (data) {
            hideForm('edit-entity-form');
            if (data === 'Not Found!') {
                alert('This data does not exist!');
            }
            if (deletingClass === 'department') {
                window.open('/department/');
            } else if (deletingClass === 'visit-time') {
                $('#' + deletingClass + '-' + objectId).remove();
                hideForm('timelog-form');
            } else
                loadAllObjectsToPage();
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function openDataForm(objectId, isNewObjectRequired = false, className = undefined) {
    var userClass = '';
    className ? userClass = className : userClass = mainEntityClass;

    if (isNewObjectRequired && mainEntityClass === 'task' && className !== 'visit-time') {
        objectId = $("body").attr('data-department-id');
    }

    $.ajax({
        url: '/' + userClass +'/form',
        type: "post",
        data: "id=" + objectId + "&adding=" + isNewObjectRequired,
        success: function (data) {
            if (data === 'Not Found!') {
                alert('This data does not exist!');
                loadAllObjectsToPage();
            } else {
                var formPlaceHtml = document.getElementById('data-form-place').innerHTML;
                formPlaceHtml += data;
                $("#data-form-place").html(formPlaceHtml);
                enableInputChecking();
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function submitFilter(formId) {
    var form = $('#' + formId);

    $.ajax({
        url: form.attr("action"),
        type: form.attr("method"),
        data: form.serialize(),
        success: function (data) {
            $("#body-content").html(data);
            hideForm(formId);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function submitDataForm(formId) {
    var isDataSet = true;
    var form = $('#' + formId);
    var inputs = $('#' + formId + ' .data');

    for (var i = 0; i < inputs.length; i++) {
        if(inputs[i].value === '' && inputs[i].hasAttribute('required')) {
            inputs[i].classList.add('red-border');
            isDataSet = false;
        }
    }

    if(!isDataSet) {
        return;
    }

    var post_url = form.attr("action");
    var request_method = form.attr("method");
    var form_data = form.serialize();

    $.ajax({
        url: post_url,
        type: request_method,
        data: form_data,
        success: function (data) {
            if (data === 'Saved') {
                if (post_url === '/department/save' && formId === 'edit-entity-form')
                    document.getElementById('page-title').textContent = inputs[0].value;

                hideForm(formId);
            } else {
                alert('Not saved! Message: ' . data);
            }
            loadAllObjectsToPage();
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function resetForm(formId) {
    var inputs = document.getElementById(formId)
        .getElementsByClassName('data');
    for (var i = 0; i < inputs.length; i++) {
        inputs[i].classList.remove('red-border');
        inputs[i].value = '';
    }
}

function hideForm(containerId) {
    if (containerId === 'add-entity-form') {
        $("#form-add-container").remove();
    } else if (containerId === 'edit-entity-form') {
        $("#form-edit-container").remove();
    } else if (containerId === 'timelog-form') {
        $("#timelog-container").remove();
    }
}

function openTimeFilter() {
    $.ajax({
        url: '/visit-time/filter',
        type: 'post',
        success: function (data) {
            $("#body-content").html(data);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function openDepartmentBoard(departmentId, departmentName) {
    var pageTitle = document.getElementById('page-title');
    pageTitle.textContent = departmentName;
    var bodyContent = document.getElementById('body-content');
    bodyContent.classList.add('department-board');
    bodyContent.innerHTML = "";
    mainEntityClass = 'task';
    $("body").attr('data-department-id', departmentId);

    loadDepartmentBoard(departmentId);

    first = $('#action-buttons span').get(0);
    second = $('#action-buttons span').get(1);
    first.innerText  = 'ADD NEW TASK';
    second.innerText = 'MAKE LIST OF ALL TASKS';
    var actionsHtml = document.getElementById('action-buttons').innerHTML;
    actionsHtml += "<a onclick=\"openDataForm('" + departmentId + "', false, 'department')\">" +
        "<img src=\"/images/edit.png\" class=\"left-menu-image\"><span>EDIT</span></a>";

    $('#action-buttons').html(actionsHtml);
}

function loadDepartmentBoard(id) {
    $.ajax({
        url: '/department-board',
        type: "post",
        data: "id=" + id,
        success: function (data) {
            if (data === 'Not Found!') {
                alert('This data does not exist!');
            } else {
                $("#body-content").html(data);
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

//=====================================
//=====================================

$("#Position_name").change(function () {
    $( "#Position_name option:selected" ).each(function() {
        console.log("Handler for POSITION .change() called.");
        // str += $( this ).text() + " ";
    });
});

$("#department_name").change(function() {
    console.log("Handler for DEPARTMENT .change() called.");
});

$( "#doctor_name").change(function() {
    console.log("Handler for DOCTOR .change() called.");
});


$("#date_input").change(function() {
    console.log("Handler for DATE .change() called.");
});
