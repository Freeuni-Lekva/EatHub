package ge.eathub.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "RestaurantsServlet", value = "/restaurants")
public class RestaurantsServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(RegistrationServlet.class.getName());
    private static final String RESTAURANTS_PAGE = "/WEB-INF/restaurants.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("restaurants get");
        request.getRequestDispatcher(RESTAURANTS_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc = getServletContext();
//        Cart cart = (Cart) req.getSession().getAttribute("CART");
//        if (cart == null){
//            cart = new Cart();
//            req.getSession().setAttribute("CART", cart);
//        }
//        Product product = (Product) req.getSession().getAttribute("PROD");
//        cart.addInCart(product);
        request.getRequestDispatcher(RESTAURANTS_PAGE).forward(request, response);




    }
}
