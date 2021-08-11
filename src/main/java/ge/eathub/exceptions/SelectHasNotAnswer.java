package ge.eathub.exceptions;

public class SelectHasNotAnswer extends RuntimeException{
    public SelectHasNotAnswer (String errorMessage) { super("Select has no answer | " + errorMessage);}
}
