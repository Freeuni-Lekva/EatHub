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

</head>
<body>
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
        <input type='text' placeholder='meal_name:' name='filter_meal_name' id="meal_name" required/>
    </label>
    <br>

    <label id="restID">
    </label>

    <input type='submit' value='submit'/>
</form>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script type="text/javascript">
    function listQ() {
        var e = document.getElementById("filter_option");
        if ("only_meals" === e.options[e.selectedIndex].value) {
            document.getElementById("restID").innerHTML = "Restaurant ID: <input type='number' min='0' placeholder='0:' name='filter_restaurant_id' required/> <br>";
        } else if ("with_restaurants" === e.options[e.selectedIndex].value) {
            document.getElementById("restID").innerHTML = "";
        }
    }

    document.getElementById("filter_option").addEventListener("click", listQ);

    // $(document).ready(function () {
    //     $('#meal_name').on("change", function () {
    //         $.ajax({
    //             type:"POST",
    //             url:"restaurants?filter",
    //             data:{ meal_name: $("#meal_name").text()},
    //             success:function(data){
    //                 $("#meal_name").html(data);
    //             }
    //         });
    //     })
    // });


</script>


    <% String error = (String) request.getAttribute(RestaurantsServlet.ERROR_ATTR);
    if (error != null) {%>
<label><%=error%>
</label>
    <% }%>


    <% Map<Restaurant, List<Meal>> map = (Map<Restaurant, List<Meal>>) request.getAttribute(RestaurantsServlet.SUCCESS_RESTAURANT_ATTR);
    if (map != null) {
        for (Restaurant restaurant : map.keySet()) {%>
<label><%="-----Restaurant name: " + restaurant.getRestaurantName()%>
</label><br>
    <%
    for (Meal meal : map.get(restaurant)) {%>
<label><%="* Meal name: '" + meal.getMealName() + "', Meal price: " + meal.getMealPrice() %>
</label><br>
    <%}%>
    <%}%>
    <% } %>


    <% List<Meal> meals = (List<Meal>) request.getAttribute(RestaurantsServlet.SUCCESS_MEALS_ATTR);
    if (meals != null) {
        for (Meal meal : meals) {%>
<label><%="* Meal name: '" + meal.getMealName() + "', Meal price: " + meal.getMealPrice() %>
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
    <li><a href="/newRoom?id=<%=restaurant.getRestaurantID()%> "><%= restaurant.getRestaurantName()%>
    </li>
    <%
        }

    %>
</div>


<body></body>
</html>