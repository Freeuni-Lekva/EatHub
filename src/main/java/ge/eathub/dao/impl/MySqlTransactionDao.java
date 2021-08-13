package ge.eathub.dao.impl;

import com.mysql.cj.jdbc.exceptions.NotUpdatable;
import ge.eathub.dao.RoomDao;
import ge.eathub.dao.TransactionDao;
import ge.eathub.dao.UserDao;
import ge.eathub.database.DBConnection;
import ge.eathub.exceptions.NotEnoughMoney;
import ge.eathub.exceptions.SelectHasNotAnswer;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.models.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlTransactionDao implements TransactionDao {
    private final DataSource dataSource;
    private final UserDao userDao;
    private final RoomDao roomDao;

    public MySqlTransactionDao(DataSource dataSource, UserDao userDao, RoomDao roomDao){
        this.dataSource = dataSource;
        this.userDao = userDao;
        this.roomDao = roomDao;
    }

    @Override
    public boolean finishOrderByUser(Long userID, Long roomID) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT SUM(%s) FROM %s INNER JOIN %s USING (%s) WHERE %s = ?;".formatted(
                            Meal.TABLE + "." + Meal.COLUMN_PRICE,
                            Meal.TABLE,
                            Order.TABLE,
                            Meal.COLUMN_ID,
                            Order.USER_ID));
            stm.setLong(1, userID);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Optional<User> user = userDao.getUserById(userID);
                BigDecimal price = rs.getBigDecimal(1);
                if (user.isPresent() && user.get().getBalance().compareTo(price) >= 0) {
                    if (minusUserBalance(conn, userID, price, roomID) && addRestaurantBalance(conn, roomID, price, userID)) {
                        conn.commit();
                        return true;
                    } else {
                        throw new RuntimeException();
                    }
                } else {
                    conn.rollback();
                    throw new NotEnoughMoney(user.get().getUserID());
                }
            } else {
                conn.rollback();
                throw new SelectHasNotAnswer("No delivery");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
            DBConnection.closeConnection(conn);
        }
        conn.rollback();
        return false;
    }



    @Override
    public boolean minusUserBalance(Connection conn, Long userID, BigDecimal price, Long roomID) throws SQLException {
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
                Long resID = roomDao.getRoomById(roomID).get().getRestaurantID();
                insertStm.setLong(1,userID);
                insertStm.setLong(2, resID);
                insertStm.setLong(3, roomID);
                insertStm.setBigDecimal(4, price);
                if (insertStm.executeUpdate() == 1) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.rollback();
        return false;
    }

    @Override
    public boolean addRestaurantBalance(Connection conn, Long roomID, BigDecimal price, Long userID) throws SQLException {
        try {
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE %s SET %s = %s + ?  where %s = ?;".formatted(
                            Restaurant.TABLE,
                            User.COLUMN_BALANCE,
                            User.COLUMN_BALANCE,
                            Restaurant.COLUMN_ID));
            Long resID = roomDao.getRoomById(roomID).get().getRestaurantID();
            stm.setBigDecimal(1, price);
            stm.setLong(2, resID);
            if (stm.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.rollback();
        return false;
    }

    @Override
    public boolean finishOrderByEachUser(Long roomID) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            List<User> users = roomDao.getUsersByRoomID(roomID);
            for (int i = 0; i < users.size(); i++) {
                PreparedStatement stm = conn.prepareStatement(
                        "SELECT SUM(%s) FROM %s INNER JOIN %s USING (%s) WHERE %s = ?;".formatted(
                                Meal.TABLE + "." + Meal.COLUMN_PRICE,
                                Meal.TABLE,
                                Order.TABLE,
                                Meal.COLUMN_ID,
                                Meal.COLUMN_RESTAURANT_ID));
                stm.setLong(1, roomID);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    User user = users.get(i);
                    BigDecimal balance = rs.getBigDecimal(1);
                    //prices.add(balance);
                    if (user.getBalance().compareTo(balance) < 0) {
                        if (!(minusUserBalance(conn, user.getUserID(), balance, roomID)) ||
                                addRestaurantBalance(conn, roomID, balance, user.getUserID())) {
                            conn.rollback();
                            return false;
                        }
                    } else {
                        conn.rollback();
                        throw new NotEnoughMoney(user.getUserID());
                    }
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
            DBConnection.closeConnection(conn);
        }
        conn.commit();
        return true;
    }
}
