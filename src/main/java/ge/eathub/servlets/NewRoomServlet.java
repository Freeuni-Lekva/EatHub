package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Room;
import ge.eathub.service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import static ge.eathub.servlets.ServletCommons.CONFIRM_PAGE;


@WebServlet(name = "NewRoomServlet", value = "/newRoom")
public class NewRoomServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(NewRoomServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        RoomService roomService = (RoomService) getServletContext()
                .getAttribute(NameConstants.ROOM_SERVICE);
        long restaurantID = Long.parseLong(request.getParameter("id"));
        logger.info("GET " + user.getUsername() + "  restaurantID " + restaurantID);
        Optional<Room> room = roomService.createRoom(user, restaurantID);
        if (room.isPresent()) {
            Room newRoom = room.get();
            logger.info("created room " + user.getUsername() + " room " + newRoom.getRoomID());
            request.getSession().setAttribute(Room.ATTR, newRoom);
            response.sendRedirect("/Room");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
