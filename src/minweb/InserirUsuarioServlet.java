package minweb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import minweb.base.dao.DAOFactory;
import minweb.dao.UsuarioDAO;
import minweb.modelo.Usuario;

@WebServlet("/inserir-usuario")
public class InserirUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = -1132939712512861733L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Usuario user = new Usuario();
		
		user.setUsername(req.getParameter("u").trim());
		user.setPassword(req.getParameter("p").trim());
		
		DAOFactory.getDAO(UsuarioDAO.class).persist(user);
	}
}