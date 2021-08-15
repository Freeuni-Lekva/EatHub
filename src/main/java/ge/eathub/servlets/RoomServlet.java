package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.models.Room;
import ge.eathub.service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ge.eathub.listener.NameConstants.ROOM_SERVICE;
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
        RoomService roomService = (RoomService) getServletContext().getAttribute(ROOM_SERVICE);
        Room room = ((Room) request.getSession().getAttribute(Room.ATTR));
        if (room == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (!roomService.isRoomActive(room.getRoomID())) {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }
        request.getRequestDispatcher(NEW_ROOM_PAGE).forward(request, response);
    }

}
