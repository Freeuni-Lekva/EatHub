<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page import="ge.eathub.models.Room" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<script type="application/javascript" src="../scripts/chat.js"></script>

<head>
    <title>New Room</title>
</head>
<body>
<script>window.onload = () => {
    <% UserDto user = ((UserDto) request.getSession().getAttribute(UserDto.ATTR));
      Room room = ((Room) request.getSession().getAttribute("NEW_ROOM"));%>
    console.log('<%= user.getUsername()%>', <%=room.getRoomID()%>);
    joinRoom('<%= user.getUsername()%>', <%=room.getRoomID()%>);
}</script>

</body>
</html>