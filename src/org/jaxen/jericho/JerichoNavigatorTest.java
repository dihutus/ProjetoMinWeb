package org.jaxen.jericho;

import java.io.InputStream;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.jaxen.XPath;

public class JerichoNavigatorTest {

	private static String testUrl = "http://www.hagah.com.br/programacao-tv/jsp/default.jspx";
	private static String[] xpaths = new String[] {
		"//div[@class='miolo']",
			
	};

	public static void main(String[] args) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(testUrl);
		
		HttpParams params = get.getParams();
		
		params.setIntParameter("uf", 26);
		params.setIntParameter("operadora", 15);
		params.setParameter("data", "16/06/2012");
		params.setParameter("genero", "Filme");
		
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();

		InputStream in = entity.getContent();
		try {
			Source doc = new Source(in);
			doc.fullSequentialParse();
			for (String xpath : xpaths) {
				XPath expr = new JerichoXPath(xpath);
				Object result = expr.evaluate(doc);

				
				if (result instanceof Element) {
					System.out.println(((Element)result).getName());
				} else if (result instanceof List) {
					System.out.println(((List<?>)result).size());
					for (Object obj : (List<?>)result) {
						System.out.println(obj);
						if (obj instanceof Element) {
						} else if (obj instanceof String) {
						} else if (obj instanceof Number) {
						} else if (obj instanceof Boolean) {
						} else {
							// n sei se pode
						}
					}
				} else if (result instanceof String) {
				} else if (result instanceof Number) {
				} else if (result instanceof Boolean) {
				} else {
					// se for nulo ele entra aqui
				}
			}
		} finally {
			in.close();
		}
	}
}