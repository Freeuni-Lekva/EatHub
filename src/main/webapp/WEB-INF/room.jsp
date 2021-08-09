<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/room.css"/>">
    <title>room</title>
</head>
<body>
<div id="title">Join Room</div>
<div id="room">
    <div id="room-chat">
        <jsp:include page="chat.jsp"/>
    </div>

    <div id="room-restaurant-meals">
<%--        <jsp:include page="../show-restaurant.jsp"> <jsp:param name="id" value="1"/> </jsp:include>--%>
        <img src="../images/churchkhela.jpg" alt="churchkhela" >
    </div>
</div>
</body>
</html>
