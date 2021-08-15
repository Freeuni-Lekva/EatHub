package ge.eathub.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ge.eathub.dto.OrderDto;
import ge.eathub.dto.UserDto;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Order;
import ge.eathub.models.Room;
import ge.eathub.service.impl.OrderServiceImpl;
import ge.eathub.utils.ObjectMapperFactory;

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

@WebServlet(name = "ChooseMealServlet", value = "/ChooseMeal")
public class ChooseMealServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(ChooseMealServlet.class.getName());
    private final static ObjectMapper mapper = ObjectMapperFactory.get();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        if (ServletCommons.checkUser(request, response, user)){
            return;
        }
        ServletContext sc = getServletContext();
        OrderServiceImpl orderService = (OrderServiceImpl) sc.getAttribute(NameConstants.ORDER_SERVICE);
        Room room = ((Room) request.getSession().getAttribute(Room.ATTR));
        List<OrderDto> chosenMeals = orderService.getChosenMeals(user.getUserID(),room.getRoomID());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        mapper.writer().writeValues(response.getWriter()).write(chosenMeals);
        response.flushBuffer();
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = (UserDto) request.getSession().getAttribute(UserDto.ATTR);
        if (ServletCommons.checkUser(request, response, user)){
            return;
        }
        List<Order> orders = mapper
                .createParser(request.getReader())
                .readValueAs(new TypeReference<List<Order>>() {});
        ServletContext sc = getServletContext();
        OrderServiceImpl orderService = (OrderServiceImpl) sc.getAttribute(NameConstants.ORDER_SERVICE);
        Room room = ((Room) request.getSession().getAttribute(Room.ATTR));
        logger.info(" Post " + user.getUsername()
                + " room " + room.getRoomID());
        // TODO rewrite again
        for (Order order : orders) {
            Integer quantity = order.getQuantity();
            logger.info(" meal ID " + order.getMealID() + ": quantity" + order.getQuantity());
            Optional<Order> orderOptional = orderService.getOrderByID(user.getUserID(), room.getRoomID(), order.getMealID());
            if (orderOptional.isPresent()) {
                if (quantity == 0) {
                    if (!orderService.removeOrder(orderOptional.get())) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                } else {
                    orderOptional.get().setQuantity(quantity);
                    Optional<Order> res = orderService.updateOrder(orderOptional.get());
                    if (res.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
            } else {
                if (quantity > 0) {
                    Optional<Order> res = orderService.addOrder(user.getUserID(), order.getMealID(), room.getRoomID(), quantity);
                    if (res.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
