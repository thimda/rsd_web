package nc.uap.lfw.login.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ModelServerConfig;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.common.SessionConstant;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.itf.ILfwSsoQryService;
import nc.uap.lfw.login.itf.ILfwSsoService;
import nc.uap.lfw.login.itf.ILoginSsoService;
import nc.uap.lfw.login.itf.LoginHelper;
import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.util.LfwLoginFetcher;
import nc.uap.lfw.login.vo.LfwSessionBean;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractLfwLoginFilter implements Filter {
	
	private static final String ESCAPE_URL = "ESCAPE_URL";
	// 系统登录页面的名称
	protected String LOGIN_PATH;
	protected String RETURN_PARAM = "returnUrl";
	protected String ABOUT_PATH = "/lfw/core/login/about.jsp";
	// SSO服务器地址
	protected String SSO_SERVER = "127.0.0.1";

	public void init(FilterConfig config) throws ServletException {
		if (config.getInitParameter("login_path") != null)
			LOGIN_PATH = config.getInitParameter("login_path");
		if (config.getInitParameter("about_path") != null)
			ABOUT_PATH = config.getInitParameter("abount_path");
		if (config.getInitParameter("sso_server") != null)
			SSO_SERVER = config.getInitParameter("sso_server");
	}

	/**
	 * 登陆页面
	 * 
	 * @return
	 */
	protected String getLoginJspName() {
		return LOGIN_PATH == null ? LfwRuntimeEnvironment.getRootPath()	+ "/core/login.jsp?pageId=login" : LOGIN_PATH;
	}

	/**
	 * ‘关于’
	 * 
	 * @return
	 */
	private String getAboutJspName() {
		return ABOUT_PATH;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		LfwLogger.debug("进入LoginFilter进行身份判断。");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		WebContext webCtx = LfwRuntimeEnvironment.getWebContext();
		if(webCtx != null)
			LfwRuntimeEnvironment.getWebContext().setResponse(res);
		if (isToLogin(req, res)) {
			chain.doFilter(request, response);
		}else if (isEscapeUrl(req, res)) {
			chain.doFilter(request, response);
		} else if (isUserLogin(req, res)) {
			chain.doFilter(request, response);
		} else if(isSsoLogin(req, res)){
			chain.doFilter(request, response);
		}else if (restoreLogin(req, res)) {
			chain.doFilter(request, response);
		} else {
			String currentUrl = req.getRequestURI();
			if (req.getQueryString() != null)
				currentUrl += "?" + req.getQueryString();
			if (!isNotInControlUrl(currentUrl, req)){
				if (req.getParameter("isAjax") != null) {  // 是Ajax调用
					// 设置为会话失效状态
					res.setStatus(306);
					return;
				}
				req.getSession().setAttribute(SessionConstant.CURRENT_URL, currentUrl);
				res.sendRedirect(getLoginJspName());
			}
			else
				chain.doFilter(request, response);
		}
	}
	
	protected boolean restoreLogin(HttpServletRequest req,
			HttpServletResponse res) {
		ILoginSsoService<? extends LfwSessionBean> ssoService = getLoginSsoService();
		LfwSessionBean ses = ssoService.restoreSsoSign(getSysType());
		return ses != null;
	}

	protected abstract String getSysType();

	protected abstract ILoginSsoService<? extends LfwSessionBean> getLoginSsoService();

	private boolean isToLogin(HttpServletRequest req, HttpServletResponse res) {
		String pageId = req.getParameter("pageId");
		return pageId != null && pageId.equals("login");
	}
	/**
	 * 不受权限控制的URL
	 * @param req
	 * @param res
	 * @return
	 */
	protected boolean isEscapeUrl(HttpServletRequest req, HttpServletResponse res) {
		ModelServerConfig modelConfig = LfwRuntimeEnvironment.getModelServerConfig();
		if(modelConfig == null)
			return false;
		String exp = modelConfig.getConfigValue(ESCAPE_URL);
		String url = req.getServletPath() + StringUtils.defaultIfEmpty(req.getPathInfo(), "") + StringUtils.defaultIfEmpty(req.getQueryString(), "") ;
		if(exp != null && exp.length() > 0){
			String[] ex = exp.split(",,,");
			for(String e : ex){
				if(url.indexOf(e) != -1) 
					return true;
			}
		}
		if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN) || LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG)){
			String eclipse = req.getParameter("eclipse");
			if(eclipse != null && eclipse.equals("1"))
				return true;
		}
		if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG) || LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN)){
			/**
			 * 开发态下，不拦截ajax请求
			 */
			if(url.equals("/core")){
				return true;
			}
			
			String pageUniqueId = LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
			if(pageUniqueId != null){
			
				/**
				 * 个性化定制
				 */
				String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
				if(editModeStr == null)
					editModeStr = req.getParameter(WebConstant.EDIT_MODE_PARAM);
				else{
					boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
					if(editMode)
						return editMode;
				}
				if(editModeStr != null && editModeStr.equals("1")){
					return true;
				}
				
				/**
				 * 注册菜单
				 */
				String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
				if(pageId == null || pageId.isEmpty()){
					pageId = req.getParameter("pageId");
				}
				if(pageId == null)
					return false;
				String[] esPage = new String[]{
						"cp_menuitem",
						"cp_funcregister",
						"cp_menucategory",
						"pa","wfm_flwtype"
						};
				for(String e : esPage){
					if(pageId.equals(e)) 
						return true;
				}
			}
		}
		
		return false;
	}
	
	protected boolean isSsoLogin(HttpServletRequest req,
			HttpServletResponse res){
		String ssoKey = req.getParameter("ssoKey");
		String simpleFlag = req.getParameter("simpleFlag");
		//加入数据源设置
		String dsName = req.getParameter("dsname");
		if(dsName != null)
			LfwRuntimeEnvironment.setDatasource(dsName);
		
		Map param = null;
		boolean safeMode = ssoKey != null && !ssoKey.isEmpty();
		boolean simpleMode = simpleFlag != null && !simpleFlag.isEmpty();
		
		if(!safeMode && !simpleMode)
			return false;
		try {
			/**
			 * 使用注册式登陆
			 */
			if(safeMode){
				param = NCLocator.getInstance().lookup(ILfwSsoQryService.class).getUserBySsoKey(ssoKey);
			}
			/**
			 * 使用用户名密码登陆
			 */
			else{
				param = getParameterMap(req);
			}
			if(param != null){
				AuthenticationUserVO  userVO = getLoginHelper().createIntegrationHandler().getSsoAuthenticateVO(param);
				getLoginHelper().processLogin(userVO);
				boolean login = isUserLogin(req, res);
				if(login){
					if(safeMode)
						getSsoService().destorySsoInfo(ssoKey);
				}
				return login;
			}
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		return false;
	}
	
	
	/**
	 * 转换参数
	 * @param req
	 * @return
	 */
	private Map<String, String> getParameterMap(HttpServletRequest req) {
		HashMap<String, String> p = new HashMap<String, String>();
		Enumeration<String> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			p.put(key, req.getParameter(key));
		}
		return p;
	}
	/**
	 * 获得LoginHelper
	 * @return
	 */
	private LoginHelper<?> getLoginHelper(){
		return LfwLoginFetcher.getGeneralInstance().getLoginHelper();
	}
	
	/**
	 * 
	 * @param currentUrl
	 * @param req
	 * @return
	 */
	protected boolean isNotInControlUrl(String currentUrl,
			HttpServletRequest req) {
		WebContext wc = LfwRuntimeEnvironment.getWebContext();
		if(wc == null)
			return true;
		String pageId = wc.getPageId();
		if (pageId != null && pageId.equals("reference"))
			return true;
		String JSON_PATH = LfwRuntimeEnvironment.getCorePath() + "/json";
		return (currentUrl.indexOf(getLoginJspName()) != -1)
				|| (currentUrl.indexOf(JSON_PATH) != -1)
				|| (currentUrl.equals(getAboutJspName()))
				|| (currentUrl.indexOf("/reference/") != -1);
	}

	protected boolean isUserLogin(HttpServletRequest request, HttpServletResponse response) {
		String ssoKey = request.getParameter("ssoKey");
		String simpleFlag = request.getParameter("simpleFlag");
		boolean safeMode = ssoKey != null && !ssoKey.isEmpty();
		boolean simpleMode = simpleFlag != null && !simpleFlag.isEmpty();
		if(safeMode || simpleMode){
			request.setAttribute(SessionConstant.INTEGRATE_MODE, "1");
		}
		return  LfwRuntimeEnvironment.getLfwSessionBean() != null  ;
		
	}

	public void destroy() {
	}
	/**
	 * 获得单点登陆服务
	 * 
	 * @return
	 */
	protected ILfwSsoService getSsoService() {
		return NCLocator.getInstance().lookup(ILfwSsoService.class);
	}
}
