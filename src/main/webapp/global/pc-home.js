!function ($) {

    let homeInstance;
    $(function () {
        hide360Div();
        homeInstance = new Home();
    });

    window.home = {
        events: new Map(),
        on: function (eventName, listener) {
            let self = this;
            eventName = eventName.trim();
            if ($.isFunction(eventName)) {
                listener = eventName;
                eventName = "init";
            }
            if (eventName === "init" && homeInstance) {
                listener.apply(homeInstance);
                return;
            }
            self.events.set(eventName, listener);
        }
    };

    let Home = function (option) {
        let self = this;
        self.option = option || {};
        self.activeItem = undefined;
        self.initView();
        let initFun = home.events.get("init");
        if ($.isFunction(initFun)) {
            initFun.apply(self);
        }
    };

    Home.prototype = {
        initView: function () {
            let self = this;
            let container = $("#container");
            self.jqElements = {
                container: container,
                header: container.find(">.header"),
                menu: container.find(">.context>.menu"),
                handrail: container.find(".handrail"),
            };
            self.jqElements.handrail.find(">i").on({
                "click": function () {
                    if (self.jqElements.header.hasClass("show")) {
                        self.jqElements.header.removeClass("show");
                    } else {
                        self.jqElements.header.addClass("show");
                    }
                },
                "mouseover": function () {
                    self.jqElements.header.addClass("hover");
                    self.jqElements.handrail.addClass("hover");
                },
                "mouseout": function () {
                    self.jqElements.header.removeClass("hover");
                    self.jqElements.handrail.removeClass("hover");
                }
            });

            self.jqElements.appList = self.jqElements.menu.find(".application-list");
            createMenu(self.jqElements.appList, apps, true);
            self.jqElements.appList.delegate(".application-item-wrapper", "click", function () {
                let item = $(this);
                if (item.hasClass("branch")) {
                    if (item.hasClass("expand")) {
                        item.removeClass("expand");
                        item.parent().removeClass("expansion");
                    } else {
                        item.addClass("expand");
                        item.parent().addClass("expansion");
                    }
                } else {
                    if (self.activeItem) {
                        self.activeItem.parents(".application-item").find(">.application-item-wrapper").removeClass("active");
                        self.activeItem.removeClass("active");
                    }
                    self.activeItem = item;
                    item.parents(".application-item").find(">.application-item-wrapper").addClass("active");
                    item.addClass("active");
                }
            });
        }
    };

    //隐藏360浏览器底部自带div
    function hide360Div() {
        setTimeout(function () {
            $("#trans-tooltip").parent().hide();
        }, 1000);
    }

    let apps = [
        {
            name: "模块1",
            iconClass: "iconfont icon-shezhi1",
            apps: [
                {
                    name: "模块1.1"
                },
                {
                    name: "模块1.2"
                }
            ]
        },
        {
            name: "模块2",
            iconClass: "iconfont icon-shezhi1"
        }
    ];

    function createMenu(parent, apps, first) {
        if (!apps || apps.length === 0) {
            return;
        }
        let group = $('<ul class="application-group"></ul>').appendTo(parent);
        let app, item;
        for (let i = 0; i < apps.length; i++) {
            app = apps[i];
            item = $('<li class="application-item' + (first ? ' first' : '') + '"/>').appendTo(group);
            first = false
            let children = app.apps;
            item.append('<div class="application-item-wrapper' + (children && children.length > 0 ? ' branch' : '') + '" data-item-id="' + genID() + '">' +
                '<div class="icon' + (' '+app.iconClass || '') + '"></div>' +
                '<div class="name">' + app.name + '</div>' +
                '<div class="btn"></div>' +
                '</div>');
            createMenu(item, children, false);
        }
    }

    function genID() {
        return Number(
            Math.random().toString().substr(
                3,
                36) +
            Date.now()).toString(
            36);
    }
}(jQuery);