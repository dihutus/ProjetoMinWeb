package minweb.modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import minweb.base.modelo.ObjetoBD;

@Entity
public class Filme extends ObjetoBD {
	@Basic
	private String titulo;
	@ElementCollection
	private List<String> generos;
	@Basic
	private String classificaçãoEtaria;
	@Basic
	private String resumo;
	@ElementCollection
	private List<String> elenco;
	@ElementCollection
	private List<String> diretores;
	@Basic(optional=false)
	private double avaliação;
	@Basic
	private int ano;
	@Basic
	private int duracao;
	@Basic
	private boolean dublado;
	@ElementCollection
	private List<Date> horarios;
	@Embedded
	private LocalExibicao localExibicao;
	
	public String getTitulo() {
		return titulo;
	}
	public List<String> getGeneros() {
		return generos;
	}
	public String getClassificaçãoEtaria() {
		return classificaçãoEtaria;
	}
	public String getResumo() {
		return resumo;
	}
	public List<String> getElenco() {
		return elenco;
	}
	public List<String> getDiretores() {
		return diretores;
	}
	public double getAvaliação() {
		return avaliação;
	}
	public int getAno() {
		return ano;
	}
	public int getDuracao() {
		return duracao;
	}
	public boolean isDublado() {
		return dublado;
	}
	public List<Date> getHorarios() {
		return horarios;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public void setGeneros(List<String> generos) {
		this.generos = generos;
	}
	public void setClassificaçãoEtaria(String classificaçãoEtaria) {
		this.classificaçãoEtaria = classificaçãoEtaria;
	}
	public void setResumo(String resumo) {
		this.resumo = resumo;
	}
	public void setElenco(List<String> elenco) {
		this.elenco = elenco;
	}
	public void setDiretores(List<String> diretores) {
		this.diretores = diretores;
	}
	public void setAvaliação(double avaliação) {
		this.avaliação = avaliação;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}
	public void setDublado(boolean dublado) {
		this.dublado = dublado;
	}
	public void setHorarios(List<Date> horarios) {
		this.horarios = horarios;
	}
	
	public LocalExibicao getLocalExibicao() {
		return localExibicao;
	}
	public void setLocalExibicao(LocalExibicao localExibicao) {
		this.localExibicao = localExibicao;
	}
}