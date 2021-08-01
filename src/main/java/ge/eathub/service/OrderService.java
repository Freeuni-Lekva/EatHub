package ge.eathub.service;

import ge.eathub.models.Order;

public interface OrderService {
    Order addOrder(Long userID, Long mealID, Long roomID, Integer quantity);

    Order updateOrder(Order order);

    void removeOrder(Order order);
}
