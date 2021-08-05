package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.listener.NameConstants;
import ge.eathub.security.Authenticator;
import ge.eathub.service.UserService;
import ge.eathub.utils.AuthenticatorFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import static ge.eathub.servlets.ServletCommons.*;

@WebServlet(name = "ConfirmServlet", value = "/confirm")
public class ConfirmServlet extends HttpServlet {
    public static final String ERROR_ATTR = "confirm-error";
    public static final String CONFIRM_TOKEN = "confirm-token";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        String token = request.getParameter(CONFIRM_TOKEN);
        confirmToken(request, response, token);

    }

    private void confirmToken(HttpServletRequest request, HttpServletResponse response, String token)
            throws ServletException, IOException {
        UserService userService = (UserService) getServletContext()
                .getAttribute(NameConstants.USER_SERVICE_DB_ATTR);
        if (userService.confirmUserRegistration(token)) {
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            return;
        }
        request.setAttribute(ERROR_ATTR, "invalid token");
        request.getRequestDispatcher(CONFIRM_PAGE).forward(request, response);
    }
}
