package minweb.extracao.modelo;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import minweb.base.modelo.ObjetoBD;

@Entity
public class Documento extends ObjetoBD {
	@Column(nullable=false, unique=true)
	private String url;
	
	@Column(nullable=false)
	private Date dataExtracao;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(nullable=false)
	private String dados;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDataExtracao() {
		return dataExtracao;
	}
	public void setDataExtracao(Date dataExtracao) {
		this.dataExtracao = dataExtracao;
	}

	public String getDados() {
		return dados;
	}
	public void setDados(String dados) {
		this.dados = dados;
	}
}