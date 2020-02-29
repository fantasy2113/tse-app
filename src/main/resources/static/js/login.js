var WRONG_AUTH_DATA = '&#10149; Bitte Passwort UND Benutzername eingeben';
var AUTH_FAIL = '&#10149; Passwort oder Benutzername falsch';

function setToken(username, password, path) {
    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader('username', username);
            request.setRequestHeader('password', password);
        },
        url: getPath() + path,
        method: 'GET',
        dataType: "json",
        success: function (result) {
            document.cookie = 'ctoken=' + result.token + ';';
            $(location).attr('href', getPath());
        },
        error: function () {
            $("#alert").append(AUTH_FAIL);
        }
    });
}

function isLogin(path, username, password) {
    return username === '' || password === ''
        || username === undefined || password === undefined;
}

function login(path, username, password) {
    clear();
    if (isLogin(path, username, password)) {
        $("#alert").append(WRONG_AUTH_DATA);
    } else {
        setToken(username, password, path);
    }
}

function clear() {
    $("#alert").empty();
    $("#alert").append('&nbsp;');
}

function clickLogin(event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode == '13') {
        $('#login_button').click();
    }
}

$(document).ready(function () {
    $('#login_button').bind('click', function () {
        login('auth', $('input[id=username]').val(), $('input[id=password]').val());
    });
    $('#username').bind('change', function () {
        clear();
    }).keypress(function (event) {
        clickLogin(event);
    });
    $('#password').bind('change', function () {
        clear();
    }).keypress(function (event) {
        clickLogin(event);
    });
});
