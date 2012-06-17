package minweb.modelo;

import javax.persistence.Basic;
import javax.persistence.Entity;

import minweb.base.modelo.ObjetoBD;

@Entity
public class LocalExibicao extends ObjetoBD {
	public enum TipoLocal {
		CINEMA,
		TV
	}
	
	@Basic(optional=false)
	private String local;
	@Basic(optional=false)
	private TipoLocal tipo;
	
	public LocalExibicao(String local, TipoLocal tipo) {
		this.local = local;
		this.tipo = tipo;
	}

	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}

	public TipoLocal getTipo() {
		return tipo;
	}
	public void setTipo(TipoLocal tipo) {
		this.tipo = tipo;
	}
}