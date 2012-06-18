package minweb.base.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import minweb.base.modelo.ObjetoBD;

public abstract class DAO<T extends ObjetoBD> {
	private static EntityManagerFactory emf;
	private static EntityManager em;
	
	static void init() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("minweb");
		}
		
		if (em == null) {
			em = emf.createEntityManager();
		}
	}
	
	static void deinit() {
		if (em != null) {
			em.close();
		}
		
		if (emf != null) {
			emf.close();
		}
	}
	
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public void persist(T obj) {
		EntityManager em = getEntityManager();
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