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
    <style>
        body {
            margin: 0;
        }

        #upper_part {
            position: absolute;
            left: 0%;
            top: 0%;
            width: 100%;
            height: 8%;
            background-color: lightgreen;
        }

        #upper_text {
            text-align: center;
            font-size: 2vw;
            margin: 0;
            position: absolute;
            top: 50%;
            left: 50%;
            -ms-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
        }

        #first_part {
            align: center;
            padding-left: 1.5%;
            position: absolute;
            left: 2%;
            top: 10%;
            width: 27%;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #second_part {
            position: absolute;
            left: 34%;
            top: 10%;
            width: 27%;
            padding-left: 2%;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #third_part {
            position: absolute;
            left: 67%;
            top: 10%;
            width: 27%;
            padding-left: 2%;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #log_out {
            position: absolute;
            top: 20%;
            left: 88%;
            width: 10%;
            height: 60%;
            border-radius: 10px;
        }

        button:hover {
            background-color: lightgreen;
        }

        .text_title {
            text-align: left;
            font-size: 1.6vw;
        }

        .button_class {
            text-align: center;
            padding: 1.5% 3%;
            margin: 1.5% 0;
            box-sizing: border-box;
            border-radius: 10px;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        .line_style {
            font-size: 1.5vw;
        }

        #choose_restaurant {
            margin-right: 30%;
        }

        #buttons_pos {
            padding-top: 5%;
        }
    </style>
</head>
<body>
    <%  ServletContext sc = request.getServletContext();
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        UserService userService = (UserService) sc.getAttribute(NameConstants.USER_SERVICE);
        Optional<UserDto> optUser = userService.getUserDtoByUsername(user.getUsername());
        if (optUser.isPresent()){
            user = optUser.get();
            request.getSession().setAttribute(UserDto.ATTR, user);
        }
    %>
    <div id = "upper_part">
        <h1 id = "upper_text">
            Hello <%= user.getUsername() %>
        </h1>
        <form action="<c:url value="/logout"/>" method="get">
            <button id = "log_out" type="submit">Log out</button>
        </form>
    </div>
    <div id = "first_part">
        <table>
            <tr>
                <td> <span class = "line_style"> ID: </span></td>
                <td> <span class = "line_style"> <%= user.getUserID() %> </span> </td>
            </tr>
            <tr>
                <td> <span class = "line_style">Email: </span> </td>
                <td> <span class = "line_style"> <%= user.getEmail() %> </span> </td>
            </tr>
            <tr>
                <td> <span class = "line_style">Balance: </span> </td>
                <td> <span class = "line_style"> <%= user.getBalance() + " GEL" %>  </span> </td>
            </tr>
            <tr>
                <td> <span class = "line_style">Role: </span> </td>
                <td> <span class = "line_style"> <%= user.getRole().name() %> </span> </td>
            </tr>
        </table>
        <div id = "buttons_pos">
            <a href="<c:url value="/restaurants"/>">
                <button class="button_class" id = "choose_restaurant"> Choose Restaurant </button>
            </a>
            <a href="<c:url value="/join-room"/>">
                <button class="button_class" id = "join_room"> Join Room </button>
            </a>
        </div>
        <br>
    </div>

    <div id = "second_part">
    <%
        RoomService roomService = (RoomService) sc.getAttribute(NameConstants.ROOM_SERVICE);
        List<RoomDto> list = roomService.getAllRoomByUserID(user.getUserID());
    %><h3 class = "text_title">Active rooms</h3>
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
        <br>
    </div>
    <div id = "third_part">
    <h3 class = "text_title">Rooms history</h3>
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
        <br>
    </div>
</body>
</html>
