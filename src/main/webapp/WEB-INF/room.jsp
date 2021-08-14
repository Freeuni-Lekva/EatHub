<%@ page import="ge.eathub.dao.impl.MySqlRestaurantDao" %>
<%@ page import="ge.eathub.models.Meal" %>
<%@ page import="ge.eathub.listener.NameConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="ge.eathub.models.Room" %>
<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page import="java.util.Optional" %>
<%@ page import="ge.eathub.models.Order" %>
<%@ page import="ge.eathub.service.impl.OrderServiceImpl" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="ge.eathub.models.Restaurant" %>
<%@ page import="java.math.BigInteger" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/room.css"/>">
    <link type="text/css" rel="stylesheet" href="../styles/common.css">
    <link type="text/css" rel="stylesheet" href="../styles/chat.css">
    <script type="application/javascript" src="../scripts/chat.js"></script>

    <title>Room</title>
</head>
<%
    ServletContext sc = request.getServletContext();
    MySqlRestaurantDao restDao = (MySqlRestaurantDao) sc.getAttribute(NameConstants.RESTAURANT_DAO);
    Room room = ((Room) request.getSession().getAttribute(Room.ATTR));
    long restaurantID = room.getRestaurantID();
    Optional<Restaurant> restaurantOptional = restDao.getRestaurantById(restaurantID);
    List<Meal> meals = restDao.getAllMeals(restaurantID);
    OrderServiceImpl orderService = (OrderServiceImpl) sc.getAttribute(NameConstants.ORDER_SERVICE);
    UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
    Restaurant rest = restaurantOptional.get();%>

<body>
<div id="title">Room</div>
<div id="room">
    <div id="room-chat">
        <div style="display: block" id="contacts"></div>
        <div style="display: block" id="chat">
            <div id="messages" autofocus></div>
            <form id="chat-controls" onsubmit="return false;">
                <input type="text" id="message" placeholder="Enter a message"/>
                <input type="file" name="img" accept="image/*" id="file-img" placeholder=" choose image"/>
                <button class="button" onclick="sendMessage()">Send</button>
            </form>
        </div>
    </div>

    <div id="room-restaurant-meals">
        <h2><%=rest.getRestaurantName()%>
        </h2>
        <img width="150" height="100" src="<c:url value='<%="images/Restaurants/"+rest.getRestaurantUrl()%>'/>"
             alt="Meal IMG"/>
        <h1> Restaurant menu </h1>
        <div id="meal-search">
            <form onsubmit="return false;">
                <input id="search-text" type="text" onchange="searchMeal()" placeholder="meal"/>
                <button class="button" onclick="searchMeal()">Search</button>
                <span class="error" id="searched-meal-error"> </span>
            </form>
        </div>
        <form id="restaurant-menu" onsubmit="return false;">
            <table id="menu-list">
                <thead>
                <tr>
                    <th>Meal
                    </th>
                    <th> Price In GEL
                    </th>
                    <th style="float: left"> your chosen number</th>
                </tr>
                </thead>
                <tbody>
                <%
                    BigDecimal total = new BigDecimal(BigInteger.ZERO);
                    for (Meal meal : meals) {
                        Optional<Order> order = orderService.getOrderByID(user.getUserID(), room.getRoomID(), meal.getMealID());
                        int amount = 0;
                        if (order.isPresent()) {
                            amount = order.get().getQuantity();
                            total = total.add(meal.getMealPrice().multiply(BigDecimal.valueOf(amount)));
                        }
                %>
                <tr>
                    <td><img width="50" height="50" src="<c:url value='<%="images/Meals/"+meal.getMealUrl()%>'/>"
                             alt="Meal IMG"/></td>
                    <td><%=meal.getMealName()%>
                    </td>
                    <td><%=meal.getMealPrice()%>

                    <td>
                        <input type='number'
                               name="<%=meal.getMealID()%>"
                               min="0"
                               value="<%=amount%>">
                    </td>
                </tr>
                <%}%>
                </tbody>
            </table>
            <button class="button" onclick="chooseMeals()">choose</button>
            <div style="flex-direction: row">
                <span> Your Total cost:</span>
                <span id="chosen-cost"> <%=total%></span>
            </div>
            <span class="error" id="choose-error"> </span>
        </form>

    </div>
    <div id="chosen-meals">
        <h3>Chosen Meals:</h3>
        <table id="chosen-meals-table">
            <tr>
                <th>User
                </th>
                <th>Meal
                </th>
                <th>Amount
                </th>
                <th>Cooking Time
                </th>
                <th>Price
                </th>
            </tr>
        </table>
        <div style="flex-direction: row">
            <span>Total cost: </span>
            <span id="chosen-meals-cost"> 0</span>
            <span>Estimated Time: </span>
            <span id="estimated-time"> 0</span>
        </div>

        <span class="error" id="chosen-meals-error"> </span>

    </div>
</div>
<div id="invitations">
    <form id="invitation" onsubmit="return false;">
        <input type="text" id="invited-user" placeholder="Invite User"/>
        <button class="button" onclick="sendInvitation('<%=user.getUsername()%>')">Invite</button>
        <span class="error" id="invitation-error"> </span>
    </form>
</div>
<div>

    <label id="datetime"> chosen time:
        <input type="datetime-local" id="date-time"
               value="2021-08-13T19:30"
               min="2021-08-13T19:30" max="2022-08-13T19:30">
    </label>
    <button class="button" onclick="chooseTime()">Change Time</button>

</div>

</body>
</html>