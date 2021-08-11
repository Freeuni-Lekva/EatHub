package ge.eathub.servlets;

import ge.eathub.dao.MealDao;
import ge.eathub.dao.RestaurantDao;
import ge.eathub.dto.UserDto;
import ge.eathub.exceptions.MealCreationException;
import ge.eathub.exceptions.MealUpdateException;
import ge.eathub.exceptions.RestaurantCreationException;
import ge.eathub.exceptions.RestaurantUpdateException;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;
import ge.eathub.models.Role;
import ge.eathub.service.AdminService;
import ge.eathub.service.impl.AdminServiceImpl;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.logging.Logger;

import static ge.eathub.servlets.ServletCommons.ADMIN_PAGE;


@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {

    private final static Logger logger = Logger.getLogger(LoginServlet.class.getName());
    public static final String ERROR_ATTR = "ADD/UPDATE_ERROR";
    public static final String SUCCESS_ATTR = "ADD/UPDATE_EXECUTED";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        Session session = (Session) request.getSession(false);
        if (session != null) {
            UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
            if (user != null) {
                if (user.getRole().equals(Role.ADMIN)) {
                    request.getRequestDispatcher(ADMIN_PAGE).forward(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        if (request.getQueryString().equals("meal")) {
            doMeal(request, response);
        } else if (request.getQueryString().equals("restaurant")) {
            doRestaurant(request, response);
        }
        request.getRequestDispatcher(ADMIN_PAGE).forward(request, response);
    }


    private void doRestaurant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String restaurantID = request.getParameter("restaurant_id");
        String restaurant_name = request.getParameter("restaurant_name");
        String location = request.getParameter("location");
        long limit = Long.parseLong(request.getParameter("limit"));
        BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(request.getParameter("balance")));
        BigDecimal rating = BigDecimal.valueOf(Double.parseDouble(request.getParameter("rating")));
        String option = request.getParameter("option");
        RestaurantDao restaurantDao = (RestaurantDao) getServletContext().getAttribute(NameConstants.RESTAURANT_DAO);
        MealDao mealDao = (MealDao) getServletContext().getAttribute(NameConstants.MEAL_DAO);
        AdminService adminService = new AdminServiceImpl(restaurantDao, mealDao);
        if (option.equals("update")) {
            long restID = Long.parseLong(restaurantID);
            try {
                adminService.updateRestaurant(restID, new Restaurant(restaurant_name, location, limit, rating, balance));
                request.setAttribute(SUCCESS_ATTR, "Restaurant: " + restaurant_name + " updated successfully");
            } catch (RestaurantUpdateException ex) {
                request.setAttribute(ERROR_ATTR, ex.getMessage());
            }
        } else if (option.equals("add")) {
            try {
                adminService.addRestaurant(new Restaurant(restaurant_name, location, limit, rating, balance));
                request.setAttribute(SUCCESS_ATTR, "Restaurant: " + restaurant_name + " created successfully");
            } catch (RestaurantCreationException ex) {
                request.setAttribute(ERROR_ATTR, ex.getMessage());
            }
        }
    }

    private void doMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String option = request.getParameter("meal_option");
        String name = request.getParameter("meal_name");
        double mealPrice = Double.parseDouble(request.getParameter("meal_price"));
        long time = Long.parseLong(request.getParameter("cooking_time"));
        Long restaurantID = Long.parseLong(request.getParameter("meal_restaurant_id"));
        String mealID = request.getParameter("meal_id");
        RestaurantDao restaurantDao = (RestaurantDao) getServletContext().getAttribute(NameConstants.RESTAURANT_DAO);
        MealDao mealDao = (MealDao) getServletContext().getAttribute(NameConstants.MEAL_DAO);
        AdminService adminService = new AdminServiceImpl(restaurantDao, mealDao);
        if (option.equals("update")) {
            long meal_ID = Long.parseLong(mealID);
            try {
                if (adminService.updateMeal(restaurantID, new Meal(meal_ID, name, new BigDecimal(mealPrice), new Time(time), restaurantID))) {
                    request.setAttribute(SUCCESS_ATTR, "Meal: " + name + " updated successfully");
                }
            } catch (MealUpdateException ex) {
                request.setAttribute(ERROR_ATTR, ex.getMessage());
            }
        } else if (option.equals("add")) {
            try {
                adminService.addMeal(new Meal(name, new BigDecimal(mealPrice), new Time(time), restaurantID));
                request.setAttribute(SUCCESS_ATTR, "Meal: " + name + " added successfully");
            } catch (MealCreationException ex) {
                request.setAttribute(ERROR_ATTR, ex.getMessage());
            }
        }
    }


}
