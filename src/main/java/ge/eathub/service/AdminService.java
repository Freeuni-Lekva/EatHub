package ge.eathub.service;

import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import java.util.List;

public interface AdminService {
    boolean addMeal(Meal meal);

    boolean updateMeal(long restaurantID, Meal meal);

    boolean updateRestaurant(long restaurantID, Restaurant restaurant);

    boolean addRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurant();

    List<Meal> getAllMeal(Restaurant restaurant);
}
