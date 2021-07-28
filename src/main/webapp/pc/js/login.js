let user_name, user_pwd, captcha, remember, warn_row, warn_msg;
$(function () {
    $("#login-window").addClass("show");
    let clickable = true;
    let login_btn = $("#login-btn").on("click", function () {
        if (!clickable) {
            return;
        }
        localStorage.setItem("remember", remember[0].checked);
        clickable = false;
        let username = user_name.val().trim();
        if (!username) {
            displayWarn("用户名不能为空");
            user_name.addClass("warn");
            clickable = true;
            return;
        }
        let userPwd = user_pwd.val().trim();
        if (!userPwd) {
            displayWarn("密码不能为空");
            user_pwd.addClass("warn");
            clickable = true;
            return;
        }
        let verifyCode = captcha.val().trim();
        if (!verifyCode) {
            displayWarn("请填写验证码");
            captcha.addClass("warn");
            clickable = true;
            return;
        }
        $.ajax({
            type: "post",
            url: "/loginUser",
            data: {
                username: username,
                password: userPwd,
                verifyCode: verifyCode
            },
            dataType: "json",
            success: function (msg) {
                if (msg.errorCode === 0) {
                    localStorage.setItem("username", username);
                    window.location.href = "/home";
                } else if (msg.errorCode === 1) {
                    displayWarn(msg.msg || "登录失败");
                    clickable = true;
                } else {
                    if (msg.errorCode === 3) {
                        captcha.val("");
                        captcha_img.click();
                    }
                    displayWarn(msg.msg || "验证码错误");
                    clickable = true;
                }
            },
            error: function () {
                displayWarn("服务器无响应");
                clickable = true;
            }
        });
    });
    user_name = $("#user-name").on("focus", function () {
        user_name.removeClass("warn");
        hideWarn();
    }).val(localStorage.getItem("username") || "");
    user_pwd = $("#user-pwd").on("focus", function () {
        user_pwd.removeClass("warn");
        hideWarn();
    });
    captcha = $("#captcha").on("focus", function () {
        user_pwd.removeClass("warn");
        hideWarn();
    });
    remember = $("#remember");
    remember[0].checked = localStorage.getItem("remember") === "true";
    warn_row = $("#warn-row");
    warn_msg = $("#warn-msg");
    let captcha_img = $("#captcha-img").on("click", function () {
        captcha_img.attr("src", "/captcha?n=" + new Date().getTime());
    });
    $(window).on("keydown", function (event) {
        if (event.keyCode === 13) {
            login_btn.click();
        }
    });
});

function displayWarn(msg) {
    warn_row.addClass("show");
    warn_msg.text(msg);
}

function hideWarn() {
    warn_row.removeClass("show");
}