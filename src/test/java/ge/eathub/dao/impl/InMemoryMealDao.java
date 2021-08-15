package ge.eathub.dao.impl;

import ge.eathub.dao.MealDao;
import ge.eathub.exceptions.MealCreationException;
import ge.eathub.models.Meal;

import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMealDao implements MealDao {

    List<Meal> meals;
    AtomicLong count = new AtomicLong(1);

    public InMemoryMealDao() {
        meals = new Vector<>();
    }

    @Override
    public List<Meal> getAllMeals() {
        return meals;
    }

    @Override
    public Optional<Meal> getMealById(Long mealID) {
        return meals.stream().filter(meal ->
                        meal.getMealID()
                                .equals(mealID))
                .findAny();
    }

    @Override
    public Meal createMeal(Meal meal) {
        if (meals.stream().noneMatch(u ->
                u.getMealID().equals(meal.getMealID()))) {
            Meal newMeal = new Meal(meal).setMealID(count.getAndIncrement());
            meals.add(newMeal);
            return newMeal;
        }
        throw new MealCreationException(meal.getMealName());
    }

    @Override
    public boolean updateMeal(Meal meal, Long restaurant_id) {
        Optional<Meal> optMeal = meals.stream().filter(m -> m.getMealID().
                equals(meal.getMealID()) &&
                m.getRestaurantID().equals(meal.getRestaurantID())).findAny();
        optMeal.ifPresent(optMeal1 -> {
            optMeal1.setMealName(meal.getMealName());
            optMeal1.setMealPrice(meal.getMealPrice());
            optMeal1.setCookingTime(meal.getCookingTime());
        });
        return optMeal.isPresent();
    }

    @Override
    public boolean updateMeal(Meal meal) {
        return false;
    }
}
