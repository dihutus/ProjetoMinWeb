package minweb.base.dao;

import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DAOContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		DAO.emf = Persistence.createEntityManagerFactory("minweb");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		DAO.emf.close();
	}
}