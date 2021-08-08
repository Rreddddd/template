!function ($) {
    let Zoom = new function (option) {
        let self = this;
        self.option = option || {};
        self.jqElements = {
            imgZoomContainer: $('<div class="img-zoom-container"/>')
        };
        self.jqElements.imgZoomWrapper = $('<div class="img-zoom-wrapper"/>').appendTo(self.jqElements.imgZoomContainer);
        self.jqElements.imgZoomWaiting = $('<img class="img-zoom-waiting" src="/pc/image/image-loading.gif" alt=""/>').appendTo(self.jqElements.imgZoomContainer);
        self.jqElements.imgZoomCover = $('<div class="img-zoom-cover"/>').appendTo(self.jqElements.imgZoomContainer);

        let maxHeight = (document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight) - 70;

        self.adjustTimer = 0;
        self.adjustTimerCount = 0;
        self.wheelDelta = 0;
        self.clientX = 0;
        self.clientY = 0;

        self.jqElements.imgZoomWrapper.appendTo('<div style="display:none">' +
            '<p>' +
            '<span class="y">' +
            '<a id="' + menuid + '_imglink" class="imglink" target="_blank" title="在新窗口打开">在新窗口打开</a>' +
            '<a id="' + menuid + '_adjust" href="javascipt:;" class="imgadjust" title="实际大小">实际大小</a>' +
            '<a href="javascript:;" onclick="hideMenu()" class="imgclose" title="关闭">关闭</a>' +
            '</span>鼠标滚轮缩放图片' +
            '</p>' +
            '<div class="zimg_p" id="' + menuid + '_picpage"></div><div class="hm" id="' + menuid + '_img"></div></div>');
        self.jqElements.imgZoomContainer.on("mousewheel", function (event) {
            self.adjust(event);
        });
    };

    Zoom.property = {
        loadCheck: function (imgUrl) {
            let self = this;

        },
        showLoading: function (imgUrl) {
            let self = this;
            self.jqElements.imgZoomWaiting.css({
                left: (document.body.clientWidth - 42) / 2 + 'px',
                top: ((document.documentElement.clientHeight - 42) / 2 + Math.max(document.documentElement.scrollTop, document.body.scrollTop)) + 'px'
            });
            self.loadCheck(imgUrl);
        },
        adjustBtn: function (height) {
            let self = this;
            height = height < 90 ? 90 : height;
            if ($('zimg_prev')) {
                $('zimg_prev').style.height = parseInt(h) + 'px';
            }
            if ($('zimg_next')) {
                $('zimg_next').style.height = parseInt(h) + 'px';
            }
        },
        showImage: function (imgUrl, wrapWidth, wrapHeight, imgWidth, imgHeight) {
            let self = this;
            self.jqElements.imgZoomWaiting.hide();
            self.jqElements.imgZoomLayer.show();
            self.jqElements.imgZoomImg.css({
                width: "auto",
                height: "auto"
            });
            self.jqElements.imgZoomWrapper.css({
                width: (wrapWidth < 300 ? 320 : wrapWidth + 20) + 'px'
            });
            let wrapperHeight = wrapHeight + 63;
            self.jqElements.imgZoomLayer.css({
                height: (wrapperHeight < 120 ? 120 : wrapperHeight) + 'px'
            });
            self.jqElements.img = $('<img data-w="' + imgWidth + '" data-h="' + imgHeight + '" alt=""/>')
                .css({
                    width: wrapWidth + "px",
                    height: wrapHeight + "px"
                })
                .attr("src", imgUrl)
                .appendTo(self.jqElements.imgZoomImg);
            self.adjustBtn(wrapHeight);
        },
        adjust: function (e, a) {
            let self = this;
            if (self.adjustTimerCount) {
                self.adjustTimer = (function () {
                    return setTimeout(function () {
                        adjustTimerCount++;
                        self.adjust(e);
                    }, 20);
                })();
                self.jqElements.img.attr('timer', self.adjustTimer);
                if (self.adjustTimerCount > 17) {
                    clearTimeout(self.adjustTimer);
                    self.adjustTimerCount = 0;
                    done();
                }
            } else if (!a) {
                self.adjustTimerCount = 1;
                if (self.adjustTimer) {
                    clearTimeout(self.adjustTimer);
                    self.adjust(e, a);
                } else {
                    self.adjust(e, a);
                }
                done();
            }
            let ele = self.jqElements.img[0];
            let imgWidth = self.jqElements.img.data('w');
            let imgHeight = self.jqElements.img.data('h');

            if (!a) {
                e = e || window.event;
                try {
                    if (e.altKey || e.shiftKey || e.ctrlKey) return;
                } catch (e) {
                    e = {'wheelDelta': self.wheelDelta, 'clientX': clientX, 'clientY': self.clientY};
                }
                let step = 0;
                if (e.wheelDelta <= 0 || e.detail > 0) {
                    if (ele.width - 1 <= 200 || ele.height - 1 <= 200) {
                        clearTimeout(self.adjustTimer);
                        self.adjustTimerCount = 0;
                        done(e);
                        return;
                    }
                    step = parseInt(imgWidth / ele.width) - 4;
                } else {
                    if (ele.width + 1 >= imgWidth * 40) {
                        clearTimeout(self.adjustTimer);
                        self.adjustTimerCount = 0;
                        done(e);
                        return;
                    }
                    step = 4 - parseInt(imgWidth / ele.width) || 2;
                }
                self.wheelDelta = e.wheelDelta;
                self.clientX = e.clientX;
                self.clientY = e.clientY;
                if (imgHeight > imgWidth) {
                    ele.height += step;
                    ele.width = imgWidth * (ele.height / imgHeight);
                } else if (imgWidth < imgHeight) {
                    ele.width += step;
                    ele.height = imgHeight * (ele.width / imgWidth);
                }
            } else {
                ele.width = imgWidth;
                ele.height = imgHeight;
            }
            self.jqElements.imgZoomLayer.css({
                height: (mheight < 120 ? 120 : mheight) + 'px'
            });
            self.adjustBtn(ele.height);
            done(e);
        }
    };
}(jQuery);

function done(event, preventDefault, stopPropagation) {
    preventDefault = isUndefined(preventDefault) ? 1 : preventDefault;
    stopPropagation = isUndefined(stopPropagation) ? 1 : stopPropagation;
    if (!event) {
        return null;
    }
    if (preventDefault) {
        if (event.preventDefault) {
            event.preventDefault();
        } else {
            event.returnValue = false;
        }
    }
    if (stopPropagation) {
        if (event.stopPropagation) {
            event.stopPropagation();
        } else {
            event.cancelBubble = true;
        }
    }
    return event;
}

function isUndefined(o) {
    return o === undefined && typeof o == "undefined";
}