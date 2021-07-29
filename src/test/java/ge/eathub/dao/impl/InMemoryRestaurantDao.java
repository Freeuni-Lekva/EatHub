package ge.eathub.dao.impl;

import ge.eathub.dao.RestaurantDao;
import ge.eathub.exceptions.RestauranCreationException;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryRestaurantDao implements RestaurantDao {

    private List<Restaurant> restaurants;
    private List<Meal> meals;
    AtomicLong count = new AtomicLong(0);

    public InMemoryRestaurantDao(){
        restaurants = new ArrayList<>();
        meals = new ArrayList<>();
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurants;
    }

    @Override
    public Optional<Restaurant> getRestaurantById(Long restaurantID) {
        return restaurants.stream().filter(x -> x.getRestaurantID().equals(restaurantID)).findAny();
    }

    @Override
    public List<Meal> getAllMeals(Long restaurantID) {
        return meals.stream().filter(x -> x.getRestaurantID().equals(restaurantID)).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getMealsBySubName(String mealName, Long restaurantID) {
        return meals.stream()
                .filter(x -> x.getMealName().contains(mealName) && x.getRestaurantID().equals(restaurantID))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Restaurant, List<Meal>> getRestaurantsByMeal(String mealName) {
        return restaurants.stream()
                .map(x -> Map.of(x, getMealsBySubName(mealName, x.getRestaurantID())))
                .reduce(null, (x,y) -> {
                    if (x == null) return y;
                    x.putAll(y);
                    return x;
                });
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        if (restaurants.stream().noneMatch(u ->
                u.getRestaurantID().equals(restaurant.getRestaurantID()))) {
            Restaurant newRestaurant = new Restaurant(restaurant).setRestaurantID(count.getAndIncrement());
            restaurants.add(newRestaurant);
            return newRestaurant;
        }
        throw new RestauranCreationException(restaurant.getRestaurantName());
    }
}
