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

    $.fn.extend({
        popWindow: function () {
            let params = Arrays.clone(arguments);
            let firstParam = params.shift();
            let instance = this.data("pop-window-instance");
            if (typeof firstParam === "string") {
                if (instance) {
                    let method = instance[firstParam];
                    if ($.isFunction(method)) {
                        return method.apply(instance, params);
                    }
                }
            } else if (!instance) {
                return new PopWindow(this, firstParam);
            }
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