<%@ page import="ge.eathub.servlets.ConfirmServlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <title>Confirmation</title>
</head>
<body>
<%--    <% request.getSession().get  %>--%>
<% String error = (String) request.getAttribute(ConfirmServlet.ERROR_ATTR);
    if (error != null) {%>
<label><%=error%>
</label>
<% }%>
<div id="title"> confirm</div>
<form action="<c:url value="/confirm"/>" method="post">
    <p> please confirm registration, token is sent on your email</p>
    <label>
        <input name="confirm-token" placeholder="confirmation token "/>
    </label>
    <input type='submit' value='confirm'/>

    <span class="error" id="confirmation-error"></span>
</form>
</body>
</html>
