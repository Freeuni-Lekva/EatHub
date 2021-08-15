package ge.eathub.service.impl;

import ge.eathub.dao.OrderDao;
import ge.eathub.dao.RoomDao;
import ge.eathub.dao.TransactionDao;
import ge.eathub.dto.OrderDto;
import ge.eathub.mailer.Mailer;
import ge.eathub.mailer.mails.Mail;
import ge.eathub.mailer.mails.OrderMail;
import ge.eathub.models.Restaurant;
import ge.eathub.models.User;
import ge.eathub.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public class TransactionServiceIml implements TransactionService {
    private final TransactionDao trans;
    private final RoomDao roomDao;
    private final OrderDao orderDao;

    public TransactionServiceIml(TransactionDao trans, RoomDao roomDao, OrderDao orderDao) {
        this.trans = trans;
        this.roomDao = roomDao;
        this.orderDao = orderDao;
    }

    @Override
    public boolean makePaymentForAll(Long userID, Long roomID, String time) {
        if (roomDao.userInRoom(roomID, userID)) {
            BigDecimal amount = trans.finishOrderByUser(userID, roomID);
            if (amount == null) {
                return false;
            }
            roomDao.closeRoom(roomID);
            List<User> usersByRoomID = roomDao.getUsersByRoomID(roomID);
            Optional<Restaurant> restaurantByRoomID = roomDao.getRestaurantByRoomID(roomID);
            if (restaurantByRoomID.isEmpty()) {
                return false;
            }
            Restaurant rest = restaurantByRoomID.get();
            usersByRoomID.forEach(user -> {
                List<OrderDto> orders = orderDao.getChosenMealsByIDs(user.getUserID(), roomID);
                Mail mail;
                if (userID.equals(user.getUserID())) {
                    mail = new OrderMail(user.getEmail(), roomID, orders, rest.getRestaurantName(),
                            rest.getLocation(),time, amount);
                } else {
                    mail = new OrderMail(user.getEmail(), roomID, orders, rest.getRestaurantName(),
                            rest.getLocation(),time , BigDecimal.ZERO);
                }
                Mail finalMail = mail;
                new Thread(() -> Mailer.sendMail(
                        finalMail))
                        .start();
            });

            return true;
        } else {
            // not in this room
            return false;
        }
    }

    @Override
    public boolean splitBillForAll(Long userID, Long roomID, String time) {
        if (roomDao.userInRoom(roomID, userID)) {
            if (trans.finishOrderByEachUser(roomID)) {
                roomDao.closeRoom(roomID);
                List<User> usersByRoomID = roomDao.getUsersByRoomID(roomID);
                Optional<Restaurant> restaurantByRoomID = roomDao.getRestaurantByRoomID(roomID);
                if (restaurantByRoomID.isEmpty()) {
                    return false;
                }
                Restaurant rest = restaurantByRoomID.get();
                usersByRoomID.forEach(user -> {
                    List<OrderDto> orders = orderDao.getChosenMealsByIDs(user.getUserID(), roomID);
                    Mail mail = new OrderMail(user.getEmail(), roomID, orders, rest.getRestaurantName(),
                            rest.getLocation(),time);
                    new Thread(() -> Mailer.sendMail(
                            mail))
                            .start();
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean payOnlyForUser(Long userID, Long roomID) {
        return false;
    }
}
