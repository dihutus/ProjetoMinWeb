package minweb.modelo;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import minweb.base.modelo.ObjetoBD;

@Entity
public class Usuario extends ObjetoBD {
	private static final long serialVersionUID = 8925998844841597249L;
	
	@Basic(optional=false)
	@Column(unique=true)
	private String username;
	@Basic(optional=false)
	private String password;
	
	@ElementCollection(fetch=FetchType.LAZY)
	private List<String> generos;

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
	
	public List<String> getGeneros() {
		return generos;
	}
	public void setGeneros(List<String> generos) {
		this.generos = generos;
	}
}