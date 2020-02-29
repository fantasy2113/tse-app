function initJsGrid() {
    $('#jsGrid').empty();
    var MyDateField = function (config) {
        jsGrid.Field.call(this, config);
    };
    MyDateField.prototype = new jsGrid.Field({
        sorter: function (date1, date2) {
            return new Date(date1) - new Date(date2);
        },
        itemTemplate: function (value) {
            return new Date(value).toLocaleString();
        },
        insertTemplate: function (value) {
            return this._insertPicker = $("<input>").datepicker({defaultDate: new Date()});
        },
        editTemplate: function (value) {
            return this._editPicker = $("<input>").datepicker().datepicker("setDate", new Date(value));
        },
        insertValue: function () {
            return this._insertPicker.datepicker("getDate").toISOString();
        },
        editValue: function () {
            return this._editPicker.datepicker("getDate").toISOString();
        }
    });
    jsGrid.fields.myDateField = MyDateField;
    $("#jsGrid").jsGrid({
        width: "1000",
        height: "600",
        inserting: true,
        editing: true,
        sorting: true,
        filtering: true,
        autoload: true,
        controller: {
            loadData: function (filter) {
                var d = $.Deferred();
                $.ajax({
                    type: "PUT",
                    url: getPath() + 'licence/filter',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(filter),
                    dataType: "json",
                    statusCode: {
                        401: function (result) {
                            resetCookieAndReloadPage();
                        }
                    }
                }).done(function (result) {
                    d.resolve(result);
                });
                return d.promise();
            },
            insertItem: function (item) {
                return $.ajax({
                    type: "POST",
                    url: getPath() + 'licence/save',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(item),
                    dataType: "json",
                    statusCode: {
                        401: function () {
                            resetCookieAndReloadPage();
                        }
                    },
                    error: function () {
                        alert("err");
                    }
                });
            },
            updateItem: function (item) {
                return $.ajax({
                    type: "PUT",
                    url: getPath() + 'licence/update',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(item),
                    dataType: "json",
                    statusCode: {
                        401: function () {
                            resetCookieAndReloadPage();
                        }
                    },
                    error: function () {
                        alert("err");
                    }
                });
            },
            deleteItem: function (item) {
                $.ajax({
                    type: "DELETE",
                    url: getPath() + 'licence/delete',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(item),
                    dataType: "json",
                    statusCode: {
                        401: function (result) {
                            resetCookieAndReloadPage();
                        }
                    },
                    error: function () {
                        alert("err");
                    }
                });
            }
        },
        fields: [
            {name: "id", title: "ID", type: "number", width: "auto", visible: false},
            {name: "licenceNumber", title: "Lizenz", type: "text", width: "auto"},
            {name: "tseType", title: "TSE", type: "text", width: "auto"},
            {name: "numberOfTse", title: "Anzahl", type: "number", width: "auto"},
            {name: "branchNumber", title: "Filiale", type: "number", width: "auto"},
            {name: "tillExternalId", title: "Kasse", type: "number", width: "auto"},
            {name: "dateRegistered", title: "Registriert", type: "myDateField", width: "auto"},
            {name: "active", title: "Aktiv", type: "checkbox", width: "auto"},
            {type: "control"}
        ],
        rowClick: function (args) {
            var item = args.item;
            $.ajax({
                type: "GET",
                url: getPath() + '/licence/requests/' + item.id,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                statusCode: {
                    401: function () {
                        resetCookieAndReloadPage();
                    }
                },
                success: function (result) {
                    $("#dialog").dialog({
                        bgiframe: true,
                        modal: true,
                        width: 350,
                        open: function (event, ui) {
                            $("#dialogJsGrid").jsGrid({
                                height: "100%",
                                width: "100%",
                                sorting: true,
                                data: result,
                                fields: [
                                    {name: "requestType", type: "text", title: "Typ", width: "auto"},
                                    {name: "requestDate", title: "Datum", type: "myDateField", width: "auto"}
                                ]
                            });
                        }
                    });
                }
            });
        }
    });
}

$(document).ready(function () {
    $('#refresh_button').bind('click', function () {
        initJsGrid();
    });
    initJsGrid();
});