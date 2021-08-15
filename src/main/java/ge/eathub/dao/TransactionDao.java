package ge.eathub.dao;

import java.math.BigDecimal;

public interface TransactionDao {
    BigDecimal finishOrderByUser(Long userID, Long roomID);

    boolean finishOrderByEachUser(Long roomID);
}
