<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.servlets.RegistrationServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8" >
<head>
    <title>register user</title>
</head>
<body>
<div>
    <% String regError = (String) request.getAttribute(RegistrationServlet.ERROR_ATTR);
        if (regError != null) {%>
    <label><%=regError%>
    </label>
    <% }%>
    <p> enter name and password</p>
    <form action="<c:url value="/register"/>" method="post">
        <label> user name:
            <input type='text' placeholder='Username:' name='username' autocomplete='username' required/>
        </label>
        <br>
        <label>
            password:
            <input type='password' placeholder='Password:' name='password' autocomplete='new-password' required/>
        </label>
        <br>
        <label>
            email:
            <input type='text' placeholder='email:' name='email' autocomplete='email' required/>
        </label>
        <br>
        <input type='submit' value='register'/>
    </form>
</div>
<a href="<c:url value="/login"/>"> log in </a>
</body>
</html>
