<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page import="ge.eathub.servlets.LoginServlet" %>
<%@ page import="ge.eathub.servlets.AdminServlet" %>
<%@ page import="ge.eathub.dao.impl.MySqlRestaurantDao" %>
<%@ page import="ge.eathub.models.Restaurant" %>
<%@ page import="java.util.List" %>
<%@ page import="ge.eathub.listener.NameConstants" %>
<%@ page import="org.apache.ibatis.ognl.MapElementsAccessor" %>
<%@ page import="ge.eathub.dao.MealDao" %>
<%@ page import="ge.eathub.models.Meal" %>
<%@ page import="ge.eathub.service.RoomService" %>
<%@ page import="ge.eathub.dto.RoomDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <title> Admin </title>
    <style>
        body {
            background-color: darksalmon;
        }

        #update_or_add{
            position: absolute;
            left: 5%;
            top: 20%;
            width: 25%;
            height: 40%;
            padding: 1vw;
            margin-bottom: 2%;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #update_meal {
            position: absolute;
            left: 36.5%;
            top: 20%;
            width: 25%;
            height: 40%;
            padding: 1vw;
            margin-bottom: 2%;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #add_meal {
            position: absolute;
            left: 68%;
            top: 20%;
            width: 25%;
            height: 40%;
            padding: 1vw;
            margin-bottom: 2%;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #logout_pos {
            position: absolute;
            left: 85%;
            top: 5%;
            height: 5%;
            width: 10%;
        }

        #logout_button {
            width: 100%;
            text-align: center;
            font-size: 1.4vw;
            padding: 4%;
            border-radius: 10px;
        }

        #logout_button:hover {
            background-color: lightgreen;
        }

        .error_or_success {
            position: absolute;
            left: 10%;
            width: 80%;
            top: 10%;
            font-size: 2vw;
            text-align: center;
        }

        #error{
            color: #f02323;
        }

        #success {
            color: lightgreen;
        }

        .title_text {
            text-align: center;
            height: 14%;
            font-size: 1.4vw;
        }

        .line_type {
            width: 100%;
            margin-bottom: 0.7vw;
            font-size: 1.2vw;
        }

        .label_type {
            float: right;
            width: 62%;
            font-size: 1vw;
        }

        .chosen_options {
            float: right;
            width: 64%;
            font-size: 1vw;
        }

        .button_type {
            text-align: center;
            font-size: 0.9vw;
            padding: 1.5%;
            border-radius: 10px;
            bottom: 0;
        }

        .upload_image {
            width: 100%;
            text-align: center;
            font-size: 1.4vw;
            margin-top: 0.8vw;
            margin-bottom: 0.6vw;
        }

        .submit_button {
            width: 100%;
            text-align: center;
        }

        .button_type:hover {
            background-color: lightgreen;
        }

        #active_room {
            position: absolute;
            left: 25%;
            top: 70%;
            width: 30%;
        }

        #room_history {
            position: absolute;
            left: 60%;
            top: 70%;
            width: 20%;
        }

    </style>
