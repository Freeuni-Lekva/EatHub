package ge.eathub.exceptions;

public class InvalidUserPasswordException extends Throwable {
    public InvalidUserPasswordException(String username) {
        super("Invalid password for " + username);
    }
}
