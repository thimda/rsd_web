package nc.uap.lfw.core.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.mx.thread.ThreadTracer;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
/**
 * LFW Servlet»ùÀà
 * @author dengjt
 *
 */
public abstract class LfwServletBase extends HttpServlet{
	private static final long serialVersionUID = 2678149808373514646L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		WebContext webCtx = LfwRuntimeEnvironment.getWebContext();
		if(webCtx != null)
			LfwRuntimeEnvironment.getWebContext().setResponse(res);
		
		addRemoteCallMethod(req, res);
		super.service(req, res);
	}
	
	
	
	protected void addRemoteCallMethod(HttpServletRequest req,
			HttpServletResponse res) {
		String method = getRemoteCallMethod(req, res);
		if(method != null && !method.equals(""))
			ThreadTracer.getInstance().setRemoteCallMethod(method);
	}

	public String getRemoteCallMethod(HttpServletRequest req, HttpServletResponse res){
		return req.getRequestURI();
	}
}
