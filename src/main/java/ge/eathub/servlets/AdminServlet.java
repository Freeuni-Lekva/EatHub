package ge.eathub.servlets;

import ge.eathub.dao.MealDao;
import ge.eathub.dao.RestaurantDao;
import ge.eathub.dto.UserDto;
import ge.eathub.exceptions.*;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;
import ge.eathub.models.Role;
import ge.eathub.service.AdminService;
import ge.eathub.service.impl.AdminServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Time;
import java.util.Optional;
import java.util.logging.Logger;

import static ge.eathub.servlets.ServletCommons.ADMIN_PAGE;

@MultipartConfig
@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {

    private final static Logger logger = Logger.getLogger(LoginServlet.class.getName());
    public static final String ERROR_ATTR = "ADD/UPDATE_ERROR";
    public static final String SUCCESS_ATTR = "ADD/UPDATE_EXECUTED";
    public static final String RESTAURANT_PHOTOS = "src/main/webapp/images/Restaurants";
    public static final String MEAL_PHOTOS = "src/main/webapp/images/Meals";


    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
            if (user != null) {
                if (user.getRole().equals(Role.ADMIN)) {
                    return true;
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return false;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (checkAdmin(request, response)) {
            request.getRequestDispatcher(ADMIN_PAGE).forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        if (checkAdmin(request, response)) {
            String query = request.getQueryString();
            if (query.equals("update_meal")) {
                updateMeal(request, response);
            } else if (query.equals("add_meal")) {
                addMeal(request, response);
            } else if (request.getQueryString().equals("restaurant")) {
                doRestaurant(request, response);
            }
            request.getRequestDispatcher(ADMIN_PAGE).forward(request, response);
        }
    }

    private void addMeal(HttpServletRequest request, HttpServletResponse response) {
        String mealName = request.getParameter("add_meal_name");
        double mealPrice = Double.parseDouble(request.getParameter("add_meal_price"));
        long time = Long.parseLong(request.getParameter("add_cooking_time"));
        RestaurantDao restaurantDao = (RestaurantDao) getServletContext().getAttribute(NameConstants.RESTAURANT_DAO);
        MealDao mealDao = (MealDao) getServletContext().getAttribute(NameConstants.MEAL_DAO);
        AdminService adminService = new AdminServiceImpl(restaurantDao, mealDao);
        long restaurantID = Long.parseLong(request.getParameter("meal_admin_option"));
        try {
            Part filePart = request.getPart("file-image-add");
            File uploads = new File(MEAL_PHOTOS);
            long ID = mealDao.getAllMeals().size() + 1;
            String newFileName = "" + ID + ".jpg";
            File file = new File(uploads, newFileName);
            if (adminService.addMeal(new Meal(mealName, new BigDecimal(mealPrice), new Time(time), restaurantID, newFileName))) {
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                request.setAttribute(SUCCESS_ATTR, "Meal: '" + mealName + "' added successfully in restaurant: " + restaurantDao.getRestaurantById(restaurantID).get().getRestaurantName());
            }
        } catch (MealCreationException | MealAlreadyExistsException ex) {
            request.setAttribute(ERROR_ATTR, ex.getMessage());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long ID = Long.parseLong(request.getParameter("meal_option"));
        String name = request.getParameter("meal_name");
        double mealPrice = Double.parseDouble(request.getParameter("meal_price"));
        long time = Long.parseLong(request.getParameter("cooking_time"));
        RestaurantDao restaurantDao = (RestaurantDao) getServletContext().getAttribute(NameConstants.RESTAURANT_DAO);
        MealDao mealDao = (MealDao) getServletContext().getAttribute(NameConstants.MEAL_DAO);
        AdminService adminService = new AdminServiceImpl(restaurantDao, mealDao);
        String mealNameBeforeUpdate;
        String restaurantName;
        Optional<Meal> meal = mealDao.getMealById(ID);
        if (meal.isPresent()) {
            mealNameBeforeUpdate = meal.get().getMealName();
        } else {
            request.setAttribute(ERROR_ATTR, "Unable to update meal");
            return;
        }
        Optional<Restaurant> restaurant = restaurantDao.getRestaurantById(meal.get().getRestaurantID());
        if (restaurant.isPresent()) {
            restaurantName = restaurant.get().getRestaurantName();
        } else {
            request.setAttribute(ERROR_ATTR, "Unable to update meal");
            return;
        }
        try {
            Part filePart = request.getPart("file-image-update");
            File uploads = new File(MEAL_PHOTOS);
            String newFileName = "" + ID + ".jpg";
            File file = new File(uploads, newFileName);
            if (adminService.updateMeal(new Meal(ID, name, new BigDecimal(mealPrice), new Time(time), newFileName))) {
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                request.setAttribute(SUCCESS_ATTR, "Meal: '" + mealNameBeforeUpdate + "' updated to '" + name + "' in restaurant name: " + restaurantName);
            }
        } catch (MealUpdateException | MealAlreadyExistsException ex) {
            request.setAttribute(ERROR_ATTR, ex.getMessage());
        }
    }


    private void updateRestaurant(HttpServletRequest request, long option, RestaurantDao restaurantDao, AdminService adminService,
                                  String restaurant_name, String location, Long limit, BigDecimal rating, BigDecimal balance) throws ServletException {
        try {
            Part filePart = request.getPart("restaurant-image");
            File uploads = new File(RESTAURANT_PHOTOS);
            String newFileName = "" + option + ".jpg";
            File file = new File(uploads, newFileName);
            String restaurantNameBefore;
            Optional<Restaurant> restaurant = restaurantDao.getRestaurantById(option);
            if (restaurant.isPresent()) {
                restaurantNameBefore = restaurant.get().getRestaurantName();
            } else {
                request.setAttribute(ERROR_ATTR, "Unable to add restaurant");
                return;
            }
            adminService.updateRestaurant(option, new Restaurant(restaurant_name, location, limit, rating, balance, newFileName));
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            request.setAttribute(SUCCESS_ATTR, "Restaurant: '" + restaurantNameBefore + "' updated to '" + restaurant_name + "' successfully");
        } catch (RestaurantUpdateException ex) {
            request.setAttribute(ERROR_ATTR, ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRestaurant(HttpServletRequest request, RestaurantDao restaurantDao, AdminService adminService, String restaurant_name, String location,
                               long limit, BigDecimal rating, BigDecimal balance) throws ServletException {
        try {
            Part filePart = request.getPart("restaurant-image");
            File uploads = new File(RESTAURANT_PHOTOS);
            long id = restaurantDao.getAllRestaurant().size() + 1;
            String newFileName = "" + id + ".jpg";
            File file = new File(uploads, newFileName);
            adminService.addRestaurant(new Restaurant(restaurant_name, location, limit, rating, balance, newFileName));
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            request.setAttribute(SUCCESS_ATTR, "Restaurant: " + restaurant_name + " created successfully");
        } catch (RestaurantCreationException | IOException | ServletException ex) {
            request.setAttribute(ERROR_ATTR, ex.getMessage());
        }
    }

    private void doRestaurant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long option = Long.parseLong(request.getParameter("admin_option"));
        String restaurant_name = request.getParameter("restaurant_name");
        String location = request.getParameter("location");
        long limit = Long.parseLong(request.getParameter("limit"));
        BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(request.getParameter("balance")));
        BigDecimal rating = BigDecimal.valueOf(Double.parseDouble(request.getParameter("rating")));
        RestaurantDao restaurantDao = (RestaurantDao) getServletContext().getAttribute(NameConstants.RESTAURANT_DAO);
        MealDao mealDao = (MealDao) getServletContext().getAttribute(NameConstants.MEAL_DAO);
        AdminService adminService = new AdminServiceImpl(restaurantDao, mealDao);
        if (option == 0) {
            addRestaurant(request, restaurantDao, adminService, restaurant_name, location, limit, rating, balance);
        } else if (option > 0) {
            updateRestaurant(request, option, restaurantDao, adminService, restaurant_name, location, limit, rating, balance);
        }
    }


}
