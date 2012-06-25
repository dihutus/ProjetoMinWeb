package minweb.extracao;

import java.util.Calendar;
import java.util.List;

import minweb.dao.FilmeDAO;
import minweb.modelo.Filme;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import minweb.base.dao.DAOFactory;

@WebServlet("/extracao")
public class Extracao extends HttpServlet {

	private static final long serialVersionUID = -6417374018056890968L;

	public static int extrairDados(){

		try {
			List<Filme> cinema = ExtracaoGoogle.extrairGoogle();
			List<Filme> tv = null;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			
			for(int i = 0; i < 12; i++){
				if(tv == null){
					tv = ExtracaoHagah.extrair(cal.getTime());
				} else {
					List<Filme> temp = ExtracaoHagah.extrair(cal.getTime());
					for (Filme f : temp) {
						if(!tv.contains(f))
							tv.add(f);
					}
				}
				cal.set(Calendar.HOUR_OF_DAY, i*2);
			}
			List<Filme> filmesHoje = cinema;
			filmesHoje.addAll(tv);
			filmesHoje = ExtracaoIMDB.buscarFilmes(filmesHoje);
			FilmeDAO dao = DAOFactory.getDAO(FilmeDAO.class);
			for (Filme f : filmesHoje) {
				System.out.println(f.toString() + " " + f.getLocalExibicao().getLocal());
				dao.persist(f);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;//TODO
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		extrairDados();
	}
}