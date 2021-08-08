package ge.eathub.security;

import ge.eathub.dto.UserDto;
import ge.eathub.models.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class AuthenticatedRequest extends HttpServletRequestWrapper {

    private final UserDto user;

    public AuthenticatedRequest(HttpServletRequest request, UserDto user) {
        super(request);
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return new UserPrincipal(user.getUsername(),user.getUserID());
    }
}
