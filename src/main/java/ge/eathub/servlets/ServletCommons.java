package ge.eathub.servlets;

import ge.eathub.dto.UserDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ServletCommons {
    protected static final String USER_START_PAGE = "/WEB-INF/start.jsp";
    protected static final String ADMIN_PAGE = "/WEB-INF/admin.jsp";
    protected static final String LOGIN_PAGE = "/WEB-INF/login.jsp";
    protected static final String CONFIRM_PAGE = "/WEB-INF/confirm.jsp";
    protected static final String REGISTRATION_PAGE = "/WEB-INF/register.jsp";
    protected static final String NEW_ROOM_PAGE = "/WEB-INF/newRoom.jsp";
    protected static final String JOIN_ROOM_PAGE = "/WEB-INF/join-room.jsp";
    protected static final String ROOM_PAGE = "/WEB-INF/room.jsp";

    public static final String INDEX_PAGE = "index.jsp";


    protected static boolean checkUserSession(HttpServletRequest request, HttpServletResponse response, String URL) throws ServletException, IOException {
        HttpSession ses = request.getSession(false);
        if (ses != null) {
            UserDto user = (UserDto) ses.getAttribute(UserDto.ATTR);
            if (user != null) {
                response.sendRedirect(URL);
                return false;
            }
        }
        return true;
    }

    protected static boolean checkUser(HttpServletRequest request, HttpServletResponse response, UserDto user) throws ServletException, IOException {
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return true;
        }
        if (!user.getConfirmed()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.getRequestDispatcher(CONFIRM_PAGE).forward(request, response);
            return true;
        }
        return false;
    }


    protected static void setEncoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
    }

}
