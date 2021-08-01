!function ($) {

    let $window;
    let homeInstance;
    $(function () {
        $window = $(window).on("resize", function () {
            home.trigger("window-resize");
            home.trigger("container-resize");
        });
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
        },
        trigger: function (eventName, params) {
            let self = this;
            let event = self.events.get(eventName);
            if ($.isFunction(event)) {
                event.apply(window, params);
            }
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
                handrail: container.find(".handrail")
            };
            let handrailTimeout;
            self.jqElements.handrail.find(">i").on({
                "click": function () {
                    if (handrailTimeout !== undefined) {
                        clearTimeout(handrailTimeout);
                    }
                    if (self.jqElements.header.hasClass("show")) {
                        self.jqElements.header.removeClass("show");
                    } else {
                        self.jqElements.header.addClass("show");
                    }
                    handrailTimeout = setTimeout(function () {
                        home.trigger("container-resize");
                    }, 101);
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
                    window.location.href = item.data("item-url");
                }
            });

            self.jqElements.activity = self.jqElements.container.find(">.context>.activity");
            self.jqElements.activity.find(".toolbar .menu-btn").on("click", function () {
                self.toggleMenu();
            });

            let self_name = self.jqElements.activity.find(".self-info .name").on("click", function () {
                if (self_name.hasClass("active")) {
                    self_name.removeClass("active");
                    self_name.popWindow("push");
                } else {
                    self_name.addClass("active");
                    self_name.popWindow("pop");
                }
            });
            self_name.popWindow({
                containerClass: "logout-pop-window",
                right: 15,
                onInit: function (container) {
                    $('<div class="logout-btn"/>').on("click", function () {
                        window.location.href = "/logout";
                    }).text("退出").appendTo(container);
                }
            });

            let setPanelContainerHeight = function () {
                panelContainer.css("height", $window.height() - (self.jqElements.header.outerHeight() + activityToolbar.outerHeight() + activityRoute.outerHeight() + 3) + "px");
            };
            let activityToolbar = self.jqElements.activity.find(">.toolbar");
            let activityRoute = self.jqElements.activity.find(">.route");
            let panelContainer = self.jqElements.activity.find(">.panel-container");
            home.on("container-resize", function () {
                setPanelContainerHeight();
                panelWrapper.resize();
            });
            setPanelContainerHeight();
            let panelWrapper = panelContainer.find(">.panel-wrapper").niceScroll({
                cursorcolor: "#efefef",
                cursorwidth: 10
            });
        },
        toggleMenu: function () {
            let self = this;
            let state = self.jqElements.container.hasClass("pack-menu");
            if (state) {
                self.pullMenu();
            } else {
                self.packMenu();
            }
        },
        packMenu: function () {
            let self = this;
            self.jqElements.container.addClass("pack-menu");
        },
        pullMenu: function () {
            let self = this;
            self.jqElements.container.removeClass("pack-menu");
        }
    };

    //隐藏360浏览器底部自带div
    function hide360Div() {
        setTimeout(function () {
            $("#trans-tooltip").parent().hide();
            home.trigger("container-resize");
        }, 1000);
    }
}(jQuery);