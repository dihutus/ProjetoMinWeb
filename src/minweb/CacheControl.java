package minweb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CacheControl implements Filter {

	@Override
	public void init(FilterConfig cfg) throws ServletException { }

	@Override
	public void destroy() { }

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		if (res instanceof HttpServletResponse) {
			HttpServletResponse httpRes = (HttpServletResponse)res;
			
			httpRes.addHeader("Pragma", "no-cache");
			httpRes.addHeader("Cache-Control", "no-cache");
			httpRes.addHeader("Cache-Control", "no-store");
			httpRes.addHeader("Cache-Control", "must-revalidate");
			httpRes.addHeader("Expires", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
				.format(new Date()));
		}
		
		chain.doFilter(req, res);
	}
}