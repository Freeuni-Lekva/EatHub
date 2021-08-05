package ge.eathub.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static boolean initialized = false;
    private static final String INIT_DATABASE_FILE = "src/main/resources/mysqltables.sql";

    public static DataSource getMySqlDataSource() {
        // todo make singleton
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName(DataBaseInfo.SERVER_NAME);
        ds.setPort(DataBaseInfo.PORT);
        ds.setDatabaseName(DataBaseInfo.DATA_BASE_NAME);
        ds.setUser(DataBaseInfo.USERNAME);
        ds.setPassword(DataBaseInfo.PASSWORD);
//        try {
//            executeSqlFile(ds);
//        } catch (SQLException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
        return ds;
    }

    public static DataSource getJdbcDataSource(){
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:file:C:\\Users\\vganjelashvili\\Desktop\\oop\\EatHub\\src\\main\\resources\\data;SCHEMA=EATHUB_DB;AUTO_SERVER=true;DB_CLOSE_DELAY=-1");
        ds.setUser(DataBaseInfo.USERNAME);
        ds.setPassword(DataBaseInfo.PASSWORD);
        return  ds;
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

    private static synchronized void executeSqlFile(DataSource ds) throws SQLException, FileNotFoundException {
        if (initialized) return;
        ScriptRunner sr = new ScriptRunner(ds.getConnection());
        Reader reader = new BufferedReader(new FileReader(INIT_DATABASE_FILE));
        sr.runScript(reader);
        initialized = true;
    }
}