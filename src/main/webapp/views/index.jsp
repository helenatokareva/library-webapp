<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url value="/books" var="booksUrl"/>
<c:url value="/users" var="usersUrl"/>
<c:url value="/logout" var="logoutUrl"/>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge">
		<title>Library</title>
		<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" type="text/css"/>
		<!--[if lt IE 9]>
		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"/>
		<![endif]-->
		<link rel="icon" href="data:;base64,=">
	</head>
	<body>
		<header>
			<h1>Library Web Application</h1>
		</header>
		<nav>
			<ul class="top-menu">
				<li><a href="${booksUrl}">Books</a></li>
				<li><a href="${usersUrl}">Users</a></li>
				<li><i class="username">${pageContext.request.userPrincipal.name}&nbsp;</i><a href="javascript:document.getElementById('logout').submit()">[Logout]</a></li>
			</ul>
			<form id="logout" action="${logoutUrl}" method="post" >
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
		</nav>
		<main>

		</main>
		<footer>
			<div class="footer">Copyright&nbsp;&copy;&nbsp;2017</div>
		</footer>
	</body>
</html>