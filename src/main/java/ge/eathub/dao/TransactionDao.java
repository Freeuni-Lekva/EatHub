package ge.eathub.dao;

import java.math.BigDecimal;
import java.sql.Connection;

public interface TransactionDao {
    BigDecimal finishOrderByUser(Long userID, Long roomID);

    boolean minusUserBalance(Connection conn, Long userID, BigDecimal price, Long roomID, Long resID);

    boolean addRestaurantBalance(Connection conn, BigDecimal price, Long resID);

    boolean finishOrderByEachUser(Long roomID);
}
