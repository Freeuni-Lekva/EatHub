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
    <style>
        body {
            overflow-y: hidden;
        }

        #room-chat {
            position: absolute;
            top: 5.8%;
            left: 0%;
            width: 40%;
            height: 92%;
        }

        #room-restaurant-meals {
            position: absolute;
            top: 5.8%;
            left:40%;
            width: 30%;
            height: 94.2%;
            background-color: aliceblue;
        }

        #chosen-meals {
            position: absolute;
            top: 5.8%;
            left: 70%;
            width: 30%;
            height: 94.2%;
        }

        .photo_pos {
            position: absolute;
            top: 96.5%;
            left: 75%;
            width: 4%;
            height: 4%;
        }

        #invitations {
            position: absolute;
            left: 1%;
            top: 89%;
            height: 10%;
            width: 20%;
        }

        #invited-user {
            margin-bottom: 5%;
            width: 100%;
            text-align: center;
        }

        #invite_button {
            width: 116%;

        }

        #menu_place {
            position: absolute;
            left: 5%;
            top: 42%;
            height: 43%;
            width: 95%;
        }

        #button_place {
            position: absolute;
            top: 82.2%;
            left: 25%;
            width: 50%;
        }

        #meal-choose-button {
            position: absolute;
            left: 25%;
            width: 50%;
        }

        #cost_txt {
            position: relative;
            margin-top: 2.5vw;
            text-align: center;
            font-size: 1.5vw;
        }

        #restaurant_name {
            text-align: center;
            font-weight: bold;
            font-size: 1.6vw;
            margin-top: 3%;
        }

        #picture_place {
            position: absolute;
            left: 25%;
            width: 50%;
            height: 20%;
            top: 8%;
        }

        #img_style {
            width: 100%;
            height: 100%;
        }

        #menu_name {
            position: absolute;
            top: 30%;
            text-align: center;
            left: 30%;
            font-size: 1.4vw;
        }

        #meal-search {
            position: absolute;
            top: 35%;
            left: 20%;
            width: 60%;
        }

        th, td {
            padding: 5px;
            text-align: center;
        }

        #chosen_meals_txt {
            text-align: center;
            padding: 3%;
            font-weight: bold;
            font-size: 1.6vw;
        }

        #chosen_meals_place{
            position: absolute;
            overflow-y: auto;
            left: 13%;
            top: 10%;
            height: 68%;
        }

        #cost_place {
            position: absolute;
            top: 80%;
            font-size: 1.2vw;
            font-weight: bold;
            left: 20%;
        }


        #date_place {
            position: absolute;
            left: 4%;
            top: 85%;
            width: 93%;
        }

        #pay-buttons {
            position: absolute;
            top: 93.5%;
            width: 100%;
        }

        #chosen-cost {
            font-size: 1.2vw;
            font-weight: bold;
        }

        #split {
            position: absolute;
            left: 5%;
            width: 40%;
            margin-right: 10%;
        }

        #pay{
            position: absolute;
            left: 55%;
            width: 40%;
        }

        #change-chosen-time {
            width: 28%;
        }

        #datetime {
            font-weight: bold;
            font-size: 1vw;
        }

        #start_page_button {
            position: absolute;
            top: 1.25%;
            left: 93%;
            width: 6%;
            height: 5%;
        }

        #my_button {
            padding: 1% 2%;
            margin: 1% 0;
            box-sizing: border-box;
        }

    </style>
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
            <%--            <button id="finish" class="button" onclick="finishChoosing()">finish</button>--%>
            <%--            <button id="continue" class="button" onclick="continueChoosing()">continue</button>--%>
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