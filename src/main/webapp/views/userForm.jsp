<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}"/>
<c:set var="appUrl" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}"/>

<span class="modal-close" onclick="modal.close();">x</span>
<c:if test="${empty user.userId}"> <h2> Add new user </h2></c:if>
<c:if test="${not empty user.userId}">  <h2> Edit user </h2></c:if>
<p class="validate-msg"/>
<form:form method="post" action="${appUrl}/user" modelAttribute="user" id="userForm" onsubmit="return validator.validateUserForm();">
    <form:hidden path="userId" id="userId"/>
    <label for="login">Username</label>
    <form:input path="login" type="text" id="login" value="${user.login}" class="validate-fields text ui-widget-content ui-corner-all" required="true"/>
    <c:if test="${not empty user.userId}">
        <label for="oldPassword">Old password</label>
        <input type="password" id="oldPassword" class="validate-fields text ui-widget-content ui-corner-all"/>
    </c:if>
    <label for="password">New password</label>
    <form:input path="password" type="password" id="password" class="validate-fields text ui-widget-content ui-corner-all password"/>
    <label for="rePassword">Reenter new password</label>
    <input type="password" id="rePassword" class="validate-fields text ui-widget-content ui-corner-all"/>
    <div class="modal-buttons">
        <button type="submit" id="save" class="button">Save</button>
        <button type="button" id="cancel" class="button" onclick="modal.close();">Cancel</button>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>
