<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" uri="http://www.0512.red/tags" %>
<html>
<head>
    <title>首页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global/iconfont/iconfont.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global/pc-base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global/pc-home.css"/>
    <script src="${pageContext.request.contextPath}/global/jquery-1.11.3.js"></script>
    <script src="${pageContext.request.contextPath}/global/pc-base.js"></script>
    <script src="${pageContext.request.contextPath}/global/pc-home.js"></script>
</head>
<body>
<div class="container" id="container">
    <div class="header">
        <div class="information">
            <div class="head-img"></div>
            <div class="feature">
                <div class="text">
                    <div class="name">布尔</div>
                    <div class="dept">系统管理员</div>
                </div>
                <div class="setting">
                    <button class="form-editor">编辑资料</button>
                </div>
            </div>
        </div>
        <div class="notification">
            <div class="title">通知</div>
            <ul>
                <li>
                    <label>
                        <input type="checkbox"/>
                        <span>消息</span>
                    </label>
                </li>
                <li>
                    <label>
                        <input type="checkbox"/>
                        <span>事件</span>
                    </label>
                </li>
            </ul>
        </div>
    </div>
    <div class="context">
        <div class="menu">
            <div class="toolbar">
                <span class="company-icon">Rred</span>
                <div class="handrail">
                    <i class="iconfont icon-shezhi1"></i>
                </div>
            </div>
            <div class="application-list expansion">

            </div>
        </div>
        <div class="activity">
            <div class="toolbar">
                <div class="toolbar-item menu-btn left">
                    <i class="menu-icon"></i>
                </div>
                <div class="toolbar-item self-info right">
                    <div class="head-img"></div>
                    <span class="name">而对</span>
                </div>
            </div>
            <div class="route">
                <div class="route-item">
                    <div class="route-item-wrapper">
                        <i class="iconfont icon-shezhi1"></i>
                        <span class="route-item-name">首页</span>
                    </div>
                </div>
                <div class="route-item route-separation"></div>
                <div class="route-item">
                    <div class="route-item-wrapper">
                        <span class="route-item-name">模块1</span>
                    </div>
                </div>
                <div class="route-item route-separation"></div>
                <div class="route-item">
                    <div class="route-item-wrapper active">
                        <span class="route-item-name">模块1.1</span>
                    </div>
                </div>
            </div>
            <div class="panel-container">
                <div class="panel-wrapper">
                    ${html}
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>