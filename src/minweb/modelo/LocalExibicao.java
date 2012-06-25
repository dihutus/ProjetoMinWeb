package minweb.modelo;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class LocalExibicao {
	public enum TipoLocal {
		CINEMA,
		TV
	}
	
	@Basic
	private String local;
	@Basic
	private TipoLocal tipo;
	
	public LocalExibicao() {
	}
	
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