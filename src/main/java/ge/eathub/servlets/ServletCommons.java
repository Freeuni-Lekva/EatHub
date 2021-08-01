package ge.eathub.servlets;

import ge.eathub.dto.UserDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ServletCommons {
    public static final String USER_START_PAGE = "/WEB-INF/start.jsp";
    public static final String LOGIN_PAGE = "/WEB-INF/login.jsp";

    protected static boolean checkUserSession(HttpServletRequest request, HttpServletResponse response, String redirectPage) throws ServletException, IOException {
        HttpSession ses = request.getSession(false);
        if (ses != null){
            UserDto user = (UserDto) ses.getAttribute(UserDto.ATTR);
            if (user != null) {
                request.getRequestDispatcher(redirectPage).forward(request, response);
                return true;
            }
        }
        return false;
    }

}
