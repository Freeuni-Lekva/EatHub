package ge.eathub.dao.impl;

import ge.eathub.dao.RestaurantDao;
import ge.eathub.database.DBConnection;
import ge.eathub.exceptions.MealCreationException;
import ge.eathub.exceptions.RestauranCreationException;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;
import ge.eathub.models.Role;
import ge.eathub.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlRestaurantDao implements RestaurantDao {

    private final DataSource dataSource;
    public static final int MYSQL_DUPLICATE_ERROR_CODE = 1062;

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
                                rs.getDouble(5),
                                rs.getDouble(6)
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
                    rs.getDouble(5),
                    rs.getDouble(6)
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
                                rs.getTime(3),
                                rs.getLong(4)
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
                                rs.getTime(3),
                                rs.getLong(4)
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
    public List<Restaurant> getRestaurantsByMeal(String mealName) {
        Connection conn = null;
        List<Restaurant> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT distinct(%s) FROM %s where %s like ? ;".formatted(Meal.COLUMN_RESTAURANT_ID,  Meal.TABLE, Meal.COLUMN_NAME));
            stm.setString(1,"%" + mealName + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong(1);
                Optional<Restaurant> currentRestaurant = getRestaurantById(id);
                if(!currentRestaurant.isEmpty()){
                    ret.add(currentRestaurant.get());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return ret;
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?);  ;".formatted(
                            Restaurant.TABLE,
                            Restaurant.COLUMN_ID, Restaurant.COLUMN_NAME, Restaurant.COLUMN_LOCATION,
                            Restaurant.COLUMN_LIMIT, Restaurant.COLUMN_RATING, Restaurant.COLUMN_BALANCE), Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, restaurant.getRestaurantID());
            stm.setString(2, restaurant.getRestaurantName());
            stm.setString(3, restaurant.getLocation());
            stm.setLong(4, restaurant.getLimit());
            stm.setDouble(5, restaurant.getRating());
            stm.setDouble(6, restaurant.getBalance());
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
        throw new RestauranCreationException("unknown error | restaurant " + restaurant.getRestaurantName());
    }
}
