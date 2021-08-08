package ge.eathub.servlets;

import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.InvalidEmailException;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.listener.NameConstants;
import ge.eathub.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

import static ge.eathub.servlets.ServletCommons.*;

@WebServlet(name = "RegistrationServlet", value = "/register")
public class RegistrationServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(RegistrationServlet.class.getName());
    private static final String REGISTRATION_PAGE = "/WEB-INF/register.jsp";
    public static final String ERROR_ATTR = "REG_ERROR";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Registration get");
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        if (!checkUserSession(request, response, USER_START_PAGE)) {
            request.getRequestDispatcher(REGISTRATION_PAGE).forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserService userService = (UserService) getServletContext()
                .getAttribute(NameConstants.USER_SERVICE_DB_ATTR);
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        logger.info("Registration POST | name:%s , email: %s".formatted(username, email));
        if (username.isBlank() || password.isBlank() || email.isBlank()) {
            request.setAttribute(ERROR_ATTR, "OOPS, Blank fields");
            request.getRequestDispatcher(REGISTRATION_PAGE).forward(request, response);
        } else {
            UserRegisterDto user = new UserRegisterDto(username, password, email);
            try { // TODO send proper errors
                userService.registerUser(user);
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            } catch (InvalidEmailException | UserCreationException e) {
                request.setAttribute(ERROR_ATTR, e.getMessage());
                request.getRequestDispatcher(REGISTRATION_PAGE).forward(request, response);
            }

        }

    }

}
