package ge.eathub.exceptions;

public class MealAlreadyExistsException extends RuntimeException {
    public MealAlreadyExistsException(String errorMessage) {
        super("Meal already exists | " + errorMessage);
    }

}
