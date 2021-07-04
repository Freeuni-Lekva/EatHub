package ge.eathub.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String mail) {
        super("Invalid mail " + mail);
    }
}
