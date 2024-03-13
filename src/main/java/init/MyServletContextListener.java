package init;

import database.H2DatabaseManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        H2DatabaseManager h2DatabaseManager = new H2DatabaseManager();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
