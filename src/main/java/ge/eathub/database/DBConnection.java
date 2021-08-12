package ge.eathub.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;

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
    private static MysqlConnectionPoolDataSource ds = null;

    public synchronized static DataSource getMySqlDataSource() {
        // todo make threadsafe
        if (ds == null) {
            ds = new MysqlConnectionPoolDataSource();
            ds.setServerName(DataBaseInfo.SERVER_NAME);
            ds.setPort(DataBaseInfo.PORT);
            ds.setDatabaseName(DataBaseInfo.DATA_BASE_NAME);
            ds.setUser(DataBaseInfo.USERNAME);
            ds.setPassword(DataBaseInfo.PASSWORD);
//        try {
//            executeSqlFile(ds,INIT_DATABASE_FILE);
//        } catch (SQLException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
        }
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

    private static synchronized void executeSqlFile(DataSource ds, String initDatabaseFile) throws SQLException, FileNotFoundException {
        if (initialized) return;
        ScriptRunner sr = new ScriptRunner(ds.getConnection());
        Reader reader = new BufferedReader(new FileReader(INIT_DATABASE_FILE));
        sr.runScript(reader);
        initialized = true;
    }
}