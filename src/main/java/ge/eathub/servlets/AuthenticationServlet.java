package ge.eathub.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.eathub.dto.UserDto;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.exceptions.InvalidUserPasswordException;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.AccessToken;
import ge.eathub.security.Authenticator;
import ge.eathub.service.UserService;
import ge.eathub.utils.AuthenticatorFactory;
import ge.eathub.utils.ObjectMapperFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {

    private final ObjectMapper mapper = ObjectMapperFactory.get();

    private final Authenticator authenticator = AuthenticatorFactory.get();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserLoginDto credentials = mapper.readValue(request.getReader(), UserLoginDto.class);
        UserService userService = (UserService) getServletContext()
                .getAttribute(NameConstants.USER_SERVICE);
        try {
            UserDto userDto = userService.loginUser(credentials);
            String token = authenticator.getAccessToken(userDto.getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            mapper.writeValue(response.getWriter(), new AccessToken(token));
//            resp.sendRedirect("/chat.jsp");
        } catch (InvalidUserPasswordException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain");
            response.getWriter().write("Invalid credentials");
            e.printStackTrace();
        }
    }
}