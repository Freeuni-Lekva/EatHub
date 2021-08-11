package ge.eathub.servlets;

import ge.eathub.dao.impl.MySqlRestaurantDao;
import ge.eathub.dto.UserDto;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Meal;
import ge.eathub.models.Order;
import ge.eathub.models.Room;
import ge.eathub.service.RoomService;
import ge.eathub.service.impl.OrderServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
            request.getRequestDispatcher(ServletCommons.NEW_ROOM_PAGE).forward(request, response);
        } else {
            // TODO: ERROR NOTIFICATION
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc = getServletContext();
        OrderServiceImpl orderService = (OrderServiceImpl) sc.getAttribute(NameConstants.ORDER_SERVICE);
        Room room = ((Room) request.getSession().getAttribute(Room.ATTR));
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        MySqlRestaurantDao dao = (MySqlRestaurantDao) sc.getAttribute(NameConstants.RESTAURANT_DAO);
        long restaurantID = room.getRestaurantID();
        List<Meal> meals = dao.getAllMeals(restaurantID);
        List<Order> orders = orderService.getAll(user.getUserID(), room.getRoomID());
        logger.info(" Post " + user.getUsername()
                + " room " + room.getRoomID());
        for (Meal meal : meals) {
            int quantity = Integer.parseInt(request.getParameter(meal.getMealID().toString()));
            logger.info(" meal ID " + meal.getMealID() + ": quantity" + quantity);
            Optional<Order> order = orderService.getOrderByID(user.getUserID(), room.getRoomID(), meal.getMealID());
            if (order.isPresent()) {
                if (quantity == 0) {
                    orderService.removeOrder(order.get());
                } else {
                    order.get().setQuantity(quantity);
                    orderService.updateOrder(order.get());
                }
            } else {
                if (quantity > 0) {
                    orderService.addOrder(user.getUserID(), meal.getMealID(), room.getRoomID(), quantity);
                }
            }
        }
        logger.info("OK " + user.getUsername() + " roomID " + room.getRoomID());
        request.setAttribute(NameConstants.ORDER_SERVICE, orderService);
        request.getRequestDispatcher(ServletCommons.NEW_ROOM_PAGE).forward(request, response);
    }
}
