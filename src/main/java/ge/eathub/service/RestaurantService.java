package ge.eathub.service;

import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import java.util.List;

public interface RestaurantService {
    List<Restaurant> getAll();
    List<Meal> getAll(Restaurant restaurant);
    void addMeal(long restaurantID, Meal meal);
    void updateMeal(long restaurantID, Meal meal);
}
