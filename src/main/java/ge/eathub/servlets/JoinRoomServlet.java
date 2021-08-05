package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.security.Authenticator;
import ge.eathub.utils.AuthenticatorFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ge.eathub.servlets.ServletCommons.CONFIRM_PAGE;

@WebServlet(name = "JoinRoomServlet", value = "/join-room")
public class JoinRoomServlet extends HttpServlet {
    public static final String BASIC_AUTH = "Basic ";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (!user.getConfirmed()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.getRequestDispatcher(CONFIRM_PAGE).forward(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/join-room.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (!user.getConfirmed()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.getRequestDispatcher(CONFIRM_PAGE).forward(request, response);
            return;
        }
        try {
            Long roomID = Long.valueOf(request.getParameter("room-id"));
            //TODO CHECK ROOM WITH ROOM SERVICE
            response.setStatus(HttpServletResponse.SC_OK);
            Authenticator auth = AuthenticatorFactory.get();
            String token = auth.getAccessToken(user.getUsername());
            response.setHeader("Authorization", BASIC_AUTH + token);
            request.getRequestDispatcher("/WEB-INF/chat.jsp").forward(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain");
            response.getWriter().write("room for user ");
            e.printStackTrace();
        }
    }


}
