<%@ page import="ge.eathub.security.Authenticator" %>
<%@ page import="ge.eathub.utils.AuthenticatorFactory" %>
<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page import="static ge.eathub.servlets.JoinRoomServlet.BASIC_AUTH" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" href="../styles/common.css">
    <link type="text/css" rel="stylesheet" href="../styles/chat.css">
    <script type="application/javascript" src="../scripts/chat.js"></script>
    <title>Chat</title>
</head>
<body>
<div id="title">Join Room</div>
<div style="display: block" id="contacts"></div>
<div style="display: block" id="chat">
    <div id="messages" autofocus></div>
    <form id="chat-controls" onsubmit="return false;">
        <input type="text" id="message" placeholder="Enter a message"/>
        <button class="button" onclick="sendMessage()">Send</button>
    </form>
</div>

</body>
</html>