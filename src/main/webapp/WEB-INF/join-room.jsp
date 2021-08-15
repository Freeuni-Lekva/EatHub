<%@ page import="ge.eathub.dto.UserDto" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<head>
    <meta charset="utf-8">
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/common.css"/>">
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/chat.css"/>">
    <script type="application/javascript" src="<c:url value="/scripts/chat.js"/>"></script>
    <title>Join Room</title>

</head>
<body>
<form id="join" onsubmit="return false;">
    <input type="number" id="roomID" placeholder="room ID" required min=1/>
    <% UserDto user = ((UserDto) request.getSession().getAttribute(UserDto.ATTR));%>
    <button class="button" onclick="joinRoom('<%= user.getUsername()%>',  roomID.value)">Join room chat
    </button>
    <span class="error" id="join-error"></span>
</form>
</body>
</html>