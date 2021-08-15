<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.servlets.RestaurantsServlet" %>
<%@ page import="ge.eathub.dao.impl.MySqlRestaurantDao" %>
<%@ page import="ge.eathub.models.Restaurant" %>
<%@ page import="java.util.List" %>
<%@ page import="ge.eathub.listener.NameConstants" %>
<%@ page import="ge.eathub.models.Meal" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <title>Restaurant</title>
    <style>
        body {
            margin: 0;
            overflow-x: hidden;
        }

        #upper_part {
            position: absolute;
            height: 10%;
            width: 100%;
            background-color: lightgreen;
            margin: 0;
        }

        #first_part {
            border: 2px dashed lightgreen;
            position: absolute;
            margin: 1%;
            top: 10%;
            width: 48.5%;
            border-radius: 10px;
        }

        #second_part {
            border: 2px dashed lightgreen;
            position: absolute;
            margin: 1%;
            top: 10%;
            left: 50%;
            width: 48.5%;
            border-radius: 10px;
        }

        #welcome_text {
            margin: 0;
            position: absolute;
            top: 50%;
            left: 50%;
            -ms-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
            text-align: center;
            font-size: 3vw;
        }

        .title_text {
            text-align: center;
            font-size: 1.7vw;
            margin-bottom: 1.5vw;
            margin-top: 1.5vw;
        }

        #restaurant {
            position: relative;
            left: 10%;
            margin-bottom: 3%;
            width: 80%;
        }

        #restaurant_image {
            left: 10%;
            width: 100%;
        }

        #restaurant_name {
            text-align: center;
            font-size: 1.6vw;
            margin-bottom: 1.5%;
        }

        #restaurant_filter {
            position: absolute;
            width: 50%;
        }

        #meal_filter {
            position: absolute;
            left: 50%;
            width: 50%;
        }

        #submit_button {
            width: 100%;
            text-align: center;
        }

        #button_style {
            font-size: 0.9vw;
            padding: 1%;
            border-radius: 10px;
            bottom: 0;
        }

        #button_style:hover {
            background-color: lightgreen;
        }

        #error_text {
            text-align: center;
            font-weight: bold;
        }

        .restaurant_title {
            text-align: center;
            font-weight: bold;
            font-size: 1.2vw;
        }

        .meal_class {
            position: relative;
            left: 10%;
            width: 80%;
        }

        .meal_image {
            left: 10%;
            width: 100%;
        }


    </style>
</head>
<body>
<div id="upper_part">
    <div id="welcome_text">Welcome to the Restaurant Page</div>
</div>
<div id="first_part">

    <div class="title_text">
        Filter
    </div>
    <hr class="line">


    <form action="<c:url value="/restaurants?filter"/>" method="post">
        <div>
            <label id="restaurant_filter"> Restaurants:
                <select name="filter_option" id="filter_option">
                    <option value="0">Search in all</option>
                    <%
                        ServletContext sc = request.getServletContext();
                        MySqlRestaurantDao dao = (MySqlRestaurantDao) sc.getAttribute(NameConstants.RESTAURANT_DAO);
                        List<Restaurant> restaurants = dao.getAllRestaurant();
                        for (Restaurant restaurant : restaurants) {%>
                    <option value="<%=restaurant.getRestaurantID()%>"><%=restaurant.getRestaurantName()%>
                    </option>
                    <%
                        }

                    %>
                </select>
            </label>


            <label id="meal_filter"> Meal name:
                <input type='text' placeholder='Meal name:' name='filter_meal_name' id="meal_name"/>
            </label>
        </div>
        <br>
        <br>
        <div id="submit_button">
            <input id="button_style" type='submit' value='submit'/>
        </div>
    </form>

    <br>
    <% String error = (String) request.getAttribute(RestaurantsServlet.ERROR_ATTR);
        if (error != null) {%>
    <div id="error_text">
        <%=error%>
    </div>
    <% }%>


    <% Map<Restaurant, List<Meal>> map = (Map<Restaurant, List<Meal>>) request.getAttribute(RestaurantsServlet.SUCCESS_RESTAURANT_ATTR);
        String title = (String) request.getAttribute(RestaurantsServlet.RESTAURANT_NAME_ATTR);
        if (title != null) {%>
    <div class="restaurant_title">
        <%=title%>
    </div>
    <br>
    <div class="meal_class">
        <%
            }
            if (map != null) {
                for (Restaurant restaurant : map.keySet()) {
        %>
        <div class="restaurant_title"><%="Restaurant name: '" + restaurant.getRestaurantName() + "'"%>
        </div>
        <br>
        <%

            for (Meal meal : map.get(restaurant)) {%>
        <input class="meal_image" type="image" src=<%="images/Meals/" + meal.getMealUrl()%>><br>
        <div class="restaurant_title">
            <%="Meal name: '" + meal.getMealName() + "', Meal price: " + meal.getMealPrice() + ", Time: " + meal.getCookingTime()%>
        </div>
        <%}%>
        <br>
        <%}%>
        <% } %>
    </div>

    <div class="meal_class">
        <% List<Meal> meals = (List<Meal>) request.getAttribute(RestaurantsServlet.SUCCESS_MEALS_ATTR);
            if (meals != null) {
                for (Meal meal : meals) {%>
        <input class="meal_image" type="image" src=<%="images/Meals/" + meal.getMealUrl()%>><br>
        <div class="restaurant_title">
            <%="Meal name: '" + meal.getMealName() + "', Meal price: " + meal.getMealPrice() + ", Time: " + meal.getCookingTime() %>
        </div>
        <br><br>
        <%}%>
        <% } %>
        <br>
    </div>
</div>

<div id="second_part">
    <div class="title_text">
        Available Restaurants
    </div>
    <hr class="line">
    <%
        for (Restaurant restaurant : restaurants) {%>
    <div id="restaurant">
        <a href="/newRoom?id=<%=restaurant.getRestaurantID()%> ">
            <div id="restaurant_name">
                <%= restaurant.getRestaurantName() + ", " + restaurant.getLocation()%>
            </div>
            <input id="restaurant_image" type="image"
                   src=<%="images/Restaurants/" + restaurant.getRestaurantUrl()%>><br>
        </a>
    </div>

    <%
        }

    %>
    <br>
</div>

</body>
</html>