package minweb.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import minweb.base.dao.DAOFactory;
import minweb.dao.UsuarioDAO;
import minweb.modelo.Usuario;

@ManagedBean
@RequestScoped
public class ListaUsuariosBean implements Serializable {
	private static final long serialVersionUID = -3792294134026759149L; 
	
	private List<Usuario> users;
	
	public ListaUsuariosBean() {
		this.setUsers(DAOFactory.getDAO(UsuarioDAO.class).getUsers());
	}
	
	public List<Usuario> getUsers() {
		return users;
	}
	public void setUsers(List<Usuario> users) {
		this.users = users;
	}
}