package ge.eathub.servlets;

import ge.eathub.dto.UserDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ServletCommons {
    public static final String USER_START_PAGE = "/WEB-INF/start.jsp";
    public static final String ADMIN_PAGE = "/WEB-INF/admin.jsp";
    public static final String LOGIN_PAGE = "/WEB-INF/login.jsp";
    public static final String CONFIRM_PAGE = "/WEB-INF/confirm.jsp";
    public static final String REGISTRATION_PAGE = "/WEB-INF/register.jsp";
    public static final String NEW_ROOM_PAGE = "/WEB-INF/newRoom.jsp";
    public static final String INDEX_PAGE = "index.jsp";



    protected static boolean checkUserSession(HttpServletRequest request, HttpServletResponse response, String redirectPage) throws ServletException, IOException {
        HttpSession ses = request.getSession(false);
        if (ses != null) {
            UserDto user = (UserDto) ses.getAttribute(UserDto.ATTR);
            if (user != null) {
                request.getRequestDispatcher(redirectPage).forward(request, response);
                return true;
            }
        }
        return false;
    }

}
