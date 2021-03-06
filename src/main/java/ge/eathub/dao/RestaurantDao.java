package ge.eathub.dao;

import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RestaurantDao {
    List<Restaurant> getAllRestaurant();

    Optional<Restaurant> getRestaurantById(Long restaurantID);

    List<Meal> getAllMeals(Long restaurantID);

    List<Meal> getMealsBySubName(String mealName, Long restaurantID);

    Map<Restaurant, List<Meal>> getRestaurantsByMeal(String mealName);

    boolean updateRestaurant(long restaurantID, Restaurant restaurant);

    Restaurant createRestaurant(Restaurant restaurant);
}
