package ge.eathub.dao.impl;

import ge.eathub.dao.MealDao;
import ge.eathub.dao.RestaurantDao;
import ge.eathub.database.DBConnection;
import ge.eathub.models.Meal;
import ge.eathub.models.Restaurant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlMealDaoTest {
    private static DataSource ds;
    private static MealDao sqlDao;
    private static RestaurantDao sqlRestaurant;
//    @Mock
//    MySqlMealDao sqlDaoMockito;

    @BeforeAll
    static void setUp() {
        ds = DBConnection.getMySqlDataSource();
        sqlDao = new MySqlMealDao(ds);
        sqlRestaurant = new MySqlRestaurantDao(ds);
        InitDB.executeSqlFile(ds);
    }

    @AfterEach
    void tearDown() {
        InitDB.executeSqlFile(ds);
    }

    //    @BeforeEach
    static void cleanDB() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.createStatement().execute("TRUNCATE TABLE %s;".formatted(Meal.TABLE));
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
            conn.createStatement().execute("TRUNCATE TABLE %s;".formatted(Restaurant.TABLE));
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }


    @Test
    void getAllMeals() {
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1. jpg");
        sqlRestaurant.createRestaurant(restaurant);
        MealDao memoDao = new InMemoryMealDao();
        assertTrue(sqlDao.getAllMeals().isEmpty());
        BigDecimal rating = new BigDecimal(2.9);
        BigDecimal balance = new BigDecimal(2000);
        Time tm = new Time(2);
        Meal meal = new Meal(1L, "Xinkali", new BigDecimal("0.5"), tm, 1L, "1.jpg");
        Meal sqlMeal = sqlDao.createMeal(meal);
        memoDao.createMeal(sqlMeal);
        assertEquals(meal, sqlMeal);
        List<Meal> l1 = sqlDao.getAllMeals();
        List<Meal> l2 = memoDao.getAllMeals();
        assertTrue(l1.get(0).getMealName().equals(l2.get(0).getMealName()));
        assertEquals(l1.size(), l2.size());
        Meal currMeal = new Meal(2L, "Mwvadi", new BigDecimal("11.0"), tm, 1L, "2.jpg");
        memoDao.createMeal(currMeal);
        l1 = sqlDao.getAllMeals();
        l2 = memoDao.getAllMeals();
        assertNotEquals(l1.size(), l2.size());
    }

    //TODO: mealPrice is when we select with id int
    @Test
    void getUserById() {
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1.jpg");
        sqlRestaurant.createRestaurant(restaurant);
        String name = "Khinkali";
        BigDecimal price = new BigDecimal("2.2");
        Meal meal = new Meal(name, price, new Time(3L), 1L, "2.jpg");
        MealDao memoDao = new InMemoryMealDao();
        Meal sqlMeal = sqlDao.createMeal(meal);
        assertEquals(meal.getMealName(), sqlMeal.getMealName());
        Optional<Meal> mealById = sqlDao.getMealById(sqlMeal.getMealID());
        assertTrue(mealById.isPresent());
    }


    @Test
    void createMeal() {
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1.jpg");
        sqlRestaurant.createRestaurant(restaurant);
        String name = "Mtsvadi";
        Meal meal = new Meal(name, new BigDecimal("2.2"), new Time(3L), 1L, "2.jpg");
        MealDao memoDao = new InMemoryMealDao();
        Meal memoMeal = memoDao.createMeal(meal);
        Meal sqlMeal = sqlDao.createMeal(meal);
        assertEquals(sqlMeal, memoMeal);
    }

    @Test
    void updateMeal() {
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1.jpg");
        sqlRestaurant.createRestaurant(restaurant);
        String name = "Mtsvadi";
        Meal meal = new Meal(name, new BigDecimal("2.2"), new Time(3L), 1L, "1.jpg");
        MealDao memoDao = new InMemoryMealDao();
        Meal memoMeal = memoDao.createMeal(meal);
        Meal sqlMeal = sqlDao.createMeal(meal);
        Meal newMeal = new Meal(memoMeal);
        newMeal.setMealName("Xinkali");
        memoDao.updateMeal(newMeal, 1L);
        sqlDao.updateMeal(newMeal, 1L);
        assertTrue(sqlDao.getMealById(newMeal.getMealID()).get().getMealName().equals(memoDao.getMealById(newMeal.getMealID()).get().getMealName()));
        sqlDao.updateMeal(newMeal);
        assertTrue(sqlDao.getMealById(newMeal.getMealID()).get().getMealName().equals(memoDao.getMealById(newMeal.getMealID()).get().getMealName()));

    }
}
