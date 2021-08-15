package ge.eathub.dao;

import ge.eathub.dto.OrderDto;
import ge.eathub.models.Meal;
import ge.eathub.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order createOrder(Long userID, Long mealID, Long roomID, Integer quantity);

    Order createOrder(Order order);

    List<Meal> userMeals(Long userID, Long roomID);

    List<Meal> roomMeals(Long roomID);

    // Order updateOrder(Order order);

    Optional<Order> updateOrder(Order order, Integer quantity);

    boolean removeOrder(Order order);

    Optional<Order> getOrderByID(Long userID, Long roomID, Long mealID);

    List<Order> getAll(Long userID, Long roomID);

    List<OrderDto> getChosenMealsByRoomID(Long roomID);

    List<OrderDto> getChosenMealsByIDs(Long userID, Long roomID);
    }
