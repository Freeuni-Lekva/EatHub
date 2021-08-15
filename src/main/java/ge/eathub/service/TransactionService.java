package ge.eathub.service;

public interface TransactionService {


    boolean makePaymentForAll(Long userID, Long roomID, String time);

    // make all transactions for users in room
    boolean splitBillForAll(Long userID, Long roomID, String time);

    // make transactions only for user in room
    boolean payOnlyForUser(Long userID, Long roomID);
}
