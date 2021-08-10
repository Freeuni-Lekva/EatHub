package ge.eathub.dao.impl;

import ge.eathub.dao.RestaurantDao;
import ge.eathub.database.DBConnection;
import ge.eathub.exceptions.RestaurantCreationException;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class MySqlRestaurantDao implements RestaurantDao {

    private final DataSource dataSource;

    public MySqlRestaurantDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        Connection conn = null;
        List<Restaurant> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s ;".formatted(Restaurant.TABLE));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new Restaurant(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getLong(4),
                                rs.getBigDecimal(5),
                                rs.getBigDecimal(6)
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
    public Optional<Restaurant> getRestaurantById(Long restaurantID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s where %s = ?;".formatted(Restaurant.TABLE, Restaurant.COLUMN_ID));
            stm.setLong(1, restaurantID);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return Optional.of(new Restaurant(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getLong(4),
                    rs.getBigDecimal(5),
                    rs.getBigDecimal(6)
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Meal> getAllMeals(Long restaurantID) {
        Connection conn = null;
        List<Meal> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s where %s = ?;".formatted(Meal.TABLE, Meal.COLUMN_RESTAURANT_ID));
            stm.setLong(1, restaurantID);
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
    public List<Meal> getMealsBySubName(String mealName, Long restaurantID) {
        Connection conn = null;
        List<Meal> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s where %s like ? and %s = ?;".formatted(Meal.TABLE, Meal.COLUMN_NAME, Meal.COLUMN_RESTAURANT_ID));
            stm.setString(1,"%" + mealName + "%");
            stm.setLong(2, restaurantID);
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
    public Map<Restaurant, List<Meal>> getRestaurantsByMeal(String mealName) {
        Connection conn = null;
        Map<Restaurant, List<Meal>> ret = new HashMap<>();
        try {
            conn = dataSource.getConnection();
            String mealTableName = "m";
            String restaurantTableName = "r";
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s, %s where %s = %s and %s like ? ;"
                            .formatted(Meal.TABLE + " " + mealTableName,
                                       Restaurant.TABLE + " " + restaurantTableName,
                                       mealTableName + "." + Meal.COLUMN_RESTAURANT_ID,
                                       restaurantTableName + "." + Restaurant.COLUMN_ID,
                                       mealTableName + "." + Meal.COLUMN_NAME));
            stm.setString(1,"%" + mealName + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Meal meal = new Meal(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getBigDecimal(3),
                        rs.getTime(4),
                        rs.getLong(5)
                );
                Restaurant restaurant = new Restaurant(
                        rs.getLong(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getLong(9),
                        rs.getBigDecimal(10),
                        rs.getBigDecimal(11)
                );
                if (!ret.containsKey(restaurant)){
                    ret.put(restaurant, new ArrayList<>());
                }
                ret.get(restaurant).add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return ret;
    }

    @Override
    public boolean updateRestaurant(long restaurantID, Restaurant restaurant) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE %s SET %s = ?,  %s = ? , %s = ?, %s = ?, %s = ? where %s = ?;".formatted(
                            Restaurant.TABLE,
                            Restaurant.COLUMN_NAME, Restaurant.COLUMN_LOCATION,
                            Restaurant.COLUMN_LIMIT, Restaurant.COLUMN_RATING, Restaurant.COLUMN_BALANCE, Restaurant.COLUMN_ID), Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, restaurant.getRestaurantName());
            stm.setString(2, restaurant.getLocation());
            stm.setLong(3, restaurant.getLimit());
            stm.setBigDecimal(4, restaurant.getRating());
            stm.setBigDecimal(5, restaurant.getBalance());
            stm.setLong(6, restaurantID);
            if (stm.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        throw new RestaurantCreationException("unknown error | restaurant " + restaurant.getRestaurantName());
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?);  ;".formatted(
                            Restaurant.TABLE,
                            Restaurant.COLUMN_NAME, Restaurant.COLUMN_LOCATION,
                            Restaurant.COLUMN_LIMIT, Restaurant.COLUMN_RATING, Restaurant.COLUMN_BALANCE), Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, restaurant.getRestaurantName());
            stm.setString(2, restaurant.getLocation());
            stm.setLong(3, restaurant.getLimit());
            stm.setBigDecimal(4, restaurant.getRating());
            stm.setBigDecimal(5, restaurant.getBalance());
            if (stm.executeUpdate() == 1) {
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                Long newRestaurantID = rs.getLong(1);
                return new Restaurant(restaurant).setRestaurantID(newRestaurantID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        throw new RestaurantCreationException("unknown error | restaurant " + restaurant.getRestaurantName());
    }
}
