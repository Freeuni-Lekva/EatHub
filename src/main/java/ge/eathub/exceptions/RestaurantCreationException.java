package ge.eathub.exceptions;

public class RestaurantCreationException extends RuntimeException{
    public RestaurantCreationException(String errorMessage) {
        super("Could not create restaurant | " + errorMessage);
    }
}
