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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <title> Admin </title>
</head>
<body>
<div>


    <% String error = (String) request.getAttribute(AdminServlet.ERROR_ATTR);
        if (error != null) {%>
    <label><%=error%>
    </label>
    <% }%>


    <% String success = (String) request.getAttribute(AdminServlet.SUCCESS_ATTR);
        if (success != null) {%>
    <label><%=success%>
    </label>
    <% }%>

    <h4>Update or add new restaurant</h4>
    <form action="<c:url value="/admin?restaurant"/>" method="post">

        <label> Restaurants:
            <select name="admin_option" id="admin_option">
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
            </select><br>
        </label>

        <label> Restaurant name:
            <input type='text' placeholder='restaurant_name:' name='restaurant_name' required/>
        </label>
        <br>

        <label> Location:
            <input type='text' placeholder='location:' name='location' required/>
        </label>
        <br>
        <label>Limit:
            <input type='number' min="0" placeholder='0:' name='limit' required/>
        </label>
        <br>

        <label>Rating:
            <input type='number' min="0" step="0.01" placeholder='0:' name='rating' required/>
        </label>
        <br>


        <label>Balance:
            <input type='number' min="0" step="0.1" placeholder='0:' name='balance' required/>
        </label>
        <br>

        <input type='submit' value='submit'/>
    </form>


    <h4>Update a Meal</h4>
    <form action="<c:url value="/admin?update_meal"/>" method="post">

        <label> Operation:
            <select name="meal_option" id="meal_option">
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
        </label><br>


        <label> Meal name:
            <input type='text' placeholder='meal_name:' name='meal_name' required/>
        </label>
        <br>

        <label>Meal price:
            <input type='number' min="0" step="0.5" placeholder='0:' name='meal_price' required/>
        </label>
        <br>

        <label>Cooking time:
            <input type='number' min="0" placeholder='0:' name='cooking_time' required/>
        </label>
        <br>

        <input type='submit' value='submit'/>
    </form>


    <h4>Add a new Meal</h4>
    <form action="<c:url value="/admin?add_meal"/>" method="post">

        <label> Meal name:
            <input type='text' placeholder='add_meal_name:' name='add_meal_name' required/>
        </label>
        <br>

        <label>Meal price:
            <input type='number' min="0" step="0.5" placeholder='0:' name='add_meal_price' required/>
        </label>
        <br>

        <label>Cooking time:
            <input type='number' min="0" placeholder='0:' name='add_cooking_time' required/>
        </label>
        <br>

        <label> Restaurants:
            <select name="meal_admin_option" id="meal_admin_option">
                <%
                    for (Restaurant restaurant : restaurants) {%>
                <option value="<%=restaurant.getRestaurantID()%>"><%=restaurant.getRestaurantName()%>
                </option>
                <%
                    }

                %>
            </select><br>
        </label>

        <input type='submit' value='submit'/>
    </form>


</div>


</body>
</html>
