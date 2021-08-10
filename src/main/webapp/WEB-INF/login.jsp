<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.servlets.LoginServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <title>Login</title>
</head>
<body>
<h1> Welcome to EATHUB</h1>
<div>
    <% String error = (String) request.getAttribute(LoginServlet.ERROR_ATTR);
        if (error != null) {%>
    <label><%=error%>
    </label>
    <% }%>
    <p> please log in</p>
    <form action="<c:url value="/login"/>" method="post">
        <label>user name:
            <input type='text' placeholder='Username:' name='username' autocomplete='username' required/>
        </label>
        <br>
        <label> password:
            <input type='password' placeholder='Password:' name='password' autocomplete='current-password' required/>
        </label>
        <br>
        <input type='submit' value='Login'/>
    </form>
</div>
<a href="<c:url value="/register"/>"> create new account </a>
</body>
</html>
