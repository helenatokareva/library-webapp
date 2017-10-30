<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}"/>
<c:set var="appUrl" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}"/>
<script src="<c:url value="/resources/js/jquery.maskedinput.min.js"/>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $.mask.definitions['x'] = '[-0-9]';
        $.mask.definitions['9'] = '';
        $("#form-isbn").mask("978-xxxxxxxxxxx-x");
    });
</script>

<span class="modal-close" onclick="modal.close();">x</span>
<c:if test="${empty book.bookId}">
    <h2>Add new book</h2>
    <p>ISBN is a set of numbers divided into 5 parts separated with hyphens<br>(example: 978-9-99999-999-9).</p>
</c:if>
<c:if test="${not empty book.bookId}"><h2>Edit book</h2></c:if>
<p class="validate-msg">All form fields are required</p>
<form:form method="post" action="${appUrl}/book" modelAttribute="book" id="bookForm" onsubmit="return validator.validateBookForm();">
    <form:hidden path="bookId" id="bookId"/>
    <label for="isbn">ISBN</label>
    <form:input path="isbn" type="text" value="${book.isbn}" id="form-isbn" class="validate-fields text ui-widget-content ui-corner-all" required="true"/>
    <label for="author">Author</label>
    <form:input path="author" type="text" id="form-author" value="${book.author}" class="validate-fields text ui-widget-content ui-corner-all" required="true"/>
    <label for="title">Title</label>
    <form:input path="title" type="text" id="form-title" value="${book.title}" class="validate-fields text ui-widget-content ui-corner-all" required="true"/>

    <div class="modal-buttons">
        <button type="submit" id="save" class="button">Save</button>
        <button type="button" id="cancel" class="button" onclick="modal.close();">Cancel</button>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>