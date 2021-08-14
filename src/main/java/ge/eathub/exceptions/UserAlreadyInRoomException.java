package ge.eathub.exceptions;

public class UserAlreadyInRoomException extends RuntimeException{
    public UserAlreadyInRoomException (String errorMessage) { super("User already in room with ID: | " + errorMessage);}

}
