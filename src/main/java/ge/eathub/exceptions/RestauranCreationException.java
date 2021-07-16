package ge.eathub.exceptions;

public class RestauranCreationException extends RuntimeException{
    public RestauranCreationException(String errorMessage) {
        super("Could not create restaurant | " + errorMessage);
    }
}
