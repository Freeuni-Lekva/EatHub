package ge.eathub.dao.impl;

import ge.eathub.dao.MealDao;
import ge.eathub.database.DBConnection;
import ge.eathub.exceptions.MealAlreadyExistsException;
import ge.eathub.exceptions.MealCreationException;
import ge.eathub.exceptions.MealUpdateException;
import ge.eathub.models.Meal;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ge.eathub.dao.impl.MySqlConstants.MYSQL_DUPLICATE_ERROR_CODE;

public class MySqlMealDao implements MealDao {

    private final DataSource dataSource;

    public MySqlMealDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Meal> getAllMeals() {
        Connection conn = null;
        List<Meal> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s ;".formatted(Meal.TABLE));
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
    public Optional<Meal> getMealById(Long mealID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s where %s = ? ;".formatted(Meal.TABLE, Meal.COLUMN_ID));
            stm.setLong(1, mealID);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return Optional.of(new Meal(
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
        return Optional.empty();
    }

    @Override
    public Meal createMeal(Meal meal) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);".formatted(
                            Meal.TABLE,
                            Meal.COLUMN_NAME,
                            Meal.COLUMN_PRICE,
                            Meal.COLUMN_COOKING_TIME,
                            Meal.COLUMN_RESTAURANT_ID), Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, meal.getMealName());
            stm.setBigDecimal(2, meal.getMealPrice());
            stm.setTime(3, meal.getCookingTime());
            stm.setLong(4, meal.getRestaurantID());
            if (stm.executeUpdate() == 1) {
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                Long newMealID = rs.getLong(1);
                return new Meal(meal).setMealID(newMealID);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_ERROR_CODE) {
                throw new MealAlreadyExistsException(meal.getMealName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        throw new MealCreationException("unknown error | meal" + meal.getMealName());
    }


    @Override
    public boolean updateMeal(Meal meal, Long restaurantID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE %s SET %s = ?, %s = ?, %s = ? where %s = ? and %s = ? ;".formatted(
                            Meal.TABLE,
                            Meal.COLUMN_NAME,
                            Meal.COLUMN_PRICE,
                            Meal.COLUMN_COOKING_TIME,
                            Meal.COLUMN_ID,
                            Meal.COLUMN_RESTAURANT_ID), Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, meal.getMealName());
            stm.setBigDecimal(2, meal.getMealPrice());
            stm.setTime(3, meal.getCookingTime());
            stm.setLong(4, meal.getMealID());
            stm.setLong(5, restaurantID);
            if (stm.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_ERROR_CODE) {
                throw new MealAlreadyExistsException(meal.getMealName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        throw new MealUpdateException("unknown error | meal" + meal.getMealName());
    }

    @Override
    public boolean updateMeal(Meal meal) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE %s SET %s = ?, %s = ?, %s = ? where %s = ? ;".formatted(
                            Meal.TABLE,
                            Meal.COLUMN_NAME,
                            Meal.COLUMN_PRICE,
                            Meal.COLUMN_COOKING_TIME,
                            Meal.COLUMN_ID), Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, meal.getMealName());
            stm.setBigDecimal(2, meal.getMealPrice());
            stm.setTime(3, meal.getCookingTime());
            stm.setLong(4, meal.getMealID());
            if (stm.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_ERROR_CODE) {
                throw new MealAlreadyExistsException(meal.getMealName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        throw new MealUpdateException("unknown error | meal" + meal.getMealName());
    }

}