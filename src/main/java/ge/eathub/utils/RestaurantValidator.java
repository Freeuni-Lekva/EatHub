package ge.eathub.utils;

import ge.eathub.models.Restaurant;

import java.math.BigDecimal;

public class RestaurantValidator {
    public static boolean validate(Restaurant restaurant){
        return restaurant.getBalance().doubleValue() >= 0 && restaurant.getLimit() >= 0
            && restaurant.getRating().doubleValue() >= 0;
    }
}
