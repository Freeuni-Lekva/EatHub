package ge.eathub.servlets;

import ge.eathub.security.AuthenticatedRequest;
import ge.eathub.security.Authenticator;
import ge.eathub.utils.AuthenticatorFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;


@WebFilter("/chat/*")
public class AccessTokenFilter implements Filter {

    public static final String TOKEN_NOT_FOUNT = "An access token is required to connect";
    public static final String INVALID_TOKEN = "Invalid access token";
    public static final String ACCESS_TOKEN = "access-token";

    private final Authenticator authenticator = AuthenticatorFactory.get();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getParameter(ACCESS_TOKEN);
        if (token == null || token.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, TOKEN_NOT_FOUNT);
            return;
        }
        HttpSession sess = request.getSession(false);
        if (sess == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Optional<String> optionalUsername = authenticator.getUsername(token);
        // TODO check room-id
        if (optionalUsername.isPresent()) {
            filterChain.doFilter(new AuthenticatedRequest(request, optionalUsername.get()), servletResponse);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, INVALID_TOKEN);
        }
    }


    @Override
    public void destroy() {

    }


}