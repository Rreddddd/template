<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>登录</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global/iconfont/iconfont.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global/pc-base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/pc/css/login.css"/>
    <script src="${pageContext.request.contextPath}/global/jquery-1.11.3.js"></script>
    <script src="${pageContext.request.contextPath}/pc/js/login.js"></script>
</head>
<body>
<div class="login-window" id="login-window">
    <div class="warn-row" id="warn-row">
        <i class="iconfont icon-warning"></i>
        <span class="msg" id="warn-msg"></span>
    </div>
    <div class="form-row">
        <label class="edit-wrapper single">
            <input class="edit-control line-input" type="text" id="user-name" placeholder="输入用户名"/>
        </label>
    </div>
    <div class="form-row">
        <label class="edit-wrapper single">
            <input class="edit-control line-input" type="password" id="user-pwd" placeholder="输入用户密码"/>
        </label>
    </div>
    <div class="form-row captcha-row">
        <label class="edit-wrapper single">
            <input class="edit-control line-input" type="text" id="captcha" placeholder="输入验证码"/>
        </label>
        <img src="${pageContext.request.contextPath}/captcha" alt="暂无图片" id="captcha-img"/>
    </div>
    <div class="login-tool">
        <label class="edit-checkbox-wrapper">
            <input type="checkbox" id="remember"/>
            <span>记住用户</span>
        </label>
    </div>
    <div class="login-btn-row">
        <button id="login-btn">登录</button>
    </div>
</div>
</body>
</html>
