package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.InvalidEmailException;
import ge.eathub.exceptions.InvalidUserPasswordException;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.exceptions.UserNotFoundException;
import ge.eathub.listener.NameConstants;
import ge.eathub.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private static final String LOGIN_PAGE = "/WEB-INF/login.jsp";
    private static final String USER_START_PAGE = "/WEB-INF/start.jsp";
    public static final String ERROR_ATTR = "LOGIN_ERROR";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserService userService = (UserService) getServletContext()
                .getAttribute(NameConstants.USER_SERVICE_DB_ATTR);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        logger.info("Login POST | name:%s".formatted(username));
        if (username.isBlank() || password.isBlank()) {
            request.setAttribute(ERROR_ATTR, "OOPS, Blank fields");
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
        } else {
            UserLoginDto user = new UserLoginDto(username, password);
            try { // TODO send proper errors
                UserDto usr = userService.loginUser(user);
                request.getSession().setAttribute(UserDto.ATTR, usr);
                request.getRequestDispatcher(USER_START_PAGE).forward(request, response);
            } catch (UserNotFoundException | InvalidUserPasswordException e) {
                request.setAttribute(ERROR_ATTR, e.getMessage());
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            }
        }
    }
}
