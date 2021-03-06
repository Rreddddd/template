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
            self.setTitle(self.option.title);
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
        if (self.option.width) {
            let width = self.option.width;
            if (typeof width === "number") {
                width += "px";
            }
            self.jqElements.container.css({
                width: width
            });
        }
        self.jqElements.container.find(".modal-close").on("click", function () {
            self.close();
        });
        self.jqElements.bottom.find(".btn").on("click", function () {
            let $this = $(this);
            if ($this.data("confirm") === undefined) {
                self.option.onClickBtn.apply(self, [$(this).data("type")]);
            } else {
                self.clickConfirm();
            }
        });
        self.visible = false;
        self.jqElements.$window = $(window).on("resize." + self.id, function () {
            if (self.visible) {
                self.resize();
            }
        }).on("keydown." + self.id, function (event) {
            if (self.visible && event.keyCode === 13) {
                self.clickConfirm();
            }
        });
        self.jqElements.container.on("keydown." + self.id, function (event) {
            if (self.visible && event.keyCode === 13) {
                self.clickConfirm();
            }
        });
        self.confirmState = true;
        if (self.option.initOpen) {
            self.open();
        }
    };

    Modal.prototype = {
        setTitle: function (text) {
            let self = this;
            self.jqElements.header.find(".modal-title").html(text || "");
        },
        open: function () {
            let self = this;
            if (self.option.onOpen.apply(self) === false) {
                return;
            }
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
                    self.jqElements.$window.off("resize." + self.id).on("keydown." + self.id);
                }
            }
        },
        resize: function () {
            let self = this;
            let containerHeight = self.jqElements.container.height();
            let containerWidth = self.jqElements.container.width();
            let windowHeight = self.jqElements.$window.height();
            let windowWidth = self.jqElements.$window.width();

            let top = windowHeight / 2 - containerHeight / 2;
            let left = windowWidth / 2 - containerWidth / 2;
            self.jqElements.container.css({
                left: left + "px",
                top: top + "px"
            });
        },
        clickConfirm: function () {
            let self = this;
            if (!self.confirmState) {
                return;
            }
            let result = self.option.onConfirm.apply(self);
            if (self.option.repeatConfirm) {
                if (result === true) {
                    self.close();
                }
            } else {
                if (result !== undefined && result !== null) {
                    self.confirmState = true;
                }
            }
        },
        clearConfirmState: function () {
            this.confirmState = true;
        },
        defaultOption: {
            initOpen: true,
            title: "",
            iconClass: "",
            width: 0,
            btn: [],
            repeatConfirm: true,
            onOpen: function () {

            },
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
            return this.installPlug(Modal, "modal-instance", arguments, "open");
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
                    option.title = "??????";
                } else if (option.icon === 2) {
                    option.title = "??????";
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
        confirm: '<button data-type="1" data-confirm class="form-editor add btn">??????</button>',
        yes: '<button data-type="2" class="form-editor info btn">???</button>',
        no: '<button data-type="3" class="form-editor default btn">???</button>'
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