package ge.eathub.service.impl;

import ge.eathub.dao.MealDao;
import ge.eathub.dao.OrderDao;
import ge.eathub.dao.RoomDao;
import ge.eathub.dao.UserDao;
import ge.eathub.models.Order;
import ge.eathub.service.OrderService;

import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private final RoomDao roomDao;
    private final UserDao userDao;
    private final OrderDao orderDao;

    public OrderServiceImpl(RoomDao roomDao, UserDao userDao, OrderDao orderDao, MealDao mealDao) {
        this.roomDao = roomDao;
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    @Override
    public Optional<Order> addOrder(Long userID, Long mealID, Long roomID, Integer quantity) {
        if (roomDao.userInRoom(roomID, userID) && roomDao.mealInRoom(mealID, roomID) && quantity >= 0) {
            return Optional.of(orderDao.createOrder(userID, mealID, roomID, quantity));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> updateOrder(Order order) {
        if (roomDao.userInRoom(order.getRoomID(), order.getUserID()) &&
                roomDao.mealInRoom(order.getMealID(), order.getRoomID()) && order.getQuantity() >= 0) {
            return orderDao.updateOrder(order, order.getQuantity());
        }
        return Optional.empty();
    }

    @Override
    public boolean removeOrder(Order order) {
        if (roomDao.userInRoom(order.getRoomID(), order.getUserID()) &&
                roomDao.mealInRoom(order.getMealID(), order.getRoomID()) && order.getQuantity() >= 0) {
            return orderDao.removeOrder(order);
        }
        return false;
    }
}
