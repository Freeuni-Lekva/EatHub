package ge.eathub.servlets;

import ge.eathub.dto.UserDto;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.exceptions.InvalidUserPasswordException;
import ge.eathub.exceptions.UserNotFoundException;
import ge.eathub.listener.NameConstants;
import ge.eathub.models.Role;
import ge.eathub.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

import static ge.eathub.servlets.ServletCommons.*;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(LoginServlet.class.getName());
    public static final String ERROR_ATTR = "LOGIN_ERROR";
    public static final String ADMIN_PAGE = "/WEB-INF/admin.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkUserSession(request, response, USER_START_PAGE)) {
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
        }
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
                if(usr.getRole().equals(Role.CUSTOMER)) {
                    request.getRequestDispatcher(USER_START_PAGE).forward(request, response);
                }else{
                    request.getRequestDispatcher(ADMIN_PAGE).forward(request, response);
                }
            } catch (UserNotFoundException | InvalidUserPasswordException e) {
                request.setAttribute(ERROR_ATTR, e.getMessage());
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            }
        }
    }
}
