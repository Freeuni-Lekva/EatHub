<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page import="ge.eathub.dto.RoomDto" %>
<%@ page import="java.util.List" %>
<%@ page import="ge.eathub.service.RoomService" %>
<%@ page import="ge.eathub.listener.NameConstants" %>
<%@ page import="ge.eathub.dao.UserDao" %>
<%@ page import="ge.eathub.service.UserService" %>
<%@ page import="java.util.Optional" %>
<%@ page import="ge.eathub.models.User" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<head>
    <title> EAT HUB </title>
</head>
<body>
<div>
    <%  ServletContext sc = request.getServletContext();
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        UserService userService = (UserService) sc.getAttribute(NameConstants.USER_SERVICE);
        Optional<UserDto> optUser = userService.getUserDtoByUsername(user.getUsername());
        if (optUser.isPresent()){
            user = optUser.get();
            request.getSession().setAttribute(UserDto.ATTR, user);
        }
    %>
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

    <%
        RoomService roomService = (RoomService) sc.getAttribute(NameConstants.ROOM_SERVICE);
        List<RoomDto> list = roomService.getAllRoomByUserID(user.getUserID());
    %><h3>Active rooms</h3>
    <%
        for (RoomDto dto : list) {
            if (dto.isActive()) {
    %>
    <li><%="Room ID: " + dto.getRoomID() + ", Restaurant: " + dto.getRestaurantName() + " , " + dto.getRestaurantLocation()%>
    </li>
    <%
            }
        }
    %>
    <h3>Rooms history</h3>
    <%
        for (RoomDto dto : list) {
            if (!dto.isActive()) {
    %>
    <li><%="Room id: " + dto.getRoomID() + " Restaurant: " + dto.getRestaurantName() + " , " + dto.getRestaurantLocation()%>
    </li>
    <%
            }
        }
    %>

</div>
<div>
    <a href="<c:url value="/restaurants"/>"> Choose Restaurant </a>
</div>
<div>
    <a href="<c:url value="/join-room"/>"> Join Room </a>
</div>
<form action="<c:url value="/logout"/>" method="get">
    <button type="submit">LogOut</button>
</form>
</body>
</html>
