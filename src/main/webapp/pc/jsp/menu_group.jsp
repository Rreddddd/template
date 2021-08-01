<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="menus" type="java.util.Set"--%>
<ul class="application-group">
    <c:forEach var="menu" items="${menus}">
        <c:set var="innerMenu" value="${menu}" scope="request"/>
        <c:import url="menu_item.jsp"/>
    </c:forEach>
</ul>
