package ge.eathub.dao.impl;

import ge.eathub.database.DBConnection;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class InitDB {

    private static final String INIT_DATABASE_FILE = "src/test/resources/mysqlTestTables.sql";
    private static ScriptRunner sr;
    private static Reader reader;

    public static synchronized void init(DataSource ds) {
        if (sr == null) {
            Connection connection = null;
            try {
                connection = ds.getConnection();
                sr = new ScriptRunner(connection);
                reader = new BufferedReader(new FileReader(INIT_DATABASE_FILE));

            } catch (FileNotFoundException | SQLException e) {
                e.printStackTrace();
//                DBConnection.closeConnection(connection);
            }
        }
    }


    public static synchronized void executeSqlFile() {
        try {
            reader.reset();
            sr.runScript(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void executeSqlFile(DataSource ds) {
        Connection connection = null;
        try {
            connection = ds.getConnection();
            sr = new ScriptRunner(connection);
            sr.setLogWriter(null);
            reader = new BufferedReader(new FileReader(INIT_DATABASE_FILE));
            sr.runScript(reader);
        } catch (FileNotFoundException | SQLException e) {
            readerClose(reader);
            DBConnection.closeConnection(connection);
        }
    }

    private static void readerClose(Reader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

    }

}

