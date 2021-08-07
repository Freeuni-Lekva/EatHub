<%@ page import="ge.eathub.dto.UserDto" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/common.css"/>">
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/chat.css"/>">
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/auth.css"/>">
    <script type="application/javascript" src="<c:url value="/scripts/chat.js"/>"></script>
    <meta charset="utf-8">
    <title>Join Room</title>

</head>
<body>
<div id="title">Join Room</div>
<form id="join" onsubmit="return false;">
    <input type="number" id="roomID" placeholder="room ID" required min=1 />
    <% UserDto user = ((UserDto) request.getSession().getAttribute(UserDto.ATTR));%>
    <button class="button" onclick="joinRoom('<%= user.getUsername()%>',  roomID.value)">Join room chat
    </button>
    <span class="error" id="join-error"></span>
</form>
</body>
</html>