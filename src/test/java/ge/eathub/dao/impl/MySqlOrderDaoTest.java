package ge.eathub.dao.impl;

import ge.eathub.dao.*;
import ge.eathub.database.DBConnection;
import ge.eathub.dto.OrderDto;
import ge.eathub.models.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MySqlOrderDaoTest {
    private static DataSource ds;
    private static OrderDao orderDao;
    private static RoomDao roomDao;
    private static UserDao userDao;
    private static RestaurantDao restaurantDao;
    private static MealDao mealDao;

    @BeforeAll
    static void setUp() {
        ds = DBConnection.getMySqlDataSource();
        orderDao = new MySqlOrderDao(ds);
        restaurantDao = new MySqlRestaurantDao(ds);
        userDao = new MySqlUserDao(ds);
        roomDao = new MySqlRoomDao(ds);
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
            conn.createStatement().execute("TRUNCATE TABLE %s;".formatted(Order.TABLE));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Test
    void testOrder() {
        Restaurant r1 = new Restaurant("Tiflisi", "Tbilisi", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1.jpg");
        Restaurant r2 = new Restaurant("Telavuri", "Telavi", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "2.jpg");
        Restaurant restaurant1 = restaurantDao.createRestaurant(r1);
        Restaurant restaurant2 = restaurantDao.createRestaurant(r2);
        Meal meal1 = new Meal(1L, "Xinkali", new BigDecimal("0.5"), new Time(2), 1L, "");
        Meal meal2 = new Meal(2L, "Mtsvadi", new BigDecimal("1.5"), new Time(2), 1L, "");
        mealDao.createMeal(meal1);
        mealDao.createMeal(meal2);
        Meal meal3 = new Meal(3L, "Kartofili", new BigDecimal("1.5"), new Time(2), 2L, "");
        String username = "usernameTest";
        String password = "passTest";
        String email = "emailTest";
        User usr = new User(username, password, email);
        User sqlUser1 = userDao.createUser(usr);
        User usr2 = new User("test", "test", "test");
        User sqlUser2 = userDao.createUser(usr2);
        Optional<Room> sqlRoom1 = roomDao.createRoom(1L);
        Optional<Room> sqlRoom2 = roomDao.createRoom(2L);
        Room firstRoom = sqlRoom1.get();
        Room secondRoom = sqlRoom2.get();
        Order order1 = orderDao.createOrder(sqlUser1.getUserID(), 1L, 1L, 10);
        Order order2 = orderDao.createOrder(sqlUser1.getUserID(), 2L, 1L, 30);
        assertEquals(1, order1.getOrderID());
        assertEquals(2, order2.getOrderID());
        assertEquals(10, order1.getQuantity());
        assertEquals(30, order2.getQuantity());
        orderDao.updateOrder(order1, 100);
        order1 = orderDao.getOrderByID(order1.getUserID(), order1.getRoomID(), order1.getMealID()).get();
        assertEquals(100, order1.getQuantity());
        List<Order> list = orderDao.getAll(order1.getUserID(), order1.getRoomID());
        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getOrderID());
        assertEquals(2, list.get(1).getOrderID());
        assertEquals(100, list.get(0).getQuantity());
        assertEquals(30, list.get(1).getQuantity());

        orderDao.removeOrder(order1);
        assertEquals(1, orderDao.getAll(order1.getUserID(), order1.getRoomID()).size());
        assertEquals(order2.getOrderID(), orderDao.getAll(order1.getUserID(), order1.getRoomID()).get(0).getOrderID());

        Order order3 = orderDao.createOrder(order1);
        assertEquals(3, order3.getOrderID());

        Order testOrder3 = orderDao.getOrderByID(order3.getUserID(), order3.getRoomID(), order3.getMealID()).get();
        assertEquals(testOrder3.getOrderID(), order3.getOrderID());

        List<Meal> meals = orderDao.userMeals(order1.getUserID(), order1.getRoomID());
        assertEquals(meal1, meals.get(0));
        assertEquals(meal2, meals.get(1));
        order1 = orderDao.getOrderByID(order1.getUserID(), order1.getRoomID(), order1.getMealID()).get();
        Order order4 = orderDao.createOrder(2L, 1L, 1L, 1);
        List<OrderDto> dtos = orderDao.getChosenMealsByRoomID(1L);
        assertEquals(3, dtos.size());
        List<Meal> roomMeals = orderDao.roomMeals(1L);
        assertEquals(2, roomMeals.size());

        List<OrderDto> orderDtos = orderDao.getChosenMealsByIDs(sqlUser1.getUserID(), firstRoom.getRoomID());
        assertEquals(2, orderDtos.size());
    }

}
