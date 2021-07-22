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
                handrail: container.find(".handrail")
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
        }
    };

    //隐藏360浏览器底部自带div
    function hide360Div() {
        setTimeout(function () {
            $("#trans-tooltip").parent().hide();
        }, 1000);
    }
}(jQuery);