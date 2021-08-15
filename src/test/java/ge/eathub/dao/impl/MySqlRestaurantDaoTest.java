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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySqlRestaurantDaoTest {
    private static DataSource ds;
    private static RestaurantDao sqlDao;
    private static MealDao mealDao;
//    @Mock
//    MySqlUserDao sqlDaoMockito;

    @BeforeAll
    static void setUp() {
        ds = DBConnection.getMySqlDataSource();
        sqlDao = new MySqlRestaurantDao(ds);
        mealDao = new MySqlMealDao(ds);
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
    void getAll() {
        RestaurantDao memo = new InMemoryRestaurantDao();
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "2.jpg");
        Restaurant rest1 = memo.createRestaurant(restaurant);
        Restaurant rest2 = sqlDao.createRestaurant(restaurant);
        List<Restaurant> list1 = memo.getAllRestaurant();
        List<Restaurant> list2 = sqlDao.getAllRestaurant();
        assertEquals(list1.size(), list2.size());
    }

    @Test
    void getById() {
        RestaurantDao memo = new InMemoryRestaurantDao();
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "2.jpg");
        Restaurant rest1 = memo.createRestaurant(restaurant);
        Restaurant rest2 = sqlDao.createRestaurant(restaurant);
        Optional<Restaurant> foundRestaurant = sqlDao.getRestaurantById(1L);
        assertTrue(foundRestaurant.isPresent());
        assertEquals(1, foundRestaurant.get().getRestaurantID());
        assertEquals(1, foundRestaurant.get().getRestaurantID());
        assertEquals(memo.getRestaurantById(1L), sqlDao.getRestaurantById(1L));
    }

    @Test
    void getMeals() {
        RestaurantDao memo = new InMemoryRestaurantDao();
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "2.jpg");
        Restaurant rest1 = memo.createRestaurant(restaurant);
        Restaurant rest2 = sqlDao.createRestaurant(restaurant);
        Meal meal1 = new Meal(1L, "Xinkali", new BigDecimal("0.5"), new Time(2), 1L, "");
        Meal meal2 = new Meal(2L, "Mtsvadi", new BigDecimal("1.5"), new Time(2), 1L, "");
        Meal meal3 = new Meal(3L, "Adjafsandali", new BigDecimal("0.5"), new Time(2), 1L, "");
        mealDao.createMeal(meal1);
        mealDao.createMeal(meal2);
        List<Meal> meals = sqlDao.getAllMeals(1L);
        assertEquals(meal1, meals.get(0));
        assertEquals(meal2, meals.get(1));
        mealDao.createMeal(meal3);
        meals = sqlDao.getMealsBySubName("ali", 1L);
        assertEquals(meal1, meals.get(0));
        assertEquals(meal3, meals.get(1));
    }

    @Test
    void getRestauransByMeals() {
        RestaurantDao memo = new InMemoryRestaurantDao();
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1.jpg");
        Restaurant restaurant2 = new Restaurant("Modi Naxe", "Vaja", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1.jpg");
        Restaurant restMemo1 = memo.createRestaurant(restaurant);
        Restaurant restMemo2 = memo.createRestaurant(restaurant2);
        Restaurant rest1 = sqlDao.createRestaurant(restaurant);
        Restaurant rest2 = sqlDao.createRestaurant(restaurant2);
        Meal meal1 = new Meal(1L, "Xinkali", new BigDecimal("0.5"), new Time(2), 1L, "");
        Meal meal2 = new Meal(2L, "Mtsvadi", new BigDecimal("1.5"), new Time(2), 1L, "");
        Meal meal3 = new Meal(3L, "Adjafsandali", new BigDecimal("0.5"), new Time(2), 1L, "");
        mealDao.createMeal(meal1);
        mealDao.createMeal(meal2);
        List<Meal> meals = sqlDao.getAllMeals(1L);
        mealDao.createMeal(meal3);
        Map<Restaurant, List<Meal>> mp = sqlDao.getRestaurantsByMeal("ali");
        assertEquals(1, mp.size());
    }

    @Test
    void updateRestaurant() {
        RestaurantDao memo = new InMemoryRestaurantDao();
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "");
        Restaurant restMemo1 = memo.createRestaurant(restaurant);
        Restaurant rest1 = sqlDao.createRestaurant(restaurant);
        long id = rest1.getRestaurantID();
        long inMemoryID = restMemo1.getRestaurantID();
        memo.updateRestaurant(inMemoryID, new Restaurant("Modi naxe", "Vaja", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), ""));
        sqlDao.updateRestaurant(id, new Restaurant("Modi naxe", "Vaja", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), ""));
        assertEquals(sqlDao.getRestaurantById(id).get().getRestaurantName(), "Modi naxe");
        assertEquals(memo.getRestaurantById(id).get().getRestaurantName(), "Modi naxe");
    }

}
