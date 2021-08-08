package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.exceptions.InvalidUserPasswordException;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.AccessToken;
import ge.eathub.security.Authenticator;
import ge.eathub.service.UserService;
import ge.eathub.utils.AuthenticatorFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "JoinRoomServlet", value = "/join-room")
public class JoinRoomServlet extends HttpServlet {
    public static final Authenticator auth = AuthenticatorFactory.get();
    public static final String BASIC_AUTH = "Basic ";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute(UserDto.ATTR) != null) {
            request.getRequestDispatcher("/WEB-INF/join-room.jsp").forward(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserService userService = (UserService) getServletContext()
                .getAttribute(NameConstants.USER_SERVICE_DB_ATTR);
        UserDto usr = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        try {
            String username = request.getParameter("username");
            Long roomID = Long.valueOf(request.getParameter("room-id"));
            //TODO CHECK ROOM WITH ROOM SERVICE
            response.setStatus(HttpServletResponse.SC_OK);
            Authenticator auth = AuthenticatorFactory.get();
            String token = auth.getAccessToken(usr.getUsername());
            response.setHeader("Authorization", BASIC_AUTH + token);
            request.getRequestDispatcher("/WEB-INF/chat.jsp").forward(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain");
            response.getWriter().write("room for user ");
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
    }
}
