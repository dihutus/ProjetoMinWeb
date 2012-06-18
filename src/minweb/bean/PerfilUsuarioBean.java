package minweb.bean;

import java.io.Serializable;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import minweb.base.dao.DAOFactory;
import minweb.dao.UsuarioDAO;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@ManagedBean
@ViewScoped
public class PerfilUsuarioBean implements Serializable {
	private static final long serialVersionUID = 4655350772842869770L;

	// arquivo genres.list do IMDB
	private static final Set<String> generos = ImmutableSet.<String> builder().add(
			"Action", "Adventure", "Adult", "Animation", "Comedy", "Crime", "Documentary",
			"Drama", "Fantasy", "Family", "Film-Noir", "Horror", "Musical", "Mystery",
			"Romance", "Sci-Fi", "Short", "Thriller", "War", "Western"
		).build();

	@ManagedProperty("#{usuarioBean}")
	private UsuarioBean usuarioBean;

	private String generoSelecionado;
	private String generoUsuarioSelecionado;

	private Set<String> generosDisponiveis;
	
	public void adicionarGenero() {
		if (generosDisponiveis.contains(generoSelecionado)) {
			usuarioBean.getUser().getGeneros().add(generoSelecionado);
			generosDisponiveis.remove(generoSelecionado);
			DAOFactory.getDAO(UsuarioDAO.class).persist(usuarioBean.getUser());
		}
	}

	public void removerGenero() {
		if (usuarioBean.getUser().getGeneros().contains(generoUsuarioSelecionado)) {
			usuarioBean.getUser().getGeneros().remove(generoUsuarioSelecionado);
			generosDisponiveis.add(generoUsuarioSelecionado);
			DAOFactory.getDAO(UsuarioDAO.class).persist(usuarioBean.getUser());
		}
	}

	public UsuarioBean getUsuarioBean() {
		return usuarioBean;
	}

	public void setUsuarioBean(UsuarioBean usuario) {
		this.usuarioBean = usuario;
	}

	public String getGeneroSelecionado() {
		return generoSelecionado;
	}

	public void setGeneroSelecionado(String generoSelecionado) {
		this.generoSelecionado = generoSelecionado;
	}

	public String getGeneroUsuarioSelecionado() {
		return generoUsuarioSelecionado;
	}

	public void setGeneroUsuarioSelecionado(String generoUsuarioSelecionado) {
		this.generoUsuarioSelecionado = generoUsuarioSelecionado;
	}

	public Set<String> getGenerosDisponiveis() {
		if (generosDisponiveis == null) {
			generosDisponiveis = Sets.newHashSet(Sets.difference(generos, usuarioBean.getUser().getGeneros()));
		}
		return generosDisponiveis;
	}

	public void setGenerosDisponiveis(Set<String> generosDisponiveis) {
		this.generosDisponiveis = generosDisponiveis;
	}
}