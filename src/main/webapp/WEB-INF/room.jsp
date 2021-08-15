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
                <label for="file-img">
                    <img class="photo_pos" src="https://cdn.iconscout.com/icon/free/png-256/photo-size-select-actual-1782180-1512958.png">
                </label>


                <input type="file" name="img" accept="image/*" id="file-img" placeholder=" choose image" style='display: none'>
                    <%--                    --%>
                </input>
                <input type="text" id="message" placeholder="Enter a message"/>
                <button class="button" onclick="sendMessage()">Send</button>
            </form>
        </div>
        <div id="invitations">
            <form id="invitation" onsubmit="return false;">
                <input type="text" id="invited-user" placeholder="Invite User"/>
                <button class="button" id = "invite_button" onclick="sendInvitation('<%=user.getUsername()%>')">Invite</button>
                <span class="error" id="invitation-error"> </span>
            </form>
        </div>
    </div>

    <div id="room-restaurant-meals">
        <div id = "restaurant_name">
            <%=rest.getRestaurantName()%>
        </div>
        <div id = "picture_place">
            <img id = "img_style" src="<c:url value='<%="images/Restaurants/"+rest.getRestaurantUrl()%>'/>"
                 alt="Meal IMG"/>
        </div>

        <div id = "menu_name">
            Restaurant menu
        </div>
        <div id="meal-search">
            <form onsubmit="return false;">
                <input id="search-text" type="text" onchange="searchMeal()" placeholder="meal"/>
                <button class="button" onclick="searchMeal()">Search</button>
                <span class="error" id="searched-meal-error"> </span>
            </form>
        </div>
        <div id = "menu_place">
            <form id="restaurant-menu" onsubmit="return false;">
                <table id="menu-list">
                    <thead>
                    <tr>
                        <th>
                        </th>
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
            </form>
        </div>
        <div id = "button_place">
            <div id = "cost_txt">
                <span>Your Total cost: </span>
                <span id="chosen-cost"> <%=total%></span>
                <span> GEL </span>
            </div>
            <br>
            <button id="meal-choose-button" class="button" onclick="chooseMeals()">choose</button>
            <span class="error" id="choose-error"> </span>
        </div>
    </div>
    <div id="chosen-meals">
        <div id = "chosen_meals_txt">
            Chosen Meals:
        </div>
        <div id = "chosen_meals_place">
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
        </div>
        <div id = "cost_place" style="flex-direction: row">
            <span>Total cost: </span>
            <span id="chosen-meals-cost"> 0</span>
            <span>Estimated Time: </span>
            <span id="estimated-time"> 0</span>
        </div>

        <span class="error" id="chosen-meals-error"> </span>
        <div id = "date_place">
            <label id="datetime"> chosen time:
                <input type="datetime-local" id="date-time"
                       value="2021-08-13T19:30"
                       min="2021-08-13T19:30" max="2022-08-13T19:30">
            </label>
            <button id="change-chosen-time" class="button" onclick="chooseTime()">Change Time</button>

        </div>

        <div id="pay-buttons">
            <button id="split" class="button" onclick="splitBill()">split bill</button>
            <button id="pay" class="button" onclick="payForAll()">Pay</button>
            <span id="transaction-error" class="error"></span>
        </div>
    </div>
    <div id = "start_page_button">
        <a href="/start">
            <button id = "my_button">Start Page</button>
        </a>
    </div>
</div>


</body>
</html>