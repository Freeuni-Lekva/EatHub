<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> EAT HUB </title>
</head>
<body>
<div>
    <% UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);%>
    <h1>Hello <%= user.getUsername() %>
    </h1>
    <h1>UserID <%= user.getUserID() %>
    </h1>
    <h2>email <%= user.getEmail() %>
    </h2>
    <h2>balance <%= user.getBalance() %>
    </h2>
    <h2>role <%= user.getRole().name() %>
    </h2>

</div>
    <div>
        <a href="<c:url value="/restaurants"/>"> Choose Restaurant </a>
    </div>
    <div>
        <a href="<c:url value="/join-room"/>"> Join Room </a>
    </div>
</body>
</html>
