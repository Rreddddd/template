<%@ page import="java.util.UUID" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="first" type="java.lang.Boolean"--%>
<%--@elvariable id="menus" type="java.util.Set"--%>
<%--@elvariable id="innerMenu" type="util.Menus.InnerMenu"--%>
<c:set var="menus" value="${innerMenu.children}" scope="request"/>
<c:set var="childrenLength" value="${fn:length(menus)}" scope="page"/>
<li class="application-item${(first==null || first)?" first":""}">
    <c:set var="first" value="${false}" scope="request"/>
    <div class="application-item-wrapper${childrenLength>0?" branch":""}"
         data-item-id="<%=UUID.randomUUID().toString()%>" data-item-url="${innerMenu.menu.module.url}">
        <div class="icon ${innerMenu.menu.iconClass}" style="color: ${innerMenu.menu.iconColor}"></div>
        <div class="name">${innerMenu.menu.title}</div>
        <c:if test="${childrenLength>0}">
            <div class="btn"></div>
        </c:if>
    </div>
    <c:if test="${childrenLength>0}">
        <c:import url="menu_group.jsp"/>
    </c:if>
</li>