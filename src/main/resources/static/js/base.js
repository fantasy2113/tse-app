var DEV_ADDRESS = 'http://localhost:8080/';
var ADDRESS = 'https://tse-lizenzpruefung.herokuapp.com/';
var CONTEXT_PATH = '3h8ZKS3wn1Xu0dDSzeOGweVe_N4n7PbuOJ0GKzrK3hEj1IS2HkcBC5AKSBLq_uK3tFQaRR8bT5rMfmQAvY192A/';
var REST_APP = 'app';

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
    var url = window.location.href;
    if (url.includes("localhost")) {
        return DEV_ADDRESS + CONTEXT_PATH;
    }
    return ADDRESS + CONTEXT_PATH;
}
