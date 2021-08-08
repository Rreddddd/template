!function ($) {

    let PopWindow = function (ele, option) {
        let self = this;
        self.ele = ele.data("pop-window-instance", self);
        self.option = $.extend({}, self.defaultOption, option);
        self.jqElements = {
            container: $('<div class="pop-window-container"/>').appendTo('body')
        };
        self.init();
        setTimeout(function () {
            self.option.onInit.apply(self, [self.jqElements.container]);
        }, 1);
    };

    PopWindow.prototype = {
        init: function () {
            let self = this;
            if (self.option.containerClass) {
                self.jqElements.container.addClass(self.option.containerClass);
            }
        },
        push: function () {
            let self = this;
            self.jqElements.container.removeClass("pop");
        },
        pop: function () {
            let self = this;
            let offset = self.ele.offset();
            let left = offset.left;
            let top = offset.top;
            let $window = $(window);
            let windowWidth = $window.width();
            let windowHeight = $window.height();
            let containerWidth = self.jqElements.container.width();
            let containerHeight = self.jqElements.container.height();
            let eleWidth = self.ele.width();
            let eleHeight = self.ele.height();
            if (containerWidth + left > windowWidth) {
                left = left - (containerWidth - eleWidth) + self.option.right;
            } else {
                left += self.option.left;
            }
            if (containerHeight + top > windowHeight) {
                top = top - (containerHeight - eleHeight) + self.bottom;
            } else {
                top += eleHeight + self.option.top;
            }
            self.jqElements.container.addClass("pop");
            setTimeout(function () {
                self.jqElements.container.css({
                    "left": left + "px",
                    "top": top + "px",
                });
            }, 100);
        },
        defaultOption: {
            containerClass: undefined,
            top: 0,
            left: 0,
            bottom: 0,
            right: 0,
            width: -1,
            height: -1,
            horizontalDirection: "auto",
            verticalDirection: "auto",
            onInit: function (container) {

            }
        }
    };

    let Table = function (ele, option) {
        let self = this;
        self.ele = ele.data("table-instance", self);
        self.option = $.extend({}, self.defaultOption, option);
        self.jqElements = {
            container: ele.addClass("custom-table").empty()
        };
        self.jqElements.thead = $('<table class="table-header"><thead></thead></table>').appendTo(self.jqElements.container);
        self.jqElements.tbody = $('<table class="table-body"><tbody></tbody></table>').appendTo(self.jqElements.container);
        self.jqElements.footer = $('<table class="table-footer"><tbody></tbody></table>').appendTo(self.jqElements.container);
        self.jqElements.bottom = $('<div class="table-bottom">' +
            '<label class="page-rows">' +
            '<span>每页条数</span>' +
            '<select>' +
            '<option>10</option>' +
            '<option>20</option>' +
            '</select>' +
            '</label>' +
            '<span>' +
            '第<span>1</span>页（共<span>2</span>页 10条数据）' +
            '</span>' +
            '<ul class="pager">' +
            '<li class="first">上一页</li>' +
            '<li class="active">1</li>' +
            '<li>1</li>' +
            '<li>...</li>' +
            '<li>1</li>' +
            '<li>1</li>' +
            '<li class="last">下一页</li>' +
            '</ul>' +
            '</div>').appendTo(self.jqElements.container);
        self.setColumns(self.option.columns);
        self.loadData(self.option.data);
    };

    Table.prototype = {
        setColumns: function (columns) {
            if (columns && columns.length > 0) {
                let self = this;
                let trs = '', column, col;
                for (let i = 0; i < columns.length; i++) {
                    column = columns[i];
                    trs += '<tr>';
                    for (let k = 0; k < column.length; k++) {
                        col = column[k];
                        trs += '<th style="width: ' + col.width + '"' + (col.colspan ? ' colspan="' + col.colspan + '"' : '') + (col.rowspan ? ' rowspan="' + col.rowspan + '"' : '') + '>' + col.title + '</th>';
                    }
                    trs += '</tr>';
                }
                self.jqElements.thead.find("thead").html(trs);
                self.option.columns = columns;
            }
        },
        loadData: function (data) {
            if (data) {
                let self = this;
                let rows = $.isArray(data) ? data : (data.rows || []);
                let fields = self.option.columns[0] || [];
                let tbody = self.jqElements.tbody.find("tbody");
                for (let i = 0; i < rows.length; i++) {
                    let rowData = rows[i];
                    let tr = $('<tr/>').data("rowData",rowData).appendTo(tbody);
                    for (let k = 0; k < fields.length; k++) {
                        let field = fields[k];
                        let td = $('<td/>').appendTo(tr);
                        td.css("width", field.width);
                        td.css("text-align", field.align || "left");
                        if ($.isFunction(field.formatter)) {
                            td.append(field.formatter.apply(self, [i, field.field, rowData]) || "");
                        } else {
                            td.html(rowData[field.field] || "");
                        }
                    }
                }
            }
        },
        defaultOption: {
            showFooter: false,
            showPage: false,
            columns: []
        }
    };

    $.fn.extend({
        installPlug: function (plug, dataName, _arguments, laterInitMethodName) {
            let params = Arrays.clone(_arguments);
            let firstParam = params.shift();
            let instance = this.data(dataName);
            if (typeof firstParam === "string") {
                if (instance) {
                    let method = instance[firstParam];
                    if ($.isFunction(method)) {
                        let result = method.apply(instance, params);
                        if (result !== undefined) {
                            return result;
                        }
                    }
                }
            } else if (!instance) {
                new plug(this, firstParam);
            } else if (laterInitMethodName) {
                let method = instance[laterInitMethodName];
                if ($.isFunction(method)) {
                    let result = method.apply(instance, params);
                    if (result !== undefined) {
                        return result;
                    }
                }
            }
            return this;
        },
        popWindow: function () {
            return this.installPlug(PopWindow, "pop-window-instance", arguments) || this;
        },
        table: function () {
            return this.installPlug(Table, "table-instance", arguments) || this;
        },
        showEditHint: function (text, redBorder, focus) {
            let hint = this.next();
            if (hint.length > 0 && hint.hasClass("edit-hint") && text) {
                hint.text(text).addClass("show");
            }
            if (redBorder) {
                let $this = this;
                $this.off("focus.hint").on("focus.hint", function () {
                    $this.hideEditHint(true);
                }).data("warnState", true).addClass("warn");
            }
            if (focus) {
                this[0].focus();
            }
            return this;
        },
        hideEditHint: function (redBorder) {
            let hint = this.next();
            if (hint.length > 0 && hint.hasClass("edit-hint")) {
                hint.removeClass("show");
            }
            if (redBorder) {
                this.data("warnState", false).removeClass("warn");
            }
            return this;
        }
    });

    window.Arrays = {
        clone: function (arr) {
            let newArr = [];
            for (let i = 0; i < arr.length; i++) {
                newArr.push(arr[i]);
            }
            return newArr;
        }
    };
}(jQuery);