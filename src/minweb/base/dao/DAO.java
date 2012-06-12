package minweb.base.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import minweb.base.modelo.ObjetoBD;

public abstract class DAO<T extends ObjetoBD> {
	static EntityManagerFactory emf;
	
	protected final Class<T> targetClass;
	
	protected DAO(Class<T> targetClass) {
		this.targetClass = targetClass;
	}
	
	protected EntityManager newEntityManager() {
		return emf.createEntityManager();
	}
}