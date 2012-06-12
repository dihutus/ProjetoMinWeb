package minweb.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;

import minweb.base.modelo.ObjetoBD;

@Entity
public class Filme extends ObjetoBD {
	@Column(nullable=false)
	private String nome;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}