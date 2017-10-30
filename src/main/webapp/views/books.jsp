<%@ page language="java" contentType="text/html;charset=UTF-8"
		 pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}"/>
<c:set var="appUrl" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}"/>
<c:url value="/books" var="booksUrl"/>
<c:url value="/users" var="usersUrl"/>
<c:url value="/logout" var="logoutUrl"/>
<c:set var="currentUser" value="${pageContext.request.userPrincipal.name}"/>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge">
		<meta name="_csrf" content="${_csrf.token}"/>
		<title>Books</title>
		<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" type="text/css"/>
		<!--[if lt IE 9]>
		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"/>
		<![endif]-->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js" type="text/javascript"></script>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" type="text/javascript"></script>
		<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" type="text/css"/>
		<script src="<c:url value="/resources/js/updateData.js"/>" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/modalForm.js"/>" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/renderTable.js"/>" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/validator.js"/>" type="text/javascript"></script>
	</head>
	<body>
		<input id="appUrl" type="hidden" value="${appUrl}"/>
		<input id="quantity" type="hidden"/>
		<header>
			<h1>Library Web Application</h1>
		</header>
		<nav>
			<ul class="top-menu">
				<li><a href="${booksUrl}">Books</a></li>
				<li><a href="${usersUrl}">Users</a></li>
				<li><i class="username">${currentUser}&nbsp;</i><a href="javascript:document.getElementById('logout').submit()">[Logout]</a></li>
			</ul>
			<form id="logout" action="${logoutUrl}" method="post" >
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
		</nav>
		<main>
			<button onclick="modal.get('/book/add');" id="addBook" class="button">Add new book</button>
			<table border="1" class="table" id="booksTable">
				<tr>
					<td class="header">ISBN</td>
					<td class="header header-sort sorted" id="header-author">Author &uarr;</td>
					<td class="header sorted column-title" id="header-title">Title</td>
					<td class="header">Taken by</td>
					<td class="header">Delete</td>
				</tr>
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
			</table>
			<a class="a-without-href a-render" id="showMore">Show more</a>
		</main>
		<footer>
			<div class="footer">Copyright&nbsp;&copy;&nbsp;2017</div>
		</footer>
		<div class="modal-form"></div>
		<div class="overlay" onclick="modal.close();"></div>
	</body>
</html>