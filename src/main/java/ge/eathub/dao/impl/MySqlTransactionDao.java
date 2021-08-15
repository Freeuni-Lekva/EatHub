package ge.eathub.dao.impl;

import ge.eathub.dao.RoomDao;
import ge.eathub.dao.TransactionDao;
import ge.eathub.dao.UserDao;
import ge.eathub.database.DBConnection;
import ge.eathub.exceptions.UserNotFoundException;
import ge.eathub.models.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlTransactionDao implements TransactionDao {
    private final DataSource dataSource;
    private final UserDao userDao;
    private final RoomDao roomDao;

    public MySqlTransactionDao(DataSource dataSource, UserDao userDao, RoomDao roomDao) {
        this.dataSource = dataSource;
        this.userDao = userDao;
        this.roomDao = roomDao;
    }

    @Override
    public BigDecimal finishOrderByUser(Long userID, Long roomID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT SUM(%s*%s), %s FROM %s INNER JOIN %s USING (%s) WHERE %s = ?;".formatted(
                            Meal.TABLE + "." + Meal.COLUMN_PRICE,
                            Order.TABLE + "." + Order.QUANTITY,
                            Meal.TABLE + "." + Meal.COLUMN_RESTAURANT_ID,
                            Meal.TABLE,
                            Order.TABLE,
                            Meal.COLUMN_ID,
                            Order.ROOM_ID));
            stm.setLong(1, roomID);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Optional<User> user = userDao.getUserById(userID);
                if (user.isEmpty()) {
                    throw new UserNotFoundException(userID);
                }
                BigDecimal price = rs.getBigDecimal(1);
                if (price == null) {
                    return new BigDecimal(BigInteger.ZERO);
                }
                Long resID = rs.getLong(2);
                if (user.get().getBalance().compareTo(price) >= 0) {
                    if (minusUserBalance(conn, userID, price, roomID, resID)
                            && addRestaurantBalance(conn, price, resID)) {
                        conn.commit();
                        return price;
                    }
                } else {
                    return null;
                }
            }
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert conn != null;
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection(conn);
        }
        return null;
    }


    @Override
    public boolean minusUserBalance(Connection conn, Long userID, BigDecimal price, Long roomID, Long resID) {
        try {
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE %s SET %s = %s - ?  WHERE %s = ?;".formatted(
                            User.TABLE,
                            User.COLUMN_BALANCE,
                            User.COLUMN_BALANCE,
                            User.COLUMN_ID));
            stm.setBigDecimal(1, price);
            stm.setLong(2, userID);
            if (stm.executeUpdate() == 1) {
                PreparedStatement insertStm = conn.prepareStatement(
                        "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?);".formatted(
                                Transaction.TABLE,
                                Transaction.USER_ID,
                                Transaction.RESTAURANT_ID,
                                Transaction.ROOM_ID,
                                Transaction.AMOUNT
                        ));

                insertStm.setLong(1, userID);
                insertStm.setLong(2, resID);
                insertStm.setLong(3, roomID);
                insertStm.setBigDecimal(4, price);
                if (insertStm.executeUpdate() == 1) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("minus user balance");
        return false;
    }

    @Override
    public boolean addRestaurantBalance(Connection conn, BigDecimal price, Long resID) {
        try {
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE %s SET %s = %s + ? WHERE %s = ?;".formatted(
                            Restaurant.TABLE,
                            Restaurant.COLUMN_BALANCE,
                            Restaurant.COLUMN_BALANCE,
                            Restaurant.COLUMN_ID));
            stm.setBigDecimal(1, price);
            stm.setLong(2, resID);
            if (stm.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("add rest balance");

        return false;
    }

    @Override
    public boolean finishOrderByEachUser(Long roomID) {
        System.out.println(roomID);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            List<User> users = roomDao.getUsersByRoomID(roomID);
            System.out.println(users);
            for (User user : users) {
                PreparedStatement stm = conn.prepareStatement(
                        "SELECT SUM(%s*%s), %s FROM %s INNER JOIN %s USING (%s) WHERE %s = ? AND %s =?;"
                                .formatted(
                                        Meal.TABLE + "." + Meal.COLUMN_PRICE,
                                        Order.TABLE + "." + Order.QUANTITY,
                                        Meal.TABLE + "." + Meal.COLUMN_RESTAURANT_ID,
                                        Meal.TABLE,
                                        Order.TABLE,
                                        Meal.COLUMN_ID,
                                        Order.ROOM_ID,
                                        User.COLUMN_ID));
                stm.setLong(1, roomID);
                stm.setLong(2, user.getUserID());
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    BigDecimal price = rs.getBigDecimal(1);
                    if (price == null) {
                        continue;
                    }
                    Long resID = rs.getLong(2);
                    if (user.getBalance().compareTo(price) > 0) {
                        System.out.println("userid " + user.getUserID() + " price " + price +
                                " room " + roomID + " resId " + resID);
                        if (!(minusUserBalance(conn, user.getUserID(), price, roomID, resID) &&
                                addRestaurantBalance(conn, price, resID))) {
                            System.out.println("inner if");
                            conn.rollback();
                            return false;
                        }
                        System.out.println("user " + user.getUsername() + " price " + price);
                    } else {
                        System.out.println("inner else if");
                        conn.rollback();
                        return false;
                    }
                } else {
                    System.out.println("asdasdasdasd");
                }

            }
            conn.commit();
            System.out.println("true");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection(conn);
        }
        System.out.println("fakse");
        return false;
    }
}
