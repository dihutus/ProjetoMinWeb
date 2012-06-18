package minweb.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import minweb.base.dao.DAOFactory;
import minweb.dao.UsuarioDAO;
import minweb.modelo.Usuario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = -9149727328431187976L;
	private static final Log log = LogFactory.getLog(LoginBean.class);
	
	private String username;
	private String password;
	
	private String errorMessage;
	
	@ManagedProperty("#{usuarioBean}")
	private UsuarioBean usuarioBean;
	
	public void logar() {
		if (usuarioBean.getUser() == null) {
			Usuario user = DAOFactory.getDAO(UsuarioDAO.class).getUser(username);
			if (user != null && user.getPassword().equals(password)) {
				log.info(String.format("User '%s' logging with password '%s'", username, password));
				usuarioBean.setUser(user);
				errorMessage = null;
			} else {
				errorMessage = "Login e/ou Senha incorretos";
			}
		}
	}
	
	public void deslogar() {
		if (usuarioBean.getUser() != null) {
			usuarioBean.setUser(null);
		}
		errorMessage = null;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public UsuarioBean getUsuarioBean() {
		return usuarioBean;
	}
	public void setUsuarioBean(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}
}