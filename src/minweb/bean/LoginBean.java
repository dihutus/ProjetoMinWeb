package minweb.bean;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import minweb.base.bean.BaseBean;

@ViewScoped
public class LoginBean extends BaseBean {
	private static final long serialVersionUID = -9149727328431187976L;
	
	private String username;
	@ManagedProperty(value="#{usuarioBean}")
	private UsuarioBean usuarioBean;

	public String logar() {
		
		
		return null;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public UsuarioBean getUsuarioBean() {
		return usuarioBean;
	}
	public void setUsuarioBean(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}
}