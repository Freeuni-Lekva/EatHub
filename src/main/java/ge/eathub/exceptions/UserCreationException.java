package ge.eathub.exceptions;

public class UserCreationException extends RuntimeException {
    public UserCreationException(String errorMessage) {
        super("Could not create user | " + errorMessage);
    }
}
