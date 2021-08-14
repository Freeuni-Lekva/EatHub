package ge.eathub.dao.impl;

import ge.eathub.dao.*;
import ge.eathub.database.DBConnection;
import ge.eathub.models.*;
import ge.eathub.models.chat.Message;
import ge.eathub.models.chat.MessageType;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySqlChatDaoTest {
    private static DataSource ds;
    private static OrderDao orderDao;
    private static RoomDao roomDao;
    private static UserDao userDao;
    private static RestaurantDao restaurantDao;
    private static MealDao mealDao;
    private static ChatDao chatDao;

    @BeforeAll
    static void setUp() {
        ds = DBConnection.getMySqlDataSource();
        orderDao = new MySqlOrderDao(ds);
        restaurantDao = new MySqlRestaurantDao(ds);
        userDao = new MySqlUserDao(ds);
        roomDao = new MySqlRoomDao(ds);
        mealDao = new MySqlMealDao(ds);
        chatDao = new MySqlChatDao(ds);
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
            conn.createStatement().execute("TRUNCATE TABLE %s;".formatted(Message.TABLE));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Test
    void testChat() {
        Restaurant r1 = new Restaurant("Tiflisi", "Tbilisi", 1000L, new BigDecimal("3.0"), new BigDecimal("1000.0"), "1.jpg");
        Restaurant restaurant1 = restaurantDao.createRestaurant(r1);
        Meal meal1 = new Meal(1L, "Xinkali", new BigDecimal("0.5"), new Time(2), 1L, "");
        mealDao.createMeal(meal1);
        String username = "usernameTest";
        String password = "passTest";
        String email = "emailTest";
        User usr = new User(username, password, email);
        User sqlUser1 = userDao.createUser(usr);
        User sqlUser2 = userDao.createUser(new User("test", "test", "test"));
        Optional<Room> sqlRoom1 = roomDao.createRoom(1L);
        Room firstRoom = sqlRoom1.get();
        roomDao.addUserIntoRoom(firstRoom.getRoomID(), sqlUser1.getUserID());
        roomDao.addUserIntoRoom(firstRoom.getRoomID(), sqlUser2.getUserID());
        Order order1 = orderDao.createOrder(sqlUser1.getUserID(), 1L, 1L, 10);
        String text1 = "Hello, let's choose the meals!";
        String text2 = "Hi, Cool, I am very hungry.";
        Message message1 = new Message(sqlUser1.getUsername(), sqlUser1.getUserID(), firstRoom.getRoomID(), MessageType.TEXT, text1);
        Message message2 = new Message(sqlUser2.getUsername(), sqlUser2.getUserID(), firstRoom.getRoomID(), MessageType.TEXT, text2);
        assertTrue(chatDao.saveMessage(message1));
        assertTrue(chatDao.saveMessage(message2));
        assertEquals(0, chatDao.getMessagesByRoomID(2L).size());
        List<Message> messageList = chatDao.getMessagesByRoomID(firstRoom.getRoomID());
        assertEquals(2, messageList.size());
        assertTrue(messageList.get(0).getContent().equals(text1));
        assertTrue(messageList.get(1).getContent().equals(text2));
        assertEquals(sqlUser1.getUserID(), messageList.get(0).getUserID());
        assertEquals(sqlUser2.getUserID(), messageList.get(1).getUserID());
    }

}
