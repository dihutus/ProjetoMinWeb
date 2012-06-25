package minweb.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import minweb.base.dao.DAOFactory;
import minweb.dao.FilmeDAO;
import minweb.modelo.Filme;

@ManagedBean
@ViewScoped
public class IndexBean implements Serializable {
	private static final long serialVersionUID = 6169525199604885451L;

	private List<Filme> filmes;
	
	public IndexBean() {
		setFilmes(DAOFactory.getDAO(FilmeDAO.class).getFilmesAfterDate(new Date()));
	}

	public List<Filme> getFilmes() {
		return filmes;
	}
	public void setFilmes(List<Filme> filmes) {
		this.filmes = filmes;
	}
}