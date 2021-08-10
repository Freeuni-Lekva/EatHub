package ge.eathub.models;

import java.security.Principal;

public class UserPrincipal implements Principal {

    private final String username;
    private final Long userID;

    public UserPrincipal(String username, Long userID) {
        this.username = username;
        this.userID = userID;
    }

    @Override
    public String getName() {
        return username;
    }

    public Long getUserID() {
        return userID;
    }
}
