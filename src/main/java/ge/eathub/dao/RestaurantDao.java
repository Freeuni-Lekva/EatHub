package ge.eathub.dao;

import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantDao {
    List<Restaurant> getAllRestaurant();

    Optional<Restaurant> getRestaurantById(Long userID);

    List<Meal> getAllMeals(Long restaurantID);

    List<Meal> getMealsBySubName(String mealName, Long restaurantID);

    // TODO: We can have with meal and restaurant
    List<Restaurant> getRestaurantsByMeal(String mealName);

    Restaurant createRestaurant(Restaurant restaurant);
}
