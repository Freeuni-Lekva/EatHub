<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.servlets.RestaurantsServlet" %>
<%@ page import="ge.eathub.dao.impl.MySqlRestaurantDao" %>
<%@ page import="ge.eathub.models.Restaurant" %>
<%@ page import="java.util.List" %>
<%@ page import="ge.eathub.listener.NameConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Restaurant</title>

    </head>
    <body>
        <h1>HII</h1>
        <%
            ServletContext sc = request.getServletContext();
            MySqlRestaurantDao dao = (MySqlRestaurantDao) sc.getAttribute(NameConstants.RESTAURANT_DAO);
            List<Restaurant> restaurants = dao.getAllRestaurant();
            for (Restaurant restaurant : restaurants) {%>
                <li><a href="../show-restaurant.jsp?id=<%=restaurant.getRestaurantID()%> "><%= restaurant.getRestaurantName()%>
                </li>
            <%}

        %>

    </body>
</html>
