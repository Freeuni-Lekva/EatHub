package ge.eathub.dao;

import ge.eathub.models.Meal;
import ge.eathub.models.Order;

import java.util.List;

public interface OrderDao {
    Order createOrder(Long userID, Long mealID, Long roomID, Integer quantity);

    Order createOrder(Order order);

    List<Meal> userMeals(Long userID, Long roomID);

    List<Meal> roomMeals(Long roomID);

    Order updateOrder(Order order);
    // მინუსებში არ წავიდეს, 0-ს რომ ჩამოცდება არ გამოგრჩეს
    Order updateOrder(Long orderID, Integer quantity);
}
