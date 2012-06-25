package minweb.extracao;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import minweb.modelo.Filme;
import minweb.modelo.LocalExibicao;
import minweb.modelo.LocalExibicao.TipoLocal;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jericho.JerichoXPath;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ExtracaoHagah {
	private static final Log log = LogFactory.getLog(ExtracaoHagah.class);

	private static final Locale PT_BR = new Locale("pt", "BR");

	private static final String URL_HAGAH_TV = "http://www.hagah.com.br/programacao-tv/jsp/";
	private static final String URL_HAGAH_TV_PAGINA = "default.jsp?uf=26&operadora=15&genero=Filme";
	
	private static final XPath xpath_trs_emissoras =
			newXPath("//table[@id='grupo1']/tbody/tr[not(contains(@class, 'limpa'))]");
	private static final XPath xpath_nome_emissora =
			newXPath("string(td[contains(@class, 'nome')]/a)");
	private static final XPath xpath_filmes =
			newXPath("td[contains(@class, 'programa')][contains(@class, 'filme')]/a[@title]/@href");
	
	private static final XPath xpath_div_filme =
			newXPath("//div[contains(@class, 'conteudo')][1]");
	private static final XPath xpath_nome_filme =
			newXPath("string(h1[contains(@class, 'programa')])");
	private static final XPath xpath_data_filme =
			newXPath("string(ul[contains(@class, 'horario')]/li[1])");
	private static final XPath xpath_horario_filme =
			newXPath("string(ul[contains(@class, 'horario')]/li[2])");

	
	private static final XPath newXPath(String xpath) {
		try {
			return new JerichoXPath(xpath);
		} catch (JaxenException jex) {
			log.error(String.format("Erro ao inicializar o XPath: %s", xpath), jex);
			return null;
		}
	}
	
	private static Filme extrairFilme(String emissora, String url) {
		try {
			StringBuilder sb = new StringBuilder();
			
			sb.append(URL_HAGAH_TV).append(url);
			
			InputStream in = readUrl(sb.toString());
			try {
				Source doc = new Source(in);
				doc.fullSequentialParse();
				
				Object div = ((List<?>)xpath_div_filme.evaluate(doc)).get(0);
				
				Filme filme = new Filme();
				
				filme.setTitulo((String)xpath_nome_filme.evaluate(div));
				filme.setLocalExibicao(new LocalExibicao(emissora, TipoLocal.TV));
				filme.setDublado(true);
				
				Calendar cal = Calendar.getInstance(PT_BR);
				Calendar temp = Calendar.getInstance(PT_BR);
				
				String tempS = (String)xpath_data_filme.evaluate(div);
				if(tempS.contains("Segunda")){
					tempS = tempS.substring(0, 13) + "-Feira" + tempS.substring(13);
				}
				Date data = new SimpleDateFormat("'Data: 'EEEEEEEE' - 'dd' de 'MMMMM' | '", PT_BR).parse(tempS);
				Date horario; 
				try {
					horario = new SimpleDateFormat("'Início: 'HH'h'mm' | '", PT_BR).parse((String)xpath_horario_filme.evaluate(div));
				} catch (ParseException pex) {
					horario = new SimpleDateFormat("'Início: 'HH'h | '", PT_BR).parse((String)xpath_horario_filme.evaluate(div));
				}

				temp.setTime(data);
				cal.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK));	
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
				
				temp.setTime(horario);
				cal.set(Calendar.HOUR, temp.get(Calendar.HOUR));
				cal.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
				cal.set(Calendar.SECOND, 0);
				
				filme.setHorarios(Lists.newArrayList(cal.getTime()));
				
				return filme;
			} finally {
				in.close();
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static InputStream readUrl(String url) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		return in;
	}

	public static List<Filme> extrair(Date date) throws ClientProtocolException, JaxenException, IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append(URL_HAGAH_TV).append(URL_HAGAH_TV_PAGINA);
		
		;
		sb.append("&data=").append(new SimpleDateFormat("dd/MM/yyyy").format(date));
		sb.append("&hora=").append(new SimpleDateFormat("HH:mm").format(date));
		
		InputStream in = readUrl(sb.toString());
		try {
			Source doc = new Source(in);
			doc.fullSequentialParse();
			
			List<Filme> filmes = Lists.newArrayList();
			
			List<?> trEmissoras = (List<?>)xpath_trs_emissoras.evaluate(doc);
			for (Object oEmissora : trEmissoras) {
				final String emissora = (String)xpath_nome_emissora.evaluate(oEmissora);
				Iterables.addAll(filmes, FluentIterable
					.from((List<?>)xpath_filmes.evaluate(oEmissora))
					.transform(new Function<Object, Filme>() {
						@Override
						public Filme apply(Object obj) {
							return extrairFilme(emissora, ((Attribute)obj).getValue());
						}
					}));
			}
			
			return filmes;
		} finally {
			in.close();
		}
	}
}