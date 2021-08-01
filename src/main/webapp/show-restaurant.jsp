<%@ page import="ge.eathub.dao.impl.MySqlRestaurantDao" %>
<%@ page import="ge.eathub.listener.NameConstants" %>
<%@ page import="ge.eathub.models.Restaurant" %>
<%@ page import="java.util.List" %>
<%@ page import="ge.eathub.models.Meal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
  </head>
  <body>
    <%
      ServletContext sc = request.getServletContext();
      MySqlRestaurantDao dao = (MySqlRestaurantDao) sc.getAttribute(NameConstants.RESTAURANT_DAO);
      String id = request.getQueryString().substring(3);
      long restaurantID = Long.parseLong(id);
      List<Meal> meals = dao.getAllMeals(restaurantID);
      for (Meal meal : meals) {%>
        <li> <%="Meal Name: " + meal.getMealName() + " Price: " + meal.getMealPrice()%>
      </li>
    <%}
    %>
    <title><%=dao.getRestaurantById(restaurantID).get().getRestaurantName()%></title>
  </body>
</html>
