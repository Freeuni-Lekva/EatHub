package ge.eathub.exceptions;

public class UserCreationException extends RuntimeException {
    public UserCreationException(String username) {
        super("could not create user: " + username);
    }
}
