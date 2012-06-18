package minweb.base.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import minweb.base.modelo.ObjetoBD;

public abstract class DAO<T extends ObjetoBD> {
	static EntityManagerFactory emf;
	
	static void createFactory() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("minweb");
		}
	}
	
	static void destroyFactory() {
		if (emf != null) {
			emf.close();
		}
	}
	
	protected EntityManager newEntityManager() {
		return emf.createEntityManager();
	}
	
	public void persist(T obj) {
		EntityManager em = newEntityManager();
		EntityTransaction et = em.getTransaction();
		
		try {
			et.begin();
			
			em.persist(obj);
			
			et.commit();
		} finally {
			if (et.isActive())
				et.rollback();
		}
	}
}