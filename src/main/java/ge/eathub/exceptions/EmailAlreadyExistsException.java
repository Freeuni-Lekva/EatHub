package ge.eathub.exceptions;

public class EmailAlreadyExistsException extends UserCreationException {
    public EmailAlreadyExistsException(String email) {
        super("Email: %s already exists ".formatted(email));
    }
}
