<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.servlets.RestaurantsServlet" %>
<%@ page import="ge.eathub.dao.impl.MySqlRestaurantDao" %>
<%@ page import="ge.eathub.models.Restaurant" %>
<%@ page import="java.util.List" %>
<%@ page import="ge.eathub.listener.NameConstants" %>
<%@ page import="ge.eathub.models.Meal" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>
<meta charset="utf-8" >

    <head>
        <title>Restaurant</title>

    </head>
    <body>
    <h1>გამარჯობა</h1>
        <h1>Welcome to the Restaurant Page</h1>

        <h4>Filter</h4>
        <form action="<c:url value="/restaurants?filter"/>" method="post">

            <label> Filter:
                <select name="filter_option" id="filter_option">
                    <option value="with_restaurants">with restaurants</option>
                    <option value="only_meals">only meals</option>
                </select>
            </label><br>

            <label> Meal name:
                <input type='text' placeholder='meal_name:' name='filter_meal_name' required/>
            </label>
            <br>

            <label>Restaurant ID:
                <input type='number' placeholder='0:' name='filter_restaurant_id'/>
            </label>
            <br>

            <input type='submit' value='submit'/>
        </form>

        <% String error = (String) request.getAttribute(RestaurantsServlet.ERROR_ATTR);
            if (error != null) {%>
        <label><%=error%>
        </label>
        <% }%>




        <% Map<Restaurant, List<Meal>> map =  (Map<Restaurant, List<Meal>>) request.getAttribute(RestaurantsServlet.SUCCESS_RESTAURANT_ATTR);
            if (map != null) {
            for (Restaurant restaurant : map.keySet()){%>
            <label><%="-----Restaurant name: " + restaurant.getRestaurantName()%></label><br>
            <%
                for (Meal meal : map.get(restaurant)){%>
            <label><%="* Meal name: '" + meal.getMealName() + "', Meal price: " +meal.getMealPrice() %></label><br>
            <%}%>
            <%}%>
        <% } %>


        <%  List<Meal> meals =  (List<Meal>) request.getAttribute(RestaurantsServlet.SUCCESS_MEALS_ATTR);
            if (meals != null) {
                for (Meal meal : meals){%>
                    <label><%="* Meal name: '" + meal.getMealName() + "', Meal price: " +meal.getMealPrice() %>
                    </label><br>
                <%}%>
        <% } %>

        <div>
            <h3>Available Restaurants</h3>
            <%
                ServletContext sc = request.getServletContext();
                MySqlRestaurantDao dao = (MySqlRestaurantDao) sc.getAttribute(NameConstants.RESTAURANT_DAO);
                List<Restaurant> restaurants = dao.getAllRestaurant();
                for (Restaurant restaurant : restaurants) {%>
            <li><a href="../show-restaurant.jsp?id=<%=restaurant.getRestaurantID()%> "><%= restaurant.getRestaurantName()%>
            </li>
            <%}

            %>
        </div>


    </body>
</html>
