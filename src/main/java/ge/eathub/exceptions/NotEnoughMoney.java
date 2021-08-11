package ge.eathub.exceptions;

public class NotEnoughMoney extends RuntimeException{
    public NotEnoughMoney (Long userID) { super("Not Enough Money or User Doesn't exist | " + userID);}
}
