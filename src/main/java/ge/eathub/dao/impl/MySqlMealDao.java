package ge.eathub.dao.impl;

import ge.eathub.dao.MealDao;
import ge.eathub.database.DBConnection;
import ge.eathub.exceptions.MealCreationException;
import ge.eathub.models.Meal;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlMealDao implements MealDao {

    private final DataSource dataSource;
    public static final int MYSQL_DUPLICATE_ERROR_CODE = 1062;

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
    public Optional<Meal> getMealById(Long mealID) {
        Connection conn = null;
        List<Meal> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s where %s = ? ;".formatted(Meal.TABLE, Meal.COLUMN_ID));
            stm.setLong(1, mealID);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return Optional.of(new Meal(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getTime(3),
                            rs.getLong(4)
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
    public Meal createMeal(Meal meal) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);  ;".formatted(
                            Meal.TABLE,
                            Meal.COLUMN_ID, Meal.COLUMN_NAME,
                            Meal.COLUMN_COOKING_TIME,
                            Meal.COLUMN_RESTAURANT_ID), Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, meal.getMealID());
            stm.setString(2, meal.getMealName());
            stm.setTime(3, meal.getCookingTime());
            stm.setLong(4, meal.getRestaurantID());
            if (stm.executeUpdate() == 1) {
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                Long newMealID = rs.getLong(1);
                return new Meal(meal).setMealID(newMealID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        throw new MealCreationException("unknown error | meal" + meal.getMealName());
    }

    @Override
    public boolean updateMeal(Meal meal) {
        return false;
    }
}
