package ge.eathub.models;

import java.math.BigDecimal;
import java.sql.Time;

public class Transaction {
    public static final String TABLE = "transactions";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String USER_ID = "user_id";
    public static final String RESTAURANT_ID = "restaurant_id";
    public static final String ROOM_ID = "room_id";
    public static final String AMOUNT = "amount";
    public static final String TIME = "time";

    public long transactionID;
    public long userID;
    public long roomID;
    public BigDecimal amount;
    public Time time;

    public Transaction(long transactionID, long userID, long roomID, BigDecimal amount, Time time) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.roomID = roomID;
        this.amount = amount;
        this.time = time;
    }

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
