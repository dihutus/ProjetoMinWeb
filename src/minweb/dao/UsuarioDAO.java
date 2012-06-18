package minweb.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import minweb.base.dao.DAO;
import minweb.modelo.Usuario;

public class UsuarioDAO extends DAO<Usuario> {
	public Usuario getUser(String username) {
		EntityManager em = newEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
		Root<Usuario> user = query.from(Usuario.class);
		
		query.select(user).where(cb.equal(user.get("username"), username));
		
		List<Usuario> users = em.createQuery(query).getResultList();
		return (users.isEmpty()) ? null : users.get(0);
	}
}