package minweb.base.dao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DAOContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		DAO.init();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		DAO.deinit();
	}
}