package ge.eathub.dao.impl;

import ge.eathub.dao.OrderDao;
import ge.eathub.database.DBConnection;
import ge.eathub.exceptions.MealUpdateException;
import ge.eathub.models.Meal;
import ge.eathub.models.Order;
import ge.eathub.models.Room;
import ge.eathub.models.UserRoom;
import org.mockito.internal.matchers.Or;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlOrderDao implements OrderDao {

    private final DataSource dataSource;

    public MySqlOrderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Order createOrder(Long userID, Long mealID, Long roomID, Integer quantity) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);".formatted(
                            Order.TABLE,
                            Order.USER_ID,
                            Order.MEAL_ID,
                            Order.ROOM_ID,
                            Order.QUANTITY), Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, userID);
            stm.setLong(2, mealID);
            stm.setLong(3, roomID);
            stm.setInt(4, quantity);
            if (stm.executeUpdate() == 1) {
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                return (new Order(userID, mealID, roomID, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public Order createOrder(Order order) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);".formatted(
                            Order.TABLE,
                            Order.USER_ID,
                            Order.MEAL_ID,
                            Order.ROOM_ID,
                            Order.QUANTITY), Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, order.getUserID());
            stm.setLong(2, order.getMealID());
            stm.setLong(3, order.getRoomID());
            stm.setInt(4, order.getQuantity());
            if (stm.executeUpdate() == 1) {
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                return (new Order(order.getUserID(), order.getMealID(), order.getRoomID(), order.getQuantity()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Meal> userMeals(Long userID, Long roomID) {
        Connection conn = null;
        List<Meal> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s INNER JOIN %s USING (%s) WHERE %s = ? AND %s = ?;".formatted(
                            Order.TABLE,
                            Meal.TABLE,
                            Meal.COLUMN_ID,
                            Order.USER_ID,
                            Order.ROOM_ID
                    ));
            stm.setLong(1,userID);
            stm.setLong(2, roomID);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new Meal(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getBigDecimal(3),
                                rs.getTime(4),
                                rs.getLong(5)
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return ret;
    }

    @Override
    public List<Meal> roomMeals(Long roomID) {
        Connection conn = null;
        List<Meal> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s INNER JOIN %s USING (%s) WHERE %s = ?;".formatted(
                            Order.TABLE,
                            Meal.TABLE,
                            Meal.COLUMN_ID,
                            Order.ROOM_ID
                    ));
            stm.setLong(1, roomID);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new Meal(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getBigDecimal(3),
                                rs.getTime(4),
                                rs.getLong(5)
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return ret;
    }

//    @Override
//    public Order updateOrder(Order order) {
//
//    }

    @Override
    public Optional<Order> updateOrder(Order order, Integer quantity) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE %s SET %s = ? where %s = ?".formatted(
                            Order.TABLE,
                            Order.QUANTITY,
                            Order.ORDER_ID), Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, quantity);
            stm.setLong(2, order.getOrderID());
            if (stm.executeUpdate() == 1) {
                return Optional.of(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public boolean removeOrder(Order order) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "DELETE FROM %s WHERE %s = ?;".formatted(
                            Order.TABLE,
                            Order.ORDER_ID));
            stm.setLong(1, order.getOrderID());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public Optional<Order> getOrderByID(Long userID, Long roomID, Long mealID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s where %s = ? and %s = ? and %s = ? ;".formatted(Order.TABLE, Order.USER_ID, Order.ROOM_ID, Order.MEAL_ID));
            stm.setLong(1, userID);
            stm.setLong(2, roomID);
            stm.setLong(3, mealID);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return Optional.of(new Order(
                            rs.getLong(1),
                            rs.getLong(2),
                            rs.getLong(3),
                            rs.getInt(4)
                    )
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getAll(Long userID, Long roomID) {
        Connection conn = null;
        List<Order> orders = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s where %s = ? and %s = ? ;".formatted(Order.TABLE, Order.USER_ID, Order.ROOM_ID));
            stm.setLong(1, userID);
            stm.setLong(2, roomID);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                            rs.getLong(1),
                            rs.getLong(2),
                            rs.getLong(3),
                            rs.getInt(4)
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return orders;
    }


}
