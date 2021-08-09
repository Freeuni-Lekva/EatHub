package ge.eathub.service;

import ge.eathub.models.Order;

import java.util.Optional;

public interface OrderService {
    Optional<Order> addOrder(Long userID, Long mealID, Long roomID, Integer quantity);

    Optional<Order> updateOrder(Order order);

    boolean removeOrder(Order order);
}
