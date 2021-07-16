package ge.eathub.models;

import java.sql.Time;
import java.util.Objects;

public class Meal{
    public static final String TABLE = "meals";
    public static final String COLUMN_ID = "meal_id";
    public static final String COLUMN_NAME = "meal_name";
    public static final String COLUMN_COOKING_TIME = "cooking_time";
    public static final String COLUMN_RESTAURANT_ID = "restaurant_id";

    private Long mealID; // auto increment
    private String mealName;
    private Time cookingTime;
    private Long restaurantID;

    public Meal(){}

    public Meal(Long mealID, String mealName, Time cookingTime, Long restaurantID) {
        this.mealID = mealID;
        this.mealName = mealName;
        this.cookingTime = cookingTime;
        this.restaurantID = restaurantID;
    }

    public Meal(Meal meal){
        this.mealID = meal.getMealID();
        this.mealName = meal.getMealName();
        this.cookingTime = meal.getCookingTime();
        this.restaurantID = meal.getRestaurantID();
    }

    public Long getMealID() {
        return mealID;
    }

    public Meal setMealID(Long mealID) {
        this.mealID = mealID;
        return this;
    }

    public String getMealName() {
        return mealName;
    }

    public Meal setMealName(String mealName) {
        this.mealID = mealID;
        return this;
    }

    public Time getCookingTime() {
        return cookingTime;
    }

    public Meal setCookingTime(Time cookingTime) {
        this.mealID = mealID;
        return this;
    }

    public Long getRestaurantID() {
        return restaurantID;
    }

    public Meal setRestaurantID(Long restaurantID) {
        this.mealID = mealID;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return mealID.equals(meal.mealID) && mealName.equals(meal.mealName) && cookingTime.equals(meal.cookingTime) && restaurantID.equals(meal.restaurantID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealID, mealName, cookingTime, restaurantID);
    }
}
