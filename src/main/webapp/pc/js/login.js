let user_name, user_pwd, remember, warn_row, warn_msg;
$(function () {
    $("#login-window").addClass("show");
    let clickable = true;
    $("#login-btn").on("click", function () {
        if (!clickable) {
            return;
        }
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
        $.ajax({
            type: "post",
            url: "/loginUser",
            data: {
                username: username,
                password: userPwd,
            },
            dataType: "json",
            success: function (msg) {
                if (msg.errorCode === 0) {
                    window.location.href = "/home";
                } else {
                    displayWarn(msg.msg || "登录失败");
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
    });
    user_pwd = $("#user-pwd").on("focus", function () {
        user_pwd.removeClass("warn");
        hideWarn();
    });
    remember = $("#remember");
    warn_row = $("#warn-row");
    warn_msg = $("#warn-msg");
});

function displayWarn(msg) {
    warn_row.addClass("show");
    warn_msg.text(msg);
}

function hideWarn() {
    warn_row.removeClass("show");
}