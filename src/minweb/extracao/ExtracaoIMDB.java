package minweb.extracao;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import minweb.modelo.Filme;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jericho.JerichoXPath;

public class ExtracaoIMDB {

	private static String testUrl = "http://www.imdb.com/find";

	private static String divMain = "//div[@id='main']";
	private static String divMainTables = "//div[@id='main']/table/tr/td/a";

	private static String ano = "//h1[@class='header']/span/a/text()";
	private static String avaliacao = "//div[@itemprop='aggregateRating']/div[@class='star-box-giga-star']/text()";
	private static String diretorElenco = "//td[@id='overview-top']/div[@class='txt-block']";
	private static String duracao = "//div[@class='infobar']/text()";
	private static String generos = "//div[@class='infobar']/a";
	private static String resumo = "//p[@itemprop='description']/text()";

	public static List<Filme> buscarFilmes (List<Filme> filmes) throws Exception {

		List<Filme> retorno = new ArrayList<Filme>();
		for(Filme fil: filmes) {
			retorno.add(buscarFilme(fil));
		}
		return retorno;
	}

	private static Filme buscarFilme(Filme filme) throws Exception {

		HttpClient client = new DefaultHttpClient();
		URIBuilder builder = new URIBuilder(testUrl);
		builder.addParameter("q", filme.getTitulo());
		builder.addParameter("s", "all");
		HttpGet get = new HttpGet(builder.build());
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		Source doc = new Source(in);
		doc.fullSequentialParse();
		XPath expr = new JerichoXPath(divMain);
		Object result = expr.evaluate(doc);

		if(result instanceof List) {
			if (((List<?>) result).size() > 0) {
				Element elt = (Element)((List<?>) result).get(0);
				doc = elt.getSource();
				doc.fullSequentialParse();
				expr = new JerichoXPath(divMainTables);
				result = expr.evaluate(doc);
				if(result instanceof List) {
					Element table = (Element)((List<?>) result).get(1);
				}
				
				
				
			} else {
				detalhesFilme(filme, doc);
			}
		}
		return filme;
	}

	private static void detalhesFilme(Filme filme, Source doc)
			throws JaxenException {
		XPath expr;
		Object result;
		expr = new JerichoXPath(ano);
		result = expr.evaluate(doc);
		filme.setAno(Integer.parseInt((String)result));

		expr = new JerichoXPath(avaliacao);
		result = expr.evaluate(doc);
		filme.setAvaliação(Double.parseDouble((String)result));

		expr = new JerichoXPath(diretorElenco);
		result = expr.evaluate(doc);
		if(result instanceof List) {
			for (Object obj : (List<?>)result){
				if(obj instanceof Element) {
					String desc = ((Element)obj).getChildElements().get(0).getContent().toString();
					if (desc.contains("Director")) {
						List<String> diretores = new ArrayList<String>();
						List<Element> le = ((Element)obj).getChildElements();
						for (Element elt : le) {
							if(elt.getEndTag().toString().contains("</a>")) {
								String s = elt.getContent().toString().trim();
								if(!s.contains("more credit")){
									diretores.add(s);
								}
							}
						}
						filme.setDiretores(diretores);
					} else if (desc.contains("Star")) {
						List<String> atores = new ArrayList<String>();
						List<Element> le = ((Element)obj).getChildElements();
						for (Element elt : le) {
							if(elt.getEndTag().toString().contains("</a>")) {
								String s = elt.getContent().toString().trim();
								if(!s.contains("full cast")){
									atores.add(s);
								}
							}
						}
						filme.setElenco(atores);
					}
				}
			}

			expr = new JerichoXPath(duracao);
			result = expr.evaluate(doc);
			int i = ((String)result).indexOf("min");
			filme.setDuracao(Integer.parseInt(((String)result).substring(0, i-1)));

			expr = new JerichoXPath(generos);
			result = expr.evaluate(doc);
			if(result instanceof List) {
				List<String> generos = new ArrayList<String>();
				for (Object obj : (List<?>)result){
					if(obj instanceof Element) {
						String desc = ((Element)obj).getAttributeValue("href").toString();
						if (!desc.contains("title")) {
							String s = ((Element)obj).getContent().toString().trim();
							generos.add(s);
						}
					}
				}
				filme.setGeneros(generos);
			}

			expr = new JerichoXPath(resumo);
			result = expr.evaluate(doc);
			filme.setResumo((String)result);
		}
	}

	public static void main(String[] args) {
		try {
			buscarFilmes(ExtracaoGoogle.extrairGoogle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}