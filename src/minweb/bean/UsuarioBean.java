package minweb.bean;

import javax.faces.bean.SessionScoped;

import minweb.base.bean.BaseBean;
import minweb.modelo.Usuario;

@SessionScoped
public class UsuarioBean extends BaseBean {
	private static final long serialVersionUID = 5403942861628367161L;
	
	private Usuario user;

	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}
}