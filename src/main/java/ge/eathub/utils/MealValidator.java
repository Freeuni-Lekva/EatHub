package ge.eathub.utils;

import ge.eathub.models.Meal;

public class MealValidator {
    public static boolean validate(Meal meal) {
        return meal.getMealPrice().doubleValue() > 0 && meal.getCookingTime().getTime() > 0;
    }
}
