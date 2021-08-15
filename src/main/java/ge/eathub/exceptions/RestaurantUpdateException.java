package ge.eathub.exceptions;

public class RestaurantUpdateException extends RuntimeException {
    public RestaurantUpdateException(String errorMessage) {
        super("Could not update restaurant | " + errorMessage);
    }
}
