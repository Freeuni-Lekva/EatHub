package ge.eathub.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("username %s not found ".formatted(username));
    }
    public UserNotFoundException(Long userID) {
        super("userID %d not found ".formatted(userID));
    }
}
