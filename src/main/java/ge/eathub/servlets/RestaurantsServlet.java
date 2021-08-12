package ge.eathub.servlets;

import ge.eathub.dao.RestaurantDao;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "RestaurantsServlet", value = "/restaurants")
public class RestaurantsServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(RegistrationServlet.class.getName());
    private static final String RESTAURANTS_PAGE = "/WEB-INF/restaurants.jsp";
    public static final String ERROR_ATTR = "FILTER_ERROR";
    public static final String SUCCESS_RESTAURANT_ATTR = "FILTER RESTAURANT EXECUTED";
    public static final String SUCCESS_MEALS_ATTR = "FILTER MEAL EXECUTED";
    public static final String RESTAURANT_NAME_ATTR = "RESTAURANT_NAME_ATTR";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("restaurants get");
        ServletCommons.setEncoding(request, response);
        request.getRequestDispatcher(RESTAURANTS_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        String option = request.getQueryString();
        if (option.equals("filter")) {
            doFilter(request, response);
        }
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("filter_meal_name").trim();
        String option = request.getParameter("filter_option");

        RestaurantDao restaurantDao = (RestaurantDao) getServletContext().getAttribute(NameConstants.RESTAURANT_DAO);
        long value = Long.parseLong(option);
        if (value == 0) {
            HashMap<Restaurant, List<Meal>> map = (HashMap<Restaurant, List<Meal>>) restaurantDao.getRestaurantsByMeal(name);
            if (map.isEmpty()) {
                request.setAttribute(ERROR_ATTR, "No meal found in any restaurant on name:" + name);
            } else {
                request.setAttribute(RESTAURANT_NAME_ATTR, "Meals founded in all restaurants");
                request.setAttribute(SUCCESS_RESTAURANT_ATTR, map);
            }
        } else if (value > 0) {
            List<Meal> meals = restaurantDao.getMealsBySubName(name, value);
            if (meals.isEmpty())
                request.setAttribute(ERROR_ATTR, "No meal found on name: '" + name + "' in a restaurant with name: " + restaurantDao.getRestaurantById(value).get().getRestaurantName());
            else {
                request.setAttribute(RESTAURANT_NAME_ATTR, "Restaurant: " + restaurantDao.getRestaurantById(value).get().getRestaurantName());
                request.setAttribute(SUCCESS_MEALS_ATTR, meals);
            }
        }
        request.getRequestDispatcher(RESTAURANTS_PAGE).forward(request, response);
    }

}
