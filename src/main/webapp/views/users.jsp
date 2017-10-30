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

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=Edge">
        <meta name="_csrf" content="${_csrf.token}"/>
        <title>Users</title>
        <!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"/>
        <![endif]-->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" type="text/javascript"></script>

        <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" type="text/css"/>
        <script src="<c:url value="/resources/js/validator.js"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/js/modalForm.js"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/js/updateData.js"/>" type="text/javascript"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jsSHA/2.2.0/sha.js" type="text/javascript"></script>
    </head>
    <body>
        <input id="appUrl" type="hidden" value="${appUrl}"/>
        <header>
            <h1>Library Web Application</h1>
        </header>
        <nav>
            <ul class="top-menu">
                <li><a href="${booksUrl}">Books</a></li>
                <li><a href="${usersUrl}">Users</a></li>
                <li><i class="username">${pageContext.request.userPrincipal.name}&nbsp;</i><a href="javascript:document.getElementById('logout').submit()">[Logout]</a></li>
            </ul>
            <form id="logout" action="${logoutUrl}" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </nav>
        <main>
            <button onclick="modal.get('/user/add');" id="addUser" class="button">Add new user</button>
            <table border="1" class="table" id="usersTable">
                <tr>
                    <td class="header">Username</td>
                    <td class="header">Delete</td>
                </tr>
                <c:forEach var="user" items="${userList}">
                    <tr id="${user.userId}">
                        <td>
                            <a onclick="modal.get('/user/${user.userId}');" class="a-without-href">${user.login}</a>
                        </td>
                        <td class="table-button">
                            <button onclick="user.deleteUser(${user.userId});" class="table-button">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </main>
        <footer>
            <div class="footer">Copyright&nbsp;&copy;&nbsp;2017</div>
        </footer>
        <div class="modal-form"></div>
        <div class="overlay" onclick="modal.close();"></div>
    </body>
</html>