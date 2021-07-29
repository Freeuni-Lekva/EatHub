package ge.eathub.listener;

import ge.eathub.dao.RestaurantDao;
import ge.eathub.dao.UserDao;
import ge.eathub.dao.impl.MySqlRestaurantDao;
import ge.eathub.dao.impl.MySqlUserDao;
import ge.eathub.database.DBConnection;
import ge.eathub.service.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;

@WebListener
public class Listener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public Listener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        DataSource ds = DBConnection.getMySqlDataSource();
        UserDao userDao = new MySqlUserDao(ds);  // change it vano iish.
        sce.getServletContext().setAttribute(NameConstants.USER_SERVICE_DB_ATTR,
                new UserServiceImpl(userDao));
        RestaurantDao restaurantDao = new MySqlRestaurantDao(ds);
        sce.getServletContext().setAttribute("RESTAURANT_DAO", restaurantDao); // change it
        //sce.getServletContext().setAttribute("CHEMI RAGACA", new chemiDao());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
