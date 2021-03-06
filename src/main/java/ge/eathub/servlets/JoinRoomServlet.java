package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Room;
import ge.eathub.security.Authenticator;
import ge.eathub.service.RoomService;
import ge.eathub.utils.AuthenticatorFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import static ge.eathub.servlets.ServletCommons.*;

@WebServlet(name = "JoinRoomServlet", value = "/join-room")
public class JoinRoomServlet extends HttpServlet {
    public static final String BASIC_AUTH = "Basic ";
    private final static Logger logger = Logger.getLogger(JoinRoomServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        if (checkUser(request, response, user)) {
            return;
        }
        logger.info("GET " + user.getUsername());
        request.getRequestDispatcher(JOIN_ROOM_PAGE).forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        if (checkUser(request, response, user)) {
            return;
        }
        logger.info("POST " + user.getUsername());
        try {
            Long roomID = Long.valueOf(request.getParameter("room-id"));
            RoomService roomService = (RoomService) getServletContext().getAttribute(NameConstants.ROOM_SERVICE);
            if (roomService.checkUser(roomID, user.getUserID())) {
                response.setStatus(HttpServletResponse.SC_OK);
                Authenticator auth = AuthenticatorFactory.get();
                String token = auth.getAccessToken(user.getUsername());
                response.setHeader("Authorization", BASIC_AUTH + token);
                Optional<Room> room = roomService.getRoomByID(roomID);
                if (room.isEmpty()) {
                    logger.warning("empty room " + user.getUsername() + " roomID " + roomID);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                logger.info("OK " + user.getUsername() + " roomID " + roomID);
                request.getSession().setAttribute(Room.ATTR, room.get());
                request.getRequestDispatcher(ROOM_PAGE).forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain");
            response.getWriter().write("room for user ");
        }
    }


}
