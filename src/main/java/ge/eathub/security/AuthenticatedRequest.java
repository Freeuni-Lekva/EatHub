package ge.eathub.security;

import ge.eathub.models.User;
import ge.eathub.models.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class AuthenticatedRequest extends HttpServletRequestWrapper {

    private final String username;

    public AuthenticatedRequest(HttpServletRequest request, String username) {
        super(request);
        this.username = username;
    }

    @Override
    public Principal getUserPrincipal() {
        return new UserPrincipal(username);
    }
}
