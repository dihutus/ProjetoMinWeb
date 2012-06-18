package minweb.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import minweb.base.dao.DAO;
import minweb.modelo.Filme;

public class FilmeDAO extends DAO<Filme> {
	public List<Filme> getFilmesAfterDate(Date date) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Filme> query = cb.createQuery(Filme.class);
		Root<Filme> filme = query.from(Filme.class);
		Join<Filme, Date> join = filme.join("horarios");
		query.select(filme).where(cb.greaterThan(join, date)).distinct(true);
		
		return em.createQuery(query).getResultList();
	}
}
