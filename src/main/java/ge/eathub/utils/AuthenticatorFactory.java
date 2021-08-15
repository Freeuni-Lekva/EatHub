package ge.eathub.utils;

import ge.eathub.security.Authenticator;

public class AuthenticatorFactory {

    private static final Authenticator auth = new Authenticator();

    public static Authenticator get() {
        return auth;
    }
}
