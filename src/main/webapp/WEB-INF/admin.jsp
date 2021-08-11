<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page import="ge.eathub.servlets.LoginServlet" %>
<%@ page import="ge.eathub.servlets.AdminServlet" %>
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

        <label> Operation:
            <select name="option" id="admin_option">
                <option value="add">add</option>
                <option value="update">update</option>
            </select>
        </label><br>

        <label id="label_restID">
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


    <h4>Update or add new meal</h4>
    <form action="<c:url value="/admin?meal"/>" method="post">

        <label> Operation:
            <select name="meal_option" id="meal_option">
                <option value="add">add</option>
                <option value="update">update</option>
            </select>
        </label><br>

        <label id="admin_meal_ID">
        </label>

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


        <label >Restaurant ID:
            <input type='number' min="0" placeholder='0:' name='meal_restaurant_id' required/>
        </label>
        <br>

        <input type='submit' value='submit'/>
    </form>
</div>


<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script type="text/javascript">
    function updateRestaurants() {
        var e = document.getElementById("admin_option");
        if ("update" === e.options[e.selectedIndex].value) {
            document.getElementById("label_restID").innerHTML = "Restaurant ID: <input type='number' min='0' placeholder='0:' name='restaurant_id' required/> <br>";
        } else if ("add" === e.options[e.selectedIndex].value) {
            document.getElementById("label_restID").innerHTML = "";
        }
    }

    document.getElementById("admin_option").addEventListener("click", updateRestaurants);


    function updateMeals() {
        var e = document.getElementById("meal_option");
        if ("update" === e.options[e.selectedIndex].value) {
            document.getElementById("admin_meal_ID").innerHTML = "Meal ID: <input type='number' min='0' placeholder='0:' name='meal_id' required/> <br>";
        } else if ("add" === e.options[e.selectedIndex].value) {
            document.getElementById("admin_meal_ID").innerHTML = "";
        }
    }

    document.getElementById("meal_option").addEventListener("click", updateMeals);


</script>


</body>
</html>
