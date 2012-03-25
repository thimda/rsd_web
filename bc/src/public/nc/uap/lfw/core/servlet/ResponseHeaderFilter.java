package nc.uap.lfw.core.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
/**
 * http响应头信息Filter。此Filter主要用来使浏览器对一些图片等做到最大能力的缓存，减少到服务器的
 * 版本比较和下载次数.具体使用需参照web.xml中配置.
 * 
 * @author dengjt
 *
 */
public class ResponseHeaderFilter implements Filter {

	private FilterConfig config;
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
	
	public void destroy() {
	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
//		if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION)){
			HttpServletResponse response = (HttpServletResponse) res;
			Enumeration em = config.getInitParameterNames();
			while(em.hasMoreElements()){
				String headerName = (String) em.nextElement();
				response.addHeader(headerName, config.getInitParameter(headerName));
			}
//		}
		chain.doFilter(req, res);
	}


}
