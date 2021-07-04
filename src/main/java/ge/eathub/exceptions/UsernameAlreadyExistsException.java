package ge.eathub.exceptions;

public class UsernameAlreadyExistsException extends UserCreationException {

    public UsernameAlreadyExistsException(String username) {
        super("username: %s already exists ".formatted(username));
    }
}
