package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.models.Room;
import ge.eathub.service.RoomService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.logging.Logger;

import static ge.eathub.listener.NameConstants.ROOM_SERVICE;
import static ge.eathub.servlets.ServletCommons.checkUser;

@WebServlet(name = "InvitationServlet", value = "/invite")
public class InvitationServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(JoinRoomServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        if (checkUser(request, response, user)) {
            return;
        }
        Room room = (Room) request.getSession().getAttribute(Room.ATTR);
        if (room == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String username = request.getParameter("username");
        logger.info("POST " + user.getUsername() + " invited " + username);
        RoomService roomService = (RoomService) getServletContext().getAttribute(ROOM_SERVICE);
        if (roomService.inviteUser(user.getUsername(), username,room.getRoomID())) {
            logger.info("sent invitation");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
