package ge.eathub.dao.impl;

import ge.eathub.dao.UserDao;
import ge.eathub.database.DBConnection;
import ge.eathub.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(MockitoExtension.class)
class MySqlUserDaoTest {
    private static DataSource ds;
    private static UserDao sqlDao;
//    @Mock
//    MySqlUserDao sqlDaoMockito;

    @BeforeAll
    static void setUp() {
        ds = DBConnection.getMySqlDataSource();
        sqlDao = new MySqlUserDao(ds);
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
            conn.createStatement().execute("TRUNCATE TABLE %s;".formatted(User.TABLE));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }


    @Test
    void getAllUsers() {
        UserDao memoDao = new InMemoryUserDao();
        assertTrue(sqlDao.getAllUsers().isEmpty());
        String username = "usernameTest";
        String password = "passTest";
        String email = "emailTest";
        User usr = new User(username, password, email);
        User sqlUser = sqlDao.createUser(usr);
        memoDao.createUser(usr);
        assertEquals(usr.getUsername(), sqlUser.getUsername());
        List<User> l1 = sqlDao.getAllUsers();
        List<User> l2 = memoDao.getAllUsers();
        assertEquals(l1.size(), l2.size());
    }

    @Test
    void getUserById() {
        String username = "usernameTest";
        String password = "passTest";
        String email = "emailTest";
        User usr = new User(username, password, email);
        User sqlUser = sqlDao.createUser(usr);
        assertEquals(usr.getUsername(), sqlUser.getUsername());
        Optional<User> userById = sqlDao.getUserById(sqlUser.getUserID());
        assertTrue(userById.isPresent());
        assertEquals(sqlUser, userById.get());

    }

    @Test
    void getUserByUsername() {
        String username = "usernameTest";
        String password = "passTest";
        String email = "emailTest";
        User usr = new User(username, password, email);
        User sqlUser = sqlDao.createUser(usr);
        assertEquals(usr.getUsername(), sqlUser.getUsername());
        Optional<User> userById = sqlDao.getUserByUsername(username);
        assertTrue(userById.isPresent());
        assertEquals(sqlUser, userById.get());
    }

    @Test
    void createUser() {
        UserDao memoDao = new InMemoryUserDao();
        String username = "usernameTest";
        String password = "passTest";
        String email = "emailTest";
        User usr = new User(username, password, email);
        User sqlUser = sqlDao.createUser(usr);
        User memoUser = memoDao.createUser(usr);
        assertEquals(memoUser.getUsername(), sqlUser.getUsername());
        assertTrue(sqlDao.checkInfo("new User", "new Email"));
        assertTrue(sqlDao.confirmUserRegistration(username));
    }
}