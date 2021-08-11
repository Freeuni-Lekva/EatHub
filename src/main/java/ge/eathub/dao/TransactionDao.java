package ge.eathub.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionDao {
    boolean finishOrderByUser (Long userID, Long roomID) throws SQLException;

    boolean minusUserBalance (Connection conn, Long userID, BigDecimal price, Long roomID) throws SQLException;

    boolean addRestaurantBalance (Connection conn, Long roomID, BigDecimal price, Long userID) throws SQLException;

    boolean finishOrderByEachUser (Long roomID) throws SQLException;
}
