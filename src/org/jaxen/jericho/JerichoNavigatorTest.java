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
import org.jaxen.XPath;

public class JerichoNavigatorTest {

	private static String testUrl = "http://www.imdb.com/title/tt1446714/";
	private static String[] xpaths = new String[] { "/html/body", "//body",
			"/html/body/../head", "/html/head/title/text()",
			"//div[@class='articlecontent']", "//div[@class]" };

	public static void main(String[] args) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(testUrl);
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
					for (Object obj : (List<?>)result) {
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