package ge.eathub.dao.impl;

import ge.eathub.dao.*;
import ge.eathub.database.DBConnection;
import ge.eathub.dto.RoomDto;
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

import static org.junit.jupiter.api.Assertions.*;

public class MySqlRoomDaoTest {
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
            conn.createStatement().execute("TRUNCATE TABLE %s;".formatted(Room.TABLE));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Test
    void testRoom() {
        Restaurant r1 = new Restaurant("Tiflisi", "Tbilisi", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1.jpg");
        Restaurant r2 = new Restaurant("Telavuri", "Telavi", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "2.jpg");
        Restaurant restaurant1 = restaurantDao.createRestaurant(r1);
        Restaurant restaurant2 = restaurantDao.createRestaurant(r2);
        Meal meal1 = new Meal(1L, "Xinkali", new BigDecimal("0.5"), new Time(2), 1L, "");
        Meal meal2 = new Meal(2L, "Mtsvadi", new BigDecimal("1.5"), new Time(2), 1L, "");
        meal1 = mealDao.createMeal(meal1);
        meal2 = mealDao.createMeal(meal2);
        Meal meal3 = new Meal(3L, "Kartofili", new BigDecimal("1.5"), new Time(2), 2L, "");
        meal3 = mealDao.createMeal(meal3);
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
        roomDao.addUserIntoRoom(firstRoom.getRoomID(), sqlUser1.getUserID());
        roomDao.addUserIntoRoom(firstRoom.getRoomID(), sqlUser2.getUserID());

        List<User> userList = roomDao.getUsersByRoomID(firstRoom.getRoomID());
        assertEquals(2, userList.size());

        Room roomFromDB = roomDao.getRoomById(1L).get();
        assertEquals(1,roomFromDB.getRoomID());

        assertTrue(roomDao.userInRoom(1L, 1L));
        assertFalse(roomDao.userInRoom(1L, 5L));

        roomDao.addUserIntoRoom(secondRoom.getRoomID(), sqlUser1.getUserID());
        List<RoomDto> rooms = roomDao.getAllRoomByUserID(sqlUser1.getUserID());
        System.out.println(rooms);
        assertEquals(2, rooms.size());
        assertEquals(firstRoom.getRoomID(), rooms.get(0).getRoomID());
        assertEquals(secondRoom.getRoomID(), rooms.get(1).getRoomID());

        rooms = roomDao.getAllRoomDto();
        assertEquals(2, rooms.size());
        assertEquals(firstRoom.getRoomID(), rooms.get(0).getRoomID());
        assertEquals(secondRoom.getRoomID(), rooms.get(1).getRoomID());

        List<Room> roomList = roomDao.getRoomsByUserID(sqlUser1.getUserID());
        assertEquals(2, roomList.size());

        assertTrue(roomDao.mealInRoom(meal1.getMealID(), firstRoom.getRoomID()));
        assertFalse(roomDao.mealInRoom(meal3.getMealID(), firstRoom.getRoomID()));
        assertTrue(roomDao.mealInRoom(meal3.getMealID(), secondRoom.getRoomID()));

        roomList = roomDao.getAllRooms();
        assertEquals(2, roomList.size());
        roomDao.createRoom(1L);
        roomList = roomDao.getAllRooms();
        assertEquals(3, roomList.size());

        roomDao.closeRoom(firstRoom);
        Room updatedRoom1 = roomDao.getRoomById(firstRoom.getRoomID()).get();
        assertFalse(updatedRoom1.getActive());

    }
}
