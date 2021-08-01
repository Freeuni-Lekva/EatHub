package ge.eathub.dao;

import ge.eathub.models.Meal;

import java.util.List;
import java.util.Optional;

public interface MealDao {
    List<Meal> getAllMeals();

    Optional<Meal> getMealById(Long mealID);

    Meal createMeal(Meal meal);

    boolean updateMeal(Meal meal, Long restaurant_id);
}
