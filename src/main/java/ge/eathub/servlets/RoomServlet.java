package ge.eathub.servlets;

import ge.eathub.dto.UserDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ge.eathub.servlets.ServletCommons.NEW_ROOM_PAGE;
import static ge.eathub.servlets.ServletCommons.checkUser;

@WebServlet(name = "RoomRedirectionServlet", value = "/Room")
public class RoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        if (checkUser(request, response, user)) {
            return;
        }
        request.getRequestDispatcher(NEW_ROOM_PAGE).forward(request, response);
    }

}
