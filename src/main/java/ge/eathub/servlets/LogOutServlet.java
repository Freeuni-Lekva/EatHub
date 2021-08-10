package ge.eathub.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ge.eathub.servlets.ServletCommons.INDEX_PAGE;

@WebServlet(name = "LogOutServlet", value = "/logout")
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletCommons.setEncoding(request, response);
        HttpSession sess = request.getSession(false);
        if (sess != null) {
            sess.invalidate();
        }
        request.getRequestDispatcher(INDEX_PAGE).forward(request, response);
    }
}
