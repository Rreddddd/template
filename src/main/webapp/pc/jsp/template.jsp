<%@ page import="util.Context" %>
<%@ page import="entity.User" %>
<%@ page import="util.Menus" %>
<%@ page import="util.WebPathUtil" %>
<%@ page import="util.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://www.0512.red/tags" %>
<%
    User user = Context.getUser();
    if (user == null) {
        user = new User();
    }
    String headImg = user.getHeadImg();
    if (StringUtils.isBlank(headImg)) {
        headImg = "/global/default-head.png";
    } else {
        headImg = WebPathUtil.convertToUri(user.getHeadImg());
    }
    user.setHeadImg(null);
    user.setPassword(null);
%>
<t:template id="pc-home">
    <html>
    <head>
        <title><t:TemplatePlaceholder id="title"/></title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/global/iconfont/iconfont.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/pc/css/modal.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/global/pc-base.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/global/pc-home.css"/>
        <script src="${pageContext.request.contextPath}/global/jquery-1.11.3.js"></script>
        <script src="${pageContext.request.contextPath}/global/jquery.nicescroll.min.js"></script>
        <script src="${pageContext.request.contextPath}/global/pc-base.js"></script>
        <script src="${pageContext.request.contextPath}/pc/js/modal.js"></script>
        <script src="${pageContext.request.contextPath}/global/pc-home.js"></script>
        <script>
            !function () {
                let userJson = '<%=user.toJson()%>';
                window.home.user = userJson ? JSON.parse(userJson) : undefined;
            }();
        </script>
        <t:TemplatePlaceholder id="head"/>
    </head>
    <body>
    <div class="container" id="container">
        <div class="header">
            <div class="information">
                <div class="head-img">
                    <img src="<%=headImg%>" alt="暂无图片" id="user-head-img-max"/>
                    <div>
                        <div>更改头像</div>
                    </div>
                </div>
                <div class="feature">
                    <div class="text">
                        <div class="name" id="user-name-max"><%=user.getName()%>
                        </div>
                        <div class="dept"><%=(user.getPosition() == null ? "- -" : user.getPosition().getName())%>
                        </div>
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
                    <c:set var="menus" value="<%=Menus.getCurrentMenus()%>" scope="request"/>
                    <c:import url="menu.jsp"/>
                </div>
            </div>
            <div class="activity">
                <div class="toolbar">
                    <div class="toolbar-item menu-btn left">
                        <i class="menu-icon"></i>
                    </div>
                    <div class="toolbar-item self-info right">
                        <div class="head-img">
                            <img src="<%=headImg%>" alt="暂无图片" id="user-head-img-min"/>
                        </div>
                        <span class="name" id="user-name-min"><%=user.getName()%></span>
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
                        <div class="panel-content">
                            <t:TemplatePlaceholder id="body"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-container" id="user-head-img">
        <div class="modal-header">
            <span class="modal-icon "></span>
            <span class="modal-title">上传头像</span>
            <div class="modal-close">x</div>
        </div>
        <div class="modal-body">
            <div class="user-head-img-container">
                <div class="img">
                    <img src="${pageContext.request.contextPath}/global/default-head.png" alt="暂无">
                    <div class="upload-progress">
                        <div class="upload-progress-wrapper">
                            <div class="upload-progress-bar"></div>
                            <div class="upload-progress-bg">10%</div>
                        </div>
                    </div>
                </div>
                <div class="line"></div>
                <a href="javascript:void(0)" class="btn">点击选择图片</a>
                <input class="img-uri" type="hidden"/>
                <input class="selector" style="display: none;" type="file" accept="image/jpeg,image/jpg,image/png"/>
            </div>
        </div>
        <div class="modal-bottom">
            <button data-type="1" data-confirm class="form-editor add btn">确定</button>
        </div>
    </div>
    <div class="modal-container" id="user-info-modal">
        <div class="modal-header">
            <span class="modal-icon "></span>
            <span class="modal-title">个人信息更改</span>
            <div class="modal-close">x</div>
        </div>
        <div class="modal-body">
            <div class="user-info-container">
                <div class="form-row">
                    <div class="edit-wrapper">
                        <span class="edit-title">账<span class="word-hold"></span><span
                                class="word-hold"></span>户 :</span>
                        <span class="edit-value account">admin</span>
                    </div>
                </div>
                <div class="form-row">
                    <label class="edit-wrapper">
                        <span class="edit-title">名<span class="word-hold"></span><span
                                class="word-hold"></span>称 :</span>
                        <div class="edit-value">
                            <input class="edit-control" maxlength="20" type="text" name="name" placeholder="输入用户名称(必填)">
                        </div>
                    </label>
                </div>
                <div class="form-row">
                    <label class="edit-wrapper">
                        <span class="edit-title">电<span class="word-hold"></span><span
                                class="word-hold"></span>话 :</span>
                        <div class="edit-value">
                            <input class="edit-control" type="text" name="phone" placeholder="输入移动电话">
                            <div class="edit-hint"></div>
                        </div>
                    </label>
                </div>
                <div class="form-row">
                    <label class="edit-wrapper">
                        <span class="edit-title">邮<span class="word-hold"></span><span
                                class="word-hold"></span>箱 :</span>
                        <div class="edit-value">
                            <input class="edit-control" maxlength="100" type="text" name="email" placeholder="输入电子邮箱">
                            <div class="edit-hint"></div>
                        </div>
                    </label>
                </div>
                <div class="change-pwd-line">修改密码</div>
                <div class="pwd-area">
                    <div class="form-row">
                        <label class="edit-wrapper">
                            <span class="edit-title">旧<span class="word-hold half"></span>密<span
                                    class="word-hold half"></span>码 :</span>
                            <div class="edit-value">
                                <input class="edit-control" type="password" name="old-pwd" placeholder="输入旧密码">
                                <div class="edit-hint"></div>
                            </div>
                        </label>
                    </div>
                    <div class="form-row">
                        <label class="edit-wrapper">
                            <span class="edit-title">新<span class="word-hold half"></span>密<span
                                    class="word-hold half"></span>码 :</span>
                            <div class="edit-value">
                                <input class="edit-control" type="password" maxlength="16" name="new-pwd" placeholder="输入新密码(不少于6位)">
                                <div class="edit-hint"></div>
                            </div>
                        </label>
                    </div>
                    <div class="form-row">
                        <label class="edit-wrapper">
                            <span class="edit-title">确认密码 :</span>
                            <div class="edit-value">
                                <input class="edit-control" type="password" name="confirm-pwd" placeholder="重新输入新密码">
                                <div class="edit-hint"></div>
                            </div>
                        </label>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-bottom">
            <button data-type="1" data-confirm class="form-editor add btn">确定</button>
        </div>
    </div>
    </body>
    </html>
</t:template>
