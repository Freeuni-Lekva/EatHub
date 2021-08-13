package ge.eathub.models;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Objects;

public class Meal {
    public static final String TABLE = "meals";
    public static final String COLUMN_ID = "meal_id";
    public static final String COLUMN_NAME = "meal_name";
    public static final String COLUMN_PRICE = "meal_price";
    public static final String COLUMN_COOKING_TIME = "cooking_time";
    public static final String COLUMN_RESTAURANT_ID = "restaurant_id";
    public static final String COLUMN_URL = "url";

    private Long mealID; // auto increment
    private String mealName;
    private BigDecimal mealPrice;
    private Time cookingTime;
    private Long restaurantID;
    private String url;



    public Meal() {
    }

    public Meal(Long mealID, String mealName, BigDecimal mealPrice, Time cookingTime, String url) {
        this.mealID = mealID;
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.cookingTime = cookingTime;
        this.url = url;
    }

    public Meal(String mealName, BigDecimal mealPrice, Time cookingTime, Long restaurantID, String url) {
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.cookingTime = cookingTime;
        this.restaurantID = restaurantID;
        this.url = url;
    }

    public Meal(Long mealID, String mealName, BigDecimal mealPrice, Time cookingTime, Long restaurantID, String url) {
        this.mealID = mealID;
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.cookingTime = cookingTime;
        this.restaurantID = restaurantID;
        this.url = url;
    }

    public Meal(Meal meal) {
        this.mealID = meal.getMealID();
        this.mealName = meal.getMealName();
        this.mealPrice = meal.getMealPrice();
        this.cookingTime = meal.getCookingTime();
        this.restaurantID = meal.getRestaurantID();
        this.url = meal.getMealUrl();
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
        this.mealName = mealName;
        return this;
    }

    public Time getCookingTime() {
        return cookingTime;
    }

    public Meal setCookingTime(Time cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public Long getRestaurantID() {
        return restaurantID;
    }

    public Meal setRestaurantID(Long restaurantID) {
        this.restaurantID = restaurantID;
        return this;
    }

    public BigDecimal getMealPrice() {
        return mealPrice;
    }

    public Meal setMealPrice(BigDecimal mealPrice) {
        this.mealPrice = mealPrice;
        return this;
    }

    public Meal setMealUrl(String url){
        this.url = url;
        return this;
    }

    public String getMealUrl(){
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return mealID.equals(meal.mealID) && mealName.equals(meal.mealName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealID, mealName);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "mealID=" + mealID +
                ", mealName='" + mealName + '\'' +
                ", mealPrice=" + mealPrice +
                ", cookingTime=" + cookingTime +
                ", restaurantID=" + restaurantID +
                '}';
    }
}
