package ge.eathub.exceptions;

public class NotEnoughMoney extends RuntimeException {
    private final String username;

    public NotEnoughMoney(String username) {
        super("Not Enough Money | " + username);
        this.username = username;
    }


    public String getUsername() {
        return username;
    }
}
