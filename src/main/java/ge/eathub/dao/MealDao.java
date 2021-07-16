package ge.eathub.dao;

import ge.eathub.models.Meal;

import java.util.List;
import java.util.Optional;

public interface MealDao {
    List<Meal> getAllMeals();

    Optional<Meal> getMealById(Long mealID);

    Meal createMeal(Meal meal);

    // TODO: CHANGE IT
    boolean updateMeal(Meal meal);
}
