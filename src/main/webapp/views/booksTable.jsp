<%@ page language="java" contentType="text/html;charset=UTF-8"
		 pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}"/>
<c:set var="appUrl" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}"/>
<c:set var="currentUser" value="${pageContext.request.userPrincipal.name}"/>

<c:forEach var="book" items="${bookList}">
	<tr id="${book.bookId}">
		<td>
			<a onclick="modal.get('/book/${book.bookId}');" class="a-without-href">${book.isbn}</a>
		</td>
		<td>${book.author}</td>
		<td class="title">${book.title}</td>
		<td class="table-button">
			<c:if test="${empty book.takenBy}">
				<button onclick="book.takeBook(${book.bookId},'${currentUser}');" class="table-button" id="takeBook">Take</button>
			</c:if>
			<c:if test="${(not empty book.takenBy) and (book.takenBy.login eq currentUser)}">
				<button onclick="book.giveBook(${book.bookId});" class="table-button">Give back</button>
			</c:if>
			<c:if test="${(not empty book.takenBy) and (book.takenBy.login ne currentUser)}">
				${book.takenBy.login}
			</c:if>
		</td>
		<td class="table-button">
			<button onclick="book.deleteBook(${book.bookId});" class="table-button">Delete</button>
		</td>
	</tr>
</c:forEach>