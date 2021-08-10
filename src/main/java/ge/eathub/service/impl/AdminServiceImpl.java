package ge.eathub.service.impl;

import ge.eathub.dao.MealDao;
import ge.eathub.dao.RestaurantDao;
import ge.eathub.exceptions.MealCreationException;
import ge.eathub.exceptions.MealUpdateException;
import ge.eathub.exceptions.RestaurantCreationException;
import ge.eathub.exceptions.RestaurantUpdateException;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;
import ge.eathub.service.AdminService;
import ge.eathub.utils.MealValidator;
import ge.eathub.utils.RestaurantValidator;

import java.util.List;
import java.util.logging.Logger;

public class AdminServiceImpl implements AdminService {

    private final static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final RestaurantDao restaurantDao;
    private final MealDao mealDao;

    public AdminServiceImpl(RestaurantDao restaurantDao, MealDao mealDao) {
        this.restaurantDao = restaurantDao;
        this.mealDao = mealDao;
    }

    @Override
    public boolean addMeal(Meal meal) {
        if (!MealValidator.validate(meal)) {
            throw new MealCreationException("The given data is incorrect");
        }
        if (restaurantDao.getRestaurantById(meal.getRestaurantID()).isEmpty()) {
            throw new MealCreationException("Restaurant id is not in database");
        }
        mealDao.createMeal(meal);
        return true;
    }

    @Override
    public boolean updateMeal(long restaurantID, Meal meal) {
        if (!MealValidator.validate(meal)) {
            throw new MealUpdateException("The given data is incorrect (negative values are impossible)");
        }
        if (mealDao.getMealById(meal.getMealID()).isEmpty()) {
            throw new MealUpdateException("Meal with that id is not in database");
        }
        if (restaurantDao.getRestaurantById(restaurantID).isEmpty()) {
            throw new MealUpdateException("Restaurant with that id is not in database");
        }
        mealDao.updateMeal(meal, restaurantID);
        return true;
    }

    @Override
    public boolean updateRestaurant(long restaurantID, Restaurant restaurant) {
        if (!RestaurantValidator.validate(restaurant)) {
            throw new RestaurantUpdateException("The given data is incorrect (negative values are impossible)");
        }
        if (restaurantDao.getRestaurantById(restaurantID).isEmpty()) {
            throw new RestaurantUpdateException("Restaurant with that id is not in database");
        }
        restaurantDao.updateRestaurant(restaurantID, restaurant);
        return true;
    }

    @Override
    public boolean addRestaurant(Restaurant restaurant) {
        if (!RestaurantValidator.validate(restaurant)) {
            throw new RestaurantCreationException("The given data is incorrect (negative values are impossible)");
        }
        restaurantDao.createRestaurant(restaurant);
        return true;
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantDao.getAllRestaurant();
    }

    @Override
    public List<Meal> getAllMeal(Restaurant restaurant) {
        return mealDao.getAllMeals();
    }
}
