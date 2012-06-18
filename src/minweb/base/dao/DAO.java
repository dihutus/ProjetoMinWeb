package minweb.base.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
	
	public T getById(int id) {
		EntityManager em = newEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(targetClass);
		
		Root<T> target = cq.from(targetClass);
		CriteriaQuery<T> select = cq.select(target).where(cb.equal(target.get("id"), id));
		
		return em.createQuery(select).getSingleResult();
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