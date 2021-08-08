<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.dto.UserDto" %>
<%@ page import="ge.eathub.servlets.LoginServlet" %>
<%@ page import="ge.eathub.servlets.AdminServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
      <select name="option" id="option">
        <option value="add">add</option>
        <option value="update">update</option>
      </select>
    </label><br>

    <label>Restaurant ID:
      <input type='number' placeholder='0:' name='restaurant_id'/>
    </label>
    <br>
    <label> Restaurant name:
      <input type='text' placeholder='restaurant_name:' name='restaurant_name' required/>
    </label>
    <br>
    <label> Location:
      <input type='text' placeholder='location:' name='location' required/>
    </label>
    <br>
    <label>Limit:
      <input type='number' placeholder='0:' name='limit' required/>
    </label>
    <br>

    <label>Rating:
      <input type='number' step="0.01" placeholder='0:' name='rating' required/>
    </label>
    <br>


    <label>Balance:
      <input type='number' step="0.1" placeholder='0:' name='balance' required/>
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

    <label>Meal ID:
      <input type='number' placeholder='0:' name='meal_id'/>
    </label>
    <br>
    <label> Meal name:
      <input type='text' placeholder='meal_name:' name='meal_name' required/>
    </label>
    <br>

    <label>Meal price:
      <input type='number' step="0.5" placeholder='0:' name='meal_price' required/>
    </label>
    <br>

    <label>Cooking time:
      <input type='number' placeholder='0:' name='cooking_time' required/>
    </label>
    <br>


    <label>Restaurant ID:
      <input type='number' placeholder='0:' name='meal_restaurant_id' required/>
    </label>
    <br>

    <input type='submit' value='submit'/>
  </form>



</div>

</body>
</html>
