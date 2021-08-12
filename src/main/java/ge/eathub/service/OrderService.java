package ge.eathub.service;

import ge.eathub.dto.OrderDto;
import ge.eathub.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Order> addOrder(Long userID, Long mealID, Long roomID, Integer quantity);

    Optional<Order> updateOrder(Order order);

    boolean removeOrder(Order order);

    Optional<Order> getOrderByID(Long userID, Long roomID, Long mealID);

    List<Order> getAll(Long userID, Long roomID);

    List<OrderDto> getChosenMeals(Long userID, Long roomID);
}
