package ge.eathub.service;

import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import java.util.List;

public interface RestaurantService {
    List<Restaurant> getAll();
    List<Meal> getAll(Restaurant restaurant);
    void addMeal(int restaurantID, Meal meal);
    void updateMeal(int restaurantID, Meal meal);
}
