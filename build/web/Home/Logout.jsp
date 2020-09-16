<%@page import="dev.salah.Utils"%>
<% Utils.markAsPrivate(request, response); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="user" value="${null}" />
<c:set var="cart" value="${null}" />
<c:redirect url="/" />