package ge.eathub.servlets;

import ge.eathub.listener.NameConstants;
import ge.eathub.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ge.eathub.servlets.ServletCommons.CONFIRM_PAGE;
import static ge.eathub.servlets.ServletCommons.LOGIN_PAGE;

@WebServlet(name = "ConfirmServlet", value = "/confirm")
public class ConfirmServlet extends HttpServlet {
    public static final String ERROR_ATTR = "confirm-error";
    public static final String CONFIRM_TOKEN = "confirm-token";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        String token = request.getParameter(CONFIRM_TOKEN);
        if (token == null) {
            request.getRequestDispatcher(CONFIRM_PAGE).forward(request, response);
            return;
        }
        confirmToken(request, response, token);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        String token = request.getParameter(CONFIRM_TOKEN);
        confirmToken(request, response, token);

    }

    private void confirmToken(HttpServletRequest request, HttpServletResponse response, String token)
            throws ServletException, IOException {
        UserService userService = (UserService) getServletContext()
                .getAttribute(NameConstants.USER_SERVICE);
        if (userService.confirmUserRegistration(token)) {
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            return;
        }
        request.setAttribute(ERROR_ATTR, "invalid token");
        request.getRequestDispatcher(CONFIRM_PAGE).forward(request, response);
    }
}
