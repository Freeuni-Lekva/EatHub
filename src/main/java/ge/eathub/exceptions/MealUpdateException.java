package ge.eathub.exceptions;

public class MealUpdateException extends RuntimeException {
    public MealUpdateException(String errorMessage) {
        super("Could not update meal | " + errorMessage);
    }
}
