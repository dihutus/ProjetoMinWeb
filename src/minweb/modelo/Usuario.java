package minweb.modelo;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import com.google.common.collect.Sets;

import minweb.base.modelo.ObjetoBD;

@Entity
public class Usuario extends ObjetoBD {
	private static final long serialVersionUID = 8925998844841597249L;
	
	@Basic(optional=false)
	@Column(unique=true)
	private String username;
	@Basic(optional=false)
	private String password;
	
	@Column(nullable=false)
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> generos = Sets.newHashSet();
	
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
	
	public Set<String> getGeneros() {
		return generos;
	}
	public void setGeneros(Set<String> generos) {
		this.generos = generos;
	}
}