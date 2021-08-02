!function ($) {

    let zIndex = 10000;

    let Modal = function (container, option) {
        let self = this;
        self.id = "modal-container-id-" + Math.round(Math.random() * 1000000);
        self.option = $.extend({}, self.defaultOption, option);
        self.jqElements = {
            mask: $('<div class="modal-mask"/>').appendTo('body')
        };
        if (!container) {
            if (self.option.destroy === undefined) {
                self.option.destroy = true;
            }
            self.jqElements.container = $('<div class="modal-container"/>').appendTo('body');
            self.jqElements.header = $('<div class="modal-header"/>')
                .append('<span class="modal-icon ' + (self.option.iconClass || '') + '"></span>' +
                    '<span class="modal-title"></span>' +
                    '<div class="modal-close">x</div>')
                .appendTo(self.jqElements.container);
            self.jqElements.body = $('<div class="modal-body"/>')
                .append(self.option.content || "")
                .appendTo(self.jqElements.container);
            self.jqElements.bottom = $('<div class="modal-bottom"/>')
                .appendTo(self.jqElements.container);
            if ($.isArray(self.option.btn) && self.option.btn.length > 0) {
                for (let i = 0; i < self.option.btn.length; i++) {
                    self.jqElements.bottom.append(self.option.btn[i]);
                }
            } else {
                self.jqElements.bottom.hide();
            }
        } else {
            if (self.option.destroy === undefined) {
                self.option.destroy = false;
            }
            self.jqElements.container = container;
            self.jqElements.header = self.jqElements.container.find(">.modal-header");
            self.jqElements.body = self.jqElements.container.find(">.modal-body");
            self.jqElements.bottom = self.jqElements.container.find(">.modal-bottom");
        }
        self.jqElements.container.data("modal-instance", self);
        let width = self.option.width;
        if (typeof width === "number") {
            width += "px";
        }
        self.jqElements.container.css({
            width: width
        });
        self.setTitle(self.option.title);
        self.jqElements.container.find(".modal-close").on("click", function () {
            self.close();
        });
        self.jqElements.bottom.find(".btn").on("click", function () {
            let $this = $(this);
            if ($this.data("confirm") === undefined) {
                self.option.onClickBtn.apply(self, [$(this).data("type")]);
            } else {
                if (self.option.onConfirm.apply(self) === true) {
                    self.close();
                }
            }
        });
        self.visible = false;
        self.jqElements.$window = $(window).on("resize." + self.id, function () {
            if (self.visible) {
                self.resize();
            }
        });
        self.open();
    };

    Modal.prototype = {
        setTitle: function (text) {
            let self = this;
            self.jqElements.header.find(".modal-title").html(text || "");
        },
        open: function () {
            let self = this;
            self.visible = true;
            self.jqElements.mask.css("z-index", zIndex++).addClass("open");
            self.jqElements.container.css("z-index", zIndex++).addClass("open");
            setTimeout(function () {
                self.resize();
                self.option.onOpened.apply(self);
            }, 100);
        },
        close: function () {
            let self = this;
            if (self.option.onClosed.apply(self) !== false) {
                self.visible = false;
                self.jqElements.mask.removeClass("open");
                self.jqElements.container.removeClass("open");
                if (self.option.destroy) {
                    self.jqElements.mask.remove();
                    self.jqElements.container.remove();
                }
            }
        },
        resize: function () {
            let self = this;
            let containerHeight = self.jqElements.container.height();
            let containerWidth = self.jqElements.container.width();
            let windowHeight = self.jqElements.$window.height();
            let windowWidth = self.jqElements.$window.width();

            let topPercent = Math.round(((windowHeight / 2 - containerHeight / 2) / windowHeight) * 100);
            let leftPercent = Math.round(((windowWidth / 2 - containerWidth / 2) / windowWidth) * 100);
            self.jqElements.container.css({
                left: leftPercent + "%",
                top: topPercent + "%"
            });
        },
        defaultOption: {
            title: "",
            iconClass: "",
            width: 600,
            btn: [],
            onOpened: function () {

            },
            onClosed: function () {

            },
            onConfirm: function () {

            },
            onClickBtn: function (type) {

            }
        }
    };

    $.fn.extend({
        modal: function (option) {
            if (option.btn === undefined) {
                option.btn = [$.modal.btn.confirm];
            }
            this.installPlug(Modal, "modal-instance", arguments);
        }
    });

    $.extend({
        modal: function (option) {
            if (option.btn === undefined) {
                option.btn = [$.modal.btn.confirm];
            }
            return new Modal(undefined, option);
        },
        alert: function (option) {
            if (option) {
                if (typeof option === "string") {
                    option = {
                        content: option
                    };
                }
            } else {
                option = {
                    content: "",
                    autoClose: 0
                };
            }
            if (!option.title) {
                if (option.icon === undefined || option.icon === 1) {
                    option.title = "消息";
                } else if (option.icon === 2) {
                    option.title = "警告";
                }
            }
            if (option.btn === $.alert.btn.confirm) {
                option.btn = [$.modal.btn.confirm];
            } else if (option.btn === $.alert.btn.yesAndNo) {
                option.btn = [$.modal.btn.yes, $.modal.btn.no];
            }
            if (option.icon === undefined || option.icon === 1) {
                option.iconClass = "iconfont icon-fl-xinxi\" style=\"color: #769aff";
            } else if (option.icon === 2) {
                option.iconClass = "iconfont icon-jinggao\" style=\"color: #facf87";
            }
            option.width = 251;
            option.content = '<span style="color: #a2a2a2;">' + (option.content || "") + '</span>';
            let modal = new Modal(undefined, option);
            if (option.autoClose) {
                setTimeout(function () {
                    modal.close();
                }, option.autoClose);
            }
            return modal;
        }
    });

    $.modal.btn = {
        confirm: '<button data-type="1" data-confirm class="form-editor add btn">确定</button>',
        yes: '<button data-type="2" data-confirm class="form-editor info btn">是</button>',
        no: '<button data-type="3" data-confirm class="form-editor default btn">否</button>'
    };

    $.alert.btn = {
        confirm: 1,
        yes: 2,
        no: 3,
        yesAndNo: 4
    };

    $.alert.icon = {
        info: 1,
        warn: 2
    };
}(jQuery);