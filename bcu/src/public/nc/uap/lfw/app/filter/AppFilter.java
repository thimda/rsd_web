package nc.uap.lfw.app.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.log.LfwLogger;
/**
 * LFW App型应用URL重写过滤器,将形如 core/xx.app 地址重写为core/app/xx
 * @author dengjt
 *
 */
public class AppFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		LfwLogger.debug("Application Filter started");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String[] params = req.getRequestURI().split("/");
		String appId = params[3];
		String winId = null;
		String opeId = null;
		if(params.length > 4)
			winId = params[4];
		else
			winId = "";
		if(params.length > 5)
			opeId = params[5];
		String url = "/core/"+appId+".app?" + AppConsts.PARAM_WIN_ID + "=" + winId;
		if(opeId != null)
			url += "&" + AppConsts.PARAM_WIN_OPE + "=" + opeId;
//		if(winId != null)
//			url += "?winId=" + winId;
		RequestDispatcher disp = LfwRuntimeEnvironment.getServletContext().getRequestDispatcher(url);
		disp.forward(request, response);
	}

	@Override
	public void destroy() {
		LfwLogger.debug("Application Filter destroyed");
	}

}
