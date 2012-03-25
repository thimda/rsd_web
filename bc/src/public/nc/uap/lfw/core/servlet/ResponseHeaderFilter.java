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
 * http��Ӧͷ��ϢFilter����Filter��Ҫ����ʹ�������һЩͼƬ��������������Ļ��棬���ٵ���������
 * �汾�ȽϺ����ش���.����ʹ�������web.xml������.
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
