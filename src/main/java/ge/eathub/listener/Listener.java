package ge.eathub.listener;

import ge.eathub.dao.*;
import ge.eathub.dao.impl.*;
import ge.eathub.database.DBConnection;
import ge.eathub.service.impl.OrderServiceImpl;
import ge.eathub.service.impl.RoomServiceImpl;
import ge.eathub.service.impl.UserServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;

@WebListener
public class Listener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public Listener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        ServletContext sc = sce.getServletContext();
        DataSource ds = DBConnection.getMySqlDataSource();

        UserDao userDao = new MySqlUserDao(ds);
        sc.setAttribute(NameConstants.USER_SERVICE,
                new UserServiceImpl(userDao));

        RoomDao roomDao = new MySqlRoomDao(ds);
        sc.setAttribute(NameConstants.ORDER_SERVICE,
                new OrderServiceImpl(roomDao, userDao, new MySqlOrderDao(ds)));

        sc.setAttribute(NameConstants.ROOM_SERVICE,
                new RoomServiceImpl(roomDao, userDao));

        RestaurantDao restaurantDao = new MySqlRestaurantDao(ds);

        sc.setAttribute(NameConstants.RESTAURANT_DAO, restaurantDao);

        MealDao mealDao = new MySqlMealDao(ds);
        sce.getServletContext().setAttribute(NameConstants.MEAL_DAO, mealDao);


        ChatDao chatDao = new MySqlChatDao(ds);
        sc.setAttribute(NameConstants.CHAT_DAO, chatDao);
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
