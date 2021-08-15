package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.exceptions.UserAlreadyInRoomException;
import ge.eathub.exceptions.UserNotFoundException;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Room;
import ge.eathub.service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String username = request.getParameter("username");
        logger.info("POST " + user.getUsername() + " invited " + username);
        try {
            roomService.inviteUser(user.getUsername(), username, room.getRoomID());
            logger.info("sent invitation");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (UserAlreadyInRoomException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } catch (UserNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
