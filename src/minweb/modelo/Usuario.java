package minweb.modelo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import minweb.base.modelo.ObjetoBD;

@Entity
public class Usuario extends ObjetoBD {
	@Column(nullable=false, unique=true)
	private String username;
	
	@ElementCollection(fetch=FetchType.LAZY)
	private List<String> generos;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public List<String> getGeneros() {
		return generos;
	}
	public void setGeneros(List<String> generos) {
		this.generos = generos;
	}
}