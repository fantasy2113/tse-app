$(document).ready(function () {
    $('#logout_button').bind('click', function () {
        resetCookieAndReloadPage();
    });
});

function resetCookieAndReloadPage() {
    document.cookie = 'ctoken=noctoken;';
    $(location).attr('href', getPath());
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

String.prototype.includes = function (str) {
    var returnValue = false;
    if (this.indexOf(str) !== -1) {
        returnValue = true;
    }
    return returnValue;
};

function getPath() {
    return window.location.href;
}
