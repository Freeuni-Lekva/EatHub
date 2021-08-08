package ge.eathub.servlets;

import ge.eathub.dao.MealDao;
import ge.eathub.dao.RestaurantDao;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
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



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("restaurants get");
        request.getRequestDispatcher(RESTAURANTS_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc = getServletContext();
        String option = request.getQueryString();
        if (option.equals("filter")){
            doFilter(request, response);
        }
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String name = request.getParameter("filter_meal_name");
        String option = request.getParameter("filter_option");
        RestaurantDao restaurantDao = (RestaurantDao) getServletContext().getAttribute(NameConstants.RESTAURANT_DAO);
        if (option.equals("with_restaurants")){
            HashMap<Restaurant, List<Meal>> map = (HashMap<Restaurant, List<Meal>>) restaurantDao.getRestaurantsByMeal(name);
            if (map.isEmpty()){
                request.setAttribute(ERROR_ATTR, "No meal found on that name:"  + name);
            }else {
                request.setAttribute(SUCCESS_RESTAURANT_ATTR,  map);
            }
        }else if(option.equals("only_meals")){
            if (request.getParameter("filter_restaurant_id").isEmpty()){
                request.setAttribute(ERROR_ATTR, "Missing restaurant ID");
            }else{
                long id = Long.parseLong(request.getParameter("filter_restaurant_id"));
                List<Meal> meals = restaurantDao.getMealsBySubName(name, id);
                if (meals.isEmpty()) request.setAttribute(ERROR_ATTR, "No meal found on that name: '"  + name + "' in a restaurant with ID: " + id);
                else request.setAttribute(SUCCESS_MEALS_ATTR,meals);
            }
        }
        request.getRequestDispatcher(RESTAURANTS_PAGE).forward(request, response);
    }


}
