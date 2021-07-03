package ge.eathub.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "toor";
    private static final String SERVER_NAME = "localhost";
    private static final int PORT = 3306;
    private static final String DATA_BASE_NAME = "eathub_db";

    public static DataSource getMySqlDataSource() {
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName(SERVER_NAME);
        ds.setPort(PORT);
        ds.setDatabaseName(DATA_BASE_NAME);
        ds.setUser(USERNAME);
        ds.setPassword(PASSWORD);
        return ds;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}