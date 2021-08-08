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
            // 头像处理 start
            let userHeadImgModal = $("#user-head-img");
            let uploadProgress = userHeadImgModal.find(".upload-progress");
            let userImgEle = userHeadImgModal.find(".img>img").on("load", function () {
                uploadProgress.removeClass("show");
            });
            let imgFileSelector = userHeadImgModal.find(".selector").on("change", function () {
                let reader = new FileReader();
                reader.readAsDataURL(this.files[0]);
                reader.onload = function () {
                    userImgEle.attr("src", this.result);
                    let files = imgFileSelector[0].files;
                    if (files.length > 0) {
                        let formData = new FormData();
                        formData.set("files", files[0]);
                        $.ajax({
                            type: "post",
                            url: "/upload",
                            data: formData,
                            dataType: "json",
                            processData: false,
                            contentType: false,
                            success: function (msg) {
                                let uris = msg.data.uris;
                                if (uris && uris.length > 0) {
                                    imgUriInput.val(uris[0]);
                                }
                            },
                            error: function () {
                                $.alert("上传失败");
                            }
                        });
                    } else {
                        $.alert("请上传图片");
                    }
                };
            });
            let imgUriInput = userHeadImgModal.find(".img-uri");
            userHeadImgModal.find(".user-head-img-container .btn").on("click", function () {
                imgFileSelector.click();
            });
            let userHeadImgMax = $("#user-head-img-max");
            let userHeadImgMin = $("#user-head-img-min");
            self.jqElements.header.find(".head-img>div>div").on("click", function () {
                userHeadImgModal.modal({
                    width: 220,
                    repeatConfirm: false,
                    onConfirm: function () {
                        let imgUriValue = imgUriInput.val();
                        if (!imgUriValue) {
                            $.alert("请选择上传图片");
                            return false;
                        }
                        $.ajax({
                            type: "post",
                            url: "/home/updateHeadImg",
                            data: {
                                uri: imgUriValue
                            },
                            dataType: "json",
                            success: function (msg) {
                                if (msg.errorCode === 0) {
                                    userHeadImgMax.attr("src", imgUriValue);
                                    userHeadImgMin.attr("src", imgUriValue);
                                    userHeadImgModal.modal("close");
                                } else {
                                    $.alert(msg.msg || "保存失败");
                                }
                                userHeadImgModal.modal("clearConfirmState");
                            },
                            error: function () {
                                $.alert("保存失败");
                                userHeadImgModal.modal("clearConfirmState");
                            }
                        });
                    },
                    onOpen: function () {
                        imgUriInput.val("");
                        imgFileSelector.val("");
                        userImgEle.attr("src", "/global/default-head.png")
                    }
                });
            });
            // 头像处理 end
            // 用户信息处理 start
            let userNameMaxSpan = $("#user-name-max");
            let userNameMinSpan = $("#user-name-min");
            let userInfoModal = $("#user-info-modal");
            userInfoModal.find(".account").text(home.user.account);
            let nameInput = userInfoModal.find(".edit-control[name='name']");
            let phoneInput = userInfoModal.find(".edit-control[name='phone']").on("input", function () {
                let phoneValue = phoneInput.val().trim();
                phoneValue = phoneValue.replace(/\D/g, "");
                phoneInput.val(phoneValue);
                if (phoneValue && !/^1[1-9][1-9]\d{8}$/.test(phoneValue)) {
                    phoneInput.showEditHint("电话号码格式错误", true);
                } else {
                    phoneInput.hideEditHint(true);
                }
            });
            let emailInput = userInfoModal.find(".edit-control[name='email']").on("input", function () {
                let emailValue = emailInput.val().trim();
                if (emailValue && !/^.+@.+\.(com|cn|org)$/.test(emailValue)) {
                    emailInput.showEditHint("邮箱地址格式错误", true);
                } else {
                    emailInput.hideEditHint(true);
                }
            });
            let pwdAreaDiv = userInfoModal.find(".pwd-area");
            let oldPwdInput = userInfoModal.find(".edit-control[name='old-pwd']");
            let newPwdInput = userInfoModal.find(".edit-control[name='new-pwd']");
            let confirmPwdInput = userInfoModal.find(".edit-control[name='confirm-pwd']");
            let changePwdLine = userInfoModal.find(".change-pwd-line").on("click", function () {
                if (changePwdLine.hasClass("down")) {
                    changePwdLine.removeClass("down");
                    pwdAreaDiv.removeClass("show");
                } else {
                    changePwdLine.addClass("down");
                    pwdAreaDiv.addClass("show");
                }
            });
            self.jqElements.header.find(".setting>button").on("click", function () {
                userInfoModal.modal({
                    repeatConfirm: false,
                    onConfirm: function () {
                        let nameValue = nameInput.val().trim();
                        if (!nameValue) {
                            nameInput.showEditHint(undefined, true, true);
                            return false;
                        }
                        let phoneValue = phoneInput.val().trim();
                        if (phoneInput.data("warnState")) {
                            phoneInput.showEditHint(undefined, true, true);
                            return false;
                        }
                        let emailValue = emailInput.val().trim();
                        if (emailInput.data("warnState")) {
                            emailInput.showEditHint(undefined, true, true);
                            return false;
                        }
                        let oldPwd, newPwd;
                        if (pwdAreaDiv.hasClass("show")) {
                            let oldPwdValue = oldPwdInput.val();
                            if (!oldPwdValue) {
                                oldPwdInput.showEditHint(undefined, true, true);
                                return false;
                            }
                            let newPwdValue = newPwdInput.val();
                            if (!newPwdValue) {
                                newPwdInput.showEditHint("密码不能为空", false, true);
                                return false;
                            } else {
                                newPwdInput.hideEditHint();
                            }
                            if (newPwdValue.length < 6) {
                                newPwdInput.showEditHint("密码不能少于6位", false, true);
                                return false;
                            } else {
                                newPwdInput.hideEditHint();
                            }
                            let confirmPwdValue = confirmPwdInput.val();
                            if (newPwdValue !== confirmPwdValue) {
                                confirmPwdInput.showEditHint("再次输入密码不一致", false, true);
                                return false;
                            } else {
                                confirmPwdInput.hideEditHint();
                            }
                            oldPwd = oldPwdValue;
                            newPwd = newPwdValue;
                        }
                        let params = {
                            id: home.user.id,
                            name: nameValue,
                            phone: phoneValue || undefined,
                            email: emailValue,
                            oldPwd: oldPwd,
                            newPwd: newPwd
                        };
                        $.ajax({
                            type: "post",
                            url: "/home/updateUserInfo",
                            data: JSON.stringify(params),
                            dataType: "json",
                            contentType: 'application/json;charset=utf-8',
                            success: function (msg) {
                                if (msg.errorCode === 0) {
                                    $.extend(home.user, {
                                        phone: phoneValue || "",
                                        email: emailValue || ""
                                    }, params);
                                    userNameMaxSpan.text(params.name);
                                    userNameMinSpan.text(params.name);
                                    userInfoModal.modal("close");
                                } else {
                                    $.alert(msg.msg || "保存失败");
                                }
                                userInfoModal.modal("clearConfirmState");
                            },
                            error: function () {
                                $.alert("保存失败");
                                userInfoModal.modal("clearConfirmState");
                            }
                        });
                    },
                    onOpen: function () {
                        nameInput.hideEditHint(true).val(home.user.name || "");
                        phoneInput.hideEditHint(true).val(home.user.phone || "");
                        emailInput.hideEditHint(true).val(home.user.email || "");
                        changePwdLine.removeClass("down");
                        pwdAreaDiv.removeClass("show");
                        oldPwdInput.hideEditHint(true).val("");
                        newPwdInput.hideEditHint(true).val("");
                        confirmPwdInput.hideEditHint(true).val("");
                    }
                });
            });
            // 用户信息处理 end
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

            //菜单处理 start
            self.jqElements.menuToolbar = self.jqElements.menu.find(".toolbar");
            self.jqElements.appList = self.jqElements.menu.find(".application-list");
            let setAppListHeight = function () {
                self.jqElements.appList.css("height", $window.height() - self.jqElements.header.outerHeight() - self.jqElements.menuToolbar.outerHeight() - 4 + "px");
            };
            setAppListHeight();
            self.jqElements.appList.find(".application-item-wrapper").on("click", function (event, enter) {
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
                    item.addClass("active");
                    let routes = [{icon: "", name: item.find(">.name").text()}];
                    let parentItem = item;
                    while ((parentItem = parentItem.parents(".application-item").eq(1)).length > 0) {
                        routes.push({
                            icon: parentItem.find(">.application-item-wrapper>.icon").attr("class"),
                            name: parentItem.find(">.application-item-wrapper>.name").text()
                        });
                        parentItem.addClass("expansion").find(">.application-item-wrapper").addClass("expand").addClass("active");
                    }
                    routes.push({icon: "", name: "首页"});
                    self.jqElements.route.empty();
                    let routeLength = routes.length - 1;
                    for (let i = routeLength; i >= 0; i--) {
                        let item = $('<div class="route-item">' +
                            '<div class="route-item-wrapper">' +
                            '<span class="route-item-name">' + routes[i].name + '</span>' +
                            '</div>' +
                            '</div>');
                        if (i === routeLength) {
                            item.on("click", function () {
                                window.location.href = "/";
                            }).find(">.route-item-wrapper").addClass("home");
                        } else {
                            self.jqElements.route.append('<div class="route-item route-separation"></div>');
                        }
                        if (i === 0) {
                            item.find(".route-item-wrapper").addClass("active");
                        }
                        self.jqElements.route.append(item);
                    }
                    if (enter !== false) {
                        window.location.href = item.data("item-url");
                    }
                }
                appListScroll.resize();
            });
            let appListScroll = self.jqElements.appList.niceScroll({
                cursorborder: "1px solid #e2e2e2",
                cursorcolor: "#2d2e2f",
                cursorwidth: 5
            });
            self.jqElements.activity = self.jqElements.container.find(">.context>.activity");
            self.jqElements.route = self.jqElements.activity.find(">.route");
            self.activeMenu(home.activeModuleId);
            //菜单处理 end

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
                setAppListHeight();
                appListScroll.resize();
            });
            home.on("toggle-menu", function () {
                appListScroll.resize();
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
            setTimeout(function () {
                home.trigger("toggle-menu");
            }, 301);
        },
        activeMenu: function (moduleId, enter) {
            let self = this;
            let menuITem = self.jqElements.appList.find('.application-item-wrapper[data-module-id="' + moduleId + '"]');
            if (menuITem.length > 0) {
                menuITem.triggerHandler("click", [enter === true]);
            } else {
                let item = $('<div class="route-item">' +
                    '<div class="route-item-wrapper">' +
                    '<span class="route-item-name">首页</span>' +
                    '</div>' +
                    '</div>').on("click", function () {
                    window.location.href = "/";
                });
                item.find(">.route-item-wrapper").addClass("home");
                self.jqElements.route.append(item);
            }
        },
        pullMenu: function () {
            let self = this;
            self.jqElements.container.removeClass("pack-menu");
            setTimeout(function () {
                home.trigger("toggle-menu");
            }, 301);
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