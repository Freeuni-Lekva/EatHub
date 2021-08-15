package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.models.Room;
import ge.eathub.service.TransactionService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import static ge.eathub.listener.NameConstants.TRANSACTION_SERVICE;
import static ge.eathub.servlets.ServletCommons.checkUser;

@WebServlet(name = "TransactionServlet", value = "/transaction")
public class TransactionServlet extends HttpServlet {

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
        String time = request.getParameter("time");
        TransactionService transactionService = (TransactionService) getServletContext().getAttribute(TRANSACTION_SERVICE);
        if (transactionService.makePaymentForAll(user.getUserID(), room.getRoomID(), time)) {
            response.getWriter().println(user.getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String time = request.getParameter("time");
        TransactionService transactionService = (TransactionService) getServletContext().getAttribute(TRANSACTION_SERVICE);
        if (transactionService.splitBillForAll(user.getUserID(), room.getRoomID(), time)) {
            response.getWriter().println(user.getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
        }
    }
}
