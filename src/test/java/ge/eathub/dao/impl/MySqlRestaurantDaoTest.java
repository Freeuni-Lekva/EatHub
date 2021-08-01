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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MySqlRestaurantDaoTest {
    private static DataSource ds;
    private static RestaurantDao sqlDao;
//    @Mock
//    MySqlUserDao sqlDaoMockito;

    @BeforeAll
    static void setUp() {
        ds = DBConnection.getMySqlDataSource();
        sqlDao = new MySqlRestaurantDao(ds);
        cleanDB();
    }

    //    @BeforeEach
    static void cleanDB() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
            conn.createStatement().execute("TRUNCATE TABLE %s;".formatted(Restaurant.TABLE));
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }



    @AfterEach
    void tearDown() {
        cleanDB();
    }


    @Test
    void getAll(){
        RestaurantDao memo = new InMemoryRestaurantDao();
        Restaurant restaurant = new Restaurant("Wadi Naxe", "Vake", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"));
        Restaurant rest1 = memo.createRestaurant(restaurant);
        Restaurant rest2 = sqlDao.createRestaurant(restaurant);
        List<Restaurant> list1 = memo.getAllRestaurant();
        List<Restaurant> list2 = sqlDao.getAllRestaurant();
        assertEquals(list1.size(), list2.size());
    }

}
