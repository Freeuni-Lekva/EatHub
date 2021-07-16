package ge.eathub.models;

import java.sql.Time;
import java.util.Objects;

public class Restaurant {
    public static final String TABLE = "Restaurants";
    public static final String COLUMN_ID = "restaurant_id";
    public static final String COLUMN_NAME = "restaurant_name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_LIMIT = "max_limit";
    public static final String COLUMN_RATING = "Rating";
    public static final String COLUMN_BALANCE = "Balance";

    private Long restaurantID; // autoincrement
    private String restaurantName;
    private String location;
    private Long limit;
    private Double rating;
    private Double balance;

    public Restaurant(){}

    public Restaurant(Long restaurantID, String restaurantName, String location, Long limit, Double rating, Double balance) {
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.location = location;
        this.limit = limit;
        this.rating = rating;
        this.balance = balance;
    }

    public Restaurant(Restaurant restaurant){
        this.restaurantID = restaurant.getRestaurantID();
        this.restaurantName = restaurant.getRestaurantName();
        this.location = restaurant.getLocation();
        this.limit = restaurant.getLimit();
        this.rating = restaurant.getRating();
        this.balance = restaurant.getBalance();
    }

    public Long getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Long restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Restaurant setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Restaurant setLocation(String location) {
        this.location = location;
        return this;
    }

    public Long getLimit() {
        return limit;
    }

    public Restaurant setLimit(Long limit) {
        this.limit = limit;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public Restaurant setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Double getBalance() {
        return balance;
    }

    public Restaurant setBalance(Double balance) {
        this.balance = balance;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return restaurantID.equals(that.restaurantID) && restaurantName.equals(that.restaurantName) && location.equals(that.location) && limit.equals(that.limit) && rating.equals(that.rating) && balance.equals(that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantID, restaurantName, location, limit, rating, balance);
    }
}