</head>
<body>
<div>


    <% String error = (String) request.getAttribute(AdminServlet.ERROR_ATTR);
        if (error != null) {%>
    <div class = "error_or_success" id = "error">
        <%=error%>
    </div>
    <% }%>


    <% String success = (String) request.getAttribute(AdminServlet.SUCCESS_ATTR);
        if (success != null) {%>
    <div class = "error_or_success" id = "success">
        <%=success%>
    </div>
    <% }%>
    <div id = "update_or_add">
        <div class = "title_text">Update or add new restaurant</div>
        <form action="<c:url value="/admin?restaurant"/>" enctype="multipart/form-data" method="post">

            <div class = "line_type"> Restaurants:
                <select name="admin_option" id="admin_option" class = "chosen_options">
                    <option value="0">Add new</option>
                    <%
                        ServletContext sc = request.getServletContext();
                        MySqlRestaurantDao restaurantDao = (MySqlRestaurantDao) sc.getAttribute(NameConstants.RESTAURANT_DAO);
                        List<Restaurant> restaurants = restaurantDao.getAllRestaurant();
                        for (Restaurant restaurant : restaurants) {%>
                    <option value="<%=restaurant.getRestaurantID()%>"><%=restaurant.getRestaurantName()%>
                    </option>
                    <%
                        }

                    %>
                </select>
            </div>

            <div class = "line_type"> Restaurant name:
                <input class = "label_type" type='text' placeholder='Restaurant Name:' name='restaurant_name' required/>
            </div>

            <div class = "line_type"> Location:
                <input class = "label_type" type='text' placeholder='Location:' name='location' required/>
            </div>
            <div class = "line_type">Limit:
                <input class = "label_type" type='number' min="0" placeholder='0:' name='limit' required/>
            </div>

            <div class = "line_type">Rating:
                <input class = "label_type" type='number' min="0" step="0.01" placeholder='0:' name='rating' required/>
            </div>

            <div class = "line_type">Balance:
                <input class = "label_type" type='number' min="0" step="0.1" placeholder='0:' name='balance' required/>
            </div>

            <div class = "upload_image">
                <input type="file" name="restaurant-image" accept="image/*" id="restaurant-image" placeholder="Choose Image" required/><br>
            </div>

            <div class = "submit_button">
                <input class = "button_type" type='submit' value='Submit'/>
            </div>
        </form>
    </div>

    <div id = "update_meal">
        <div class = "title_text">Update a Meal</div>
        <form action="<c:url value="/admin?update_meal"/>" enctype="multipart/form-data" method="post">

            <div class = "line_type"> Meal:
                <select name="meal_option" id="meal_option" class = "chosen_options">
                    <%
                        MealDao mealDao = (MealDao) sc.getAttribute(NameConstants.MEAL_DAO);
                        List<Meal> meals = mealDao.getAllMeals();
                        for (Meal meal : meals) {%>
                    <option value="<%=meal.getMealID()%>"><%=meal.getMealName() + ", " + restaurantDao.getRestaurantById(meal.getRestaurantID()).get().getRestaurantName()%>
                    </option>
                    <%
                        }

                    %>
                </select>
            </div>


            <div class = "line_type"> Meal name:
                <input class = "label_type" type='text' placeholder='Meal Name:' name='meal_name' required/>
            </div>

            <div class = "line_type">Meal price:
                <input class = "label_type" type='number' min="0" step="0.5" placeholder='0:' name='meal_price' required/>
            </div>

            <div class = "line_type">Cooking time:
                <input class = "label_type" type='number' min="0" placeholder='0:' name='cooking_time' required/>
            </div>

            <div class = "upload_image">
                <input type="file" name="file-image-update" accept="image/*" id="file-image-update" placeholder="Choose Image" required/><br>
            </div>

            <div class = "submit_button">
                <input class = "button_type" type='submit' value='Submit'/>
            </div>
        </form>
    </div>

    <div id = "add_meal">
        <div class = "title_text">Add a new Meal</div>
        <form action="<c:url value="/admin?add_meal"/>" enctype="multipart/form-data" method="post">

            <div class = "line_type"> Meal name:
                <input class = "label_type" type='text' placeholder='Add Meal Name:' name='add_meal_name' required/>
            </div>

            <div class = "line_type">Meal price:
                <input class = "label_type" type='number' min="0" step="0.5" placeholder='0:' name='add_meal_price' required/>
            </div>

            <div class = "line_type">Cooking time:
                <input class = "label_type" type='number' min="0" placeholder='0:' name='add_cooking_time' required/>
            </div>

            <div class = "line_type"> Restaurants:
                <select name="meal_admin_option" id="meal_admin_option" class = "chosen_options">
                    <%
                        for (Restaurant restaurant : restaurants) {%>
                    <option value="<%=restaurant.getRestaurantID()%>"><%=restaurant.getRestaurantName()%>
                    </option>
                    <%
                        }

                    %>
                </select>
            </div>

            <div class = "upload_image">
                <input type="file" name="file-image-add" accept="image/*" id="file-image-add"  placeholder="Choose Image" required/><br>
            </div>

            <div class = "submit_button">
                <input class = "button_type" type='submit' value='Submit'/>
            </div>
        </form>
    </div>
    <div id = "logout_pos">
        <form action="<c:url value="/logout"/>" method="get">
            <button id = "logout_button" type = "submit">Log out </button>
        </form>
    </div>

</div>

<div id = "active_room">
    <%
        RoomService roomService = (RoomService) sc.getAttribute(NameConstants.ROOM_SERVICE);
        List<RoomDto> list = roomService.getAllRoomDto();
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
</div>
<div id = "room_history">
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

</body>
</html>
