package ge.eathub.exceptions;

public class MealCreationException extends RuntimeException {
    public MealCreationException(String errorMessage) {
        super("Could not create meal | " + errorMessage);
    }
}
