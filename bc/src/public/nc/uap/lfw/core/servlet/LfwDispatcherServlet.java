package nc.uap.lfw.core.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nc.uap.lfw.core.LfwCoreController;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.util.LfwClassUtil;
/**
 * Web Framework dispatcher Servlet.基于Lfw的单据必须配置此Servlet。此servlet路径名可在ctxpath属性中配置，默认
 * 为core
 * @author dengjt
 * 2007-1-10
 */
public class LfwDispatcherServlet extends LfwServletBase{

	private static final long serialVersionUID = -8444315709092554126L;
	private static final String LFW_ERROR_PAGE = "/lfw/html/nodes/lfw_error.jsp";
	private String ctrlClazz;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//获得当前servlet映射路径
		String corePath = config.getInitParameter("ctxpath");
		if(corePath == null){
			LfwLogger.error("ctxpath is not set in context-param");
			corePath = "/core";
		}
		String ctxPath = (String) getServletContext().getAttribute(WebConstant.ROOT_PATH);
		getServletContext().setAttribute(WebConstant.CORE_PATH, ctxPath.equals("/") ? ctxPath: ctxPath + corePath);

		ctrlClazz = config.getInitParameter("ctrlclass");
		if(ctrlClazz == null){
			LfwLogger.warn("没有设置ctrlclass，使用预置class");
			ctrlClazz = LfwCoreController.class.getName();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		//this is the hacking code.whenever you use http://url/core?setdebugmode=1,all calls in the same session will be executed in debug mode
		//use http://url/core?setdebugmode=0 to cancel it.you hardly need to do that
		//StringServletResponse response = new StringServletResponse(res);
		
		String setdebugmode = req.getParameter("setdebugmode");
		//强制设置不存储缓存
		res.addHeader("Cache-Control", "no-store");
		if(setdebugmode != null && setdebugmode.equals("1")){
			HttpSession session = req.getSession(true);
			session.setAttribute(WebConstant.DEBUG_MODE, setdebugmode);
		}else{
			try
			{
				//res.setBufferSize(10240);
				LfwCoreController ctrl = (LfwCoreController) LfwClassUtil.newInstance(ctrlClazz);
				ctrl.handleRequest(req, res);
			}
			catch(Exception e)
			{
				processError(req, res, e);
			}
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendError(403);
	}

	private void processError(HttpServletRequest req, HttpServletResponse res,
			Exception e) throws IOException, UnsupportedEncodingException {
		//if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG)){
			if(e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new LfwRuntimeException(e);
//		}
//		else{
//			//Ajax请求不会抛到这里，所以这里的异常都是页面异常
//			LfwLogger.error(e.getMessage(), e);
//			String url = req.getRequestURI();
//			String queryString = req.getQueryString();
//			if(queryString != null)
//				url += "?" + queryString;
//			req.getSession().setAttribute(SessionConstant.EXCEPTION_KEY, e);
//			req.getSession().setAttribute(SessionConstant.LAST_URL_KEY, url);
//			String errorPage = (String) req.getSession().getServletContext().getAttribute(ApplicationConstant.ERROR_PAGE);
//			if(errorPage == null)
//				errorPage = LFW_ERROR_PAGE;
//			if(!errorPage.startsWith("/lfw"))
//				errorPage = LfwRuntimeEnvironment.getRootPath() + errorPage;
//			res.sendRedirect(errorPage + "?msg=" + URLEncoder.encode(e.getMessage() == null ? e.toString():e.getMessage(), "UTF-8"));
//		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
