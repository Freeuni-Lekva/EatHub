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
<span class="error" id="join-error"></span>
<script>window.onload = () => {
    <% UserDto user = ((UserDto) request.getSession().getAttribute(UserDto.ATTR));
      Room room = ((Room) request.getSession().getAttribute(Room.ATTR));%>
    joinRoom('<%= user.getUsername()%>', <%=room.getRoomID()%>);
}</script>

</body>
</html>