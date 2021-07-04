package ge.eathub.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("username %s not found ".formatted(username));
    }
}
