<%@ page import="ge.eathub.dao.impl.MySqlRestaurantDao" %>
<%@ page import="ge.eathub.models.Meal" %>
<%@ page import="ge.eathub.listener.NameConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="ge.eathub.models.Room" %>
<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page import="ge.eathub.service.RoomService" %>
<%@ page import="java.util.Optional" %>
<%@ page import="org.mockito.internal.matchers.Or" %>
<%@ page import="ge.eathub.models.Order" %>
<%@ page import="ge.eathub.service.impl.RoomServiceImpl" %>
<%@ page import="ge.eathub.service.impl.OrderServiceImpl" %>
<%@ page import="ge.eathub.service.OrderService" %>
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
        <form action="<c:url value="/newRoom"/>" method="post">
            <%
                ServletContext sc = request.getServletContext();
                MySqlRestaurantDao dao = (MySqlRestaurantDao) sc.getAttribute(NameConstants.RESTAURANT_DAO);
                Room room = ((Room) request.getSession().getAttribute("NEW_ROOM"));
                long restaurantID = room.getRestaurantID();
                List<Meal> meals = dao.getAllMeals(restaurantID);
                UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
                OrderServiceImpl orderService = (OrderServiceImpl) sc.getAttribute(NameConstants.ORDER_SERVICE);
                for (Meal meal : meals) {%>
            <%
                Optional<Order> order = orderService.getOrderByID(user.getUserID(), room.getRoomID(), meal.getMealID());
                int amount = 0;
                if (order.isPresent()) {
                    amount = order.get().getQuantity();
                }
            %>
            <li>
                <%="Meal Name: " + meal.getMealName() + " Price: " + meal.getMealPrice() + " ID=" + meal.getMealID()%>
                <input type='number'
                       name="<%=meal.getMealID()%>"
                       min="0"
                       value="<%=amount%>">
            </li>
            <%}%>
            <input type='submit' value='submit'/><br>
        </form>
    </div>
</div>
</body>
</html>