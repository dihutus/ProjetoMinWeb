package minweb.extracao;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import minweb.modelo.Filme;
import minweb.modelo.LocalExibicao;
import minweb.modelo.LocalExibicao.TipoLocal;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jericho.JerichoXPath;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ExtracaoGoogle {

	private static String testUrl = "http://www.google.com.br/movies";
	private static String divTheater = "//div[@class='theater']";
	private static String divMovie = "//div[@class='movie']";
	private static String theaterName = "//h2[@class='name']/a/text()";
	private static String[] movieData = new String[] { "//div[@class='name']/a/text()", 
		"//div[@class='times']/span/span/text()", "//span[@class='info']/text()"  };

	public static List<Filme> extrairGoogle () throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(testUrl);
		HttpParams params = new BasicHttpParams();
		params.setParameter("hl", "pt-BR");
		params.setParameter("near", "recife");
		get.setParams(params);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();

		InputStream in = entity.getContent();
		List<Filme> filmes = new ArrayList<Filme>();
		try {
			Source doc = new Source(in);
			doc.fullSequentialParse();
			XPath expr = new JerichoXPath(divTheater);
			Object result = expr.evaluate(doc);
			if(result instanceof List){
				for (Object obj : (List<?>)result){
					if(obj instanceof Element) {
						filmes.addAll(addTheater((Element)obj));
					}
				}
			} else if(result != null) {
				if(result instanceof Element) {
					filmes.addAll(addTheater((Element)result));
				}
			}
		} finally {
			in.close();
		}
		return filmes;
	}

	private static List<Filme> addTheater(Element elt) throws JaxenException {

		List<Filme> filmes = new ArrayList<Filme>();
		String theaterNam;
		XPath expr = new JerichoXPath(theaterName);
		Object result = expr.evaluate(elt);
		if(result instanceof String){
			theaterNam = (String)result;
		} else {
			theaterNam = "";
		}
		LocalExibicao local = new LocalExibicao(theaterNam, TipoLocal.CINEMA);
		expr = new JerichoXPath(divMovie);
		result = expr.evaluate(elt);
		Filme temp;
		if(result instanceof List){
			for (Object obj : (List<?>)result){
				if(obj instanceof Element) {
					temp = addMovie((Element)obj, theaterNam);
					temp.setLocalExibicao(local);
					filmes.add(temp);
				}
			}
		} else if(result != null) {
			if(result instanceof Element) {
				temp = addMovie((Element)result, theaterNam);
				temp.setLocalExibicao(local);
				filmes.add(temp);
			}
		}
		return filmes;
	}

	private static Filme addMovie(Element elt, String theaterName) throws JaxenException {

		Filme filme = new Filme();
		for (String xpath : movieData) {
			XPath expr = new JerichoXPath(xpath);
			Object result = expr.evaluate(elt);

			if (result instanceof List) {
				result = Lists.newArrayList(Iterables.filter((List<?>)result, new Predicate<Object>() {
					public boolean apply(Object arg0) {
						return ((String)arg0).length() > 5;
					}
				}));
				List<Date> horarios = new ArrayList<Date>();
				for(String str : (List<String>)result){
					int i = str.indexOf(':');
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(i - 2, i)));
					cal.set(Calendar.MINUTE, Integer.parseInt(str.substring(i+1, i+3)));
					cal.set(Calendar.SECOND, 0);
					horarios.add(cal.getTime());
				}
				filme.setHorarios(horarios);
			} else if (result instanceof String) {
				if(((String) result).endsWith("‎Verifique a Classificação‎‎ -")) {
					filme.setClassificaçãoEtaria("Não especificada");
				} else if (((String) result).contains("anos")){
					int i = ((String) result).indexOf("anos");
					filme.setClassificaçãoEtaria(((String) result).substring(i - 3, i + 4));
				} else if(((String) result).contains("Livre")) {
					filme.setClassificaçãoEtaria("Livre");
				} else {
					int i = ((String) result).indexOf('-');
					String nome = ((String) result).substring(0, i-1);
					boolean dublado = ((String) result).endsWith("Dublado");
					filme.setTitulo(nome);
					filme.setDublado(dublado);
				}
			}
		}
		return filme;
	}
}