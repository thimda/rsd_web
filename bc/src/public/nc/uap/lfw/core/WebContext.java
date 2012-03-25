package nc.uap.lfw.core;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nc.bs.framework.execute.Executor;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.ApplicationConstant;
import nc.uap.lfw.core.common.ParamConstant;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.servlet.IWebSessionListener;
import nc.uap.lfw.util.LfwClassUtil;


/**
 * Web上下文信息
 * 
 * @author dengjt
 * 
 */
public class WebContext implements Serializable {

	private static final String CHILDLIST = "childlist";
	public static final String LOGIN_DID = "login_did";
	public static final String LOGIN_DESTROY = "login_destroy";
	private static final long serialVersionUID = -2362958999118316626L;
	private PageMeta pageMeta = null;
	private IUIMeta uiMeta = null;
	private String pageId = null;
	private String pageUniqueId = null;
	private String parentUniqueId = null;
	private String parentPageId = null;
	// user define attributes
	private Object userObject = null;
	private String appUniqueId = null;

	/* 存放交互过程中request对象信息，通过此信息可以获取外部Web交互环境。 */
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	public static final String WEB_SESSION_PRE = "$WS_ID_";
	private static final String PARENT_UNIQUE_ID = "$PARENT_UNIQUE_ID";
	// private static final String PARENT_PAGE_ID = "$PARENT_PAGE_ID";
	public static final String PAGEMETA_KEY = "$PAGEMETA_KEY";
	public static final String UIMETA_KEY = "$UIMETA_KEY";
	public static final String APP_CONF = "APP_CONF";
	public static final String APP_ID = "appId";
	public static final String APP_UNIQUE_ID = "appUniqueId";
	public static final String APP_SES = "$APP_SES";
	private WebSession ses;
	private WebSession appSes;
	public WebContext() {
	}

	public WebContext(HttpServletRequest request) {
		this.request = request;
		// 从参数中获得pageId
		this.pageId = request.getParameter(ParamConstant.PAGE_ID);
		// 从参数中获得pageUniqueId
		this.pageUniqueId = request.getParameter(ParamConstant.PAGE_UNIQUE_ID);
		
		this.appUniqueId = request.getParameter(ParamConstant.APP_UNIQUE_ID);

		this.parentUniqueId = request.getParameter(ParamConstant.OTHER_PAGE_UNIQUE_ID);

		this.parentPageId = request.getParameter(ParamConstant.OTHER_PAGE_ID);
	}

	@SuppressWarnings("unchecked")
	private void initParam(WebSession ses) {
		Iterator<Entry<String, String[]>> it = this.request.getParameterMap().entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String[]> entry = it.next();
			//参数限定不会出现多个
			String[] values = entry.getValue();
			if(values != null && values.length > 0)
				ses.addOriginalParameter(entry.getKey(), values[0]);
		}
	}

	@SuppressWarnings("unchecked")
	public Map getParamMap() {
		if (request == null)
			return null;
		return request.getParameterMap();
	}

	public String getParameter(String key) {
		if (request == null)
			return null;
		return request.getParameter(key);
	}

	public void setAttribute(String key, Object obj) {
		request.setAttribute(key, obj);
	}

	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}
	
	public void removeAttribute(String key){
		request.removeAttribute(key);
	}

	public String getPageId() {
		return pageId;
	}
	
	/**
	 * 获得页面初始参数，即请求页面的URL参数
	 * @param key
	 * @return
	 */
	public String getOriginalParameter(String key){
		return getWebSession().getOriginalParameter(key);
	}

	public String getPageUniqueId() {
		if (this.pageUniqueId == null) {
//			String requestURI = request.getRequestURI();
//			if (requestURI.endsWith(".jsp") || requestURI.endsWith("um")
//					|| requestURI.endsWith("html") || requestURI.endsWith("ui") || requestURI.endsWith("ra")) {
////				// 如果是loadrunner测试，以loadrunner为准
////				if (LfwRuntimeEnvironment.getServerConfig()
////						.getLoadRunnerAdpater().equals("1")) {
////					this.pageUniqueId = request.getParameter("lrid");
////					LfwLogger.debug("获得loadrunner id:" + this.pageUniqueId);
////				}
////				if (this.pageUniqueId == null) {
////					String etag = request.getHeader("If-None-Match");
////					if (etag != null)
////						this.pageUniqueId = etag;
////				}
//				if (this.pageUniqueId == null)
//					this.pageUniqueId = UUID.randomUUID().toString();
//			}
//			if (this.pageUniqueId == null)
//				this.pageUniqueId = UUID.randomUUID().toString();
//			
			this.pageUniqueId = UUID.randomUUID().toString();
		}
		return this.pageUniqueId;
	}
	
	public PageMeta getCrossPageMeta(String crossPageId) {
		String crossUniqueId = getUniqueIdByPageId(crossPageId);
		if(crossUniqueId == null)
			return null;
		WebSession ws = getWebSession(crossUniqueId);
		PageMeta pm = (PageMeta) ws.getAttribute(WebContext.PAGEMETA_KEY);
		if (pm == null) {
			LfwLogger.error("客户端传入了不正确的pageId:" + pageUniqueId);
			getResponse().setStatus(308);
			return null;
		}

		return (PageMeta) pm.clone();
	}

	private String getUniqueIdByPageId(String crossPageId) {
		WebSession appSes = getAppSession();
		Map<String, String> pageMap = (Map<String, String>) appSes.getAttribute(CHILDLIST);
		if(pageMap == null)
			return null;
		return pageMap.get(crossPageId);
	}

	public PageMeta getPageMeta() {
		if (pageMeta == null) {
			WebSession ws = getWebSession();
			PageMeta pm = (PageMeta) ws.getAttribute(WebContext.PAGEMETA_KEY);
			pageMeta = pm;
		}
		return pageMeta;
	}
	
	public IUIMeta getUIMeta() {
		if(uiMeta == null){
			WebSession ws = getWebSession();
			IUIMeta um = (IUIMeta) ws.getAttribute(WebContext.UIMETA_KEY);
			uiMeta = um;
		}
		return uiMeta;
	}

	public void setPageMeta(PageMeta pageMeta) {
		this.pageMeta = pageMeta;
	}

//	public PageMeta getOriginalPageMeta() {
//		WebSession ws = getWebSession();
//		PageMeta pm = (PageMeta) ws.getAttribute(WebContext.PAGEMETA_KEY);
//		if (pm == null) {
//			LfwLogger.error("客户端传入了不正确的pageId:" + pageUniqueId);
//			getResponse().setStatus(308);
//			return null;
//		}
//
//		return pm;
//	}
	
//	public IUIMeta getOriginalUIMeta() {
//		WebSession ws = getWebSession();
//		IUIMeta um = (IUIMeta) ws.getAttribute(WebContext.UIMETA_KEY);
//		return um;
//	}

//	public PageMeta getParentOriginalPageMeta() {
//		String parentPageId = getParentPageUniqueId();
//		WebSession ws = getParentWebSession(parentPageId);
//		PageMeta pm = (PageMeta) ws.getAttribute(WebContext.PAGEMETA_KEY);
//		if (pm == null) {
//			LfwLogger.error("客户端传入了不正确的pageId:" + pageUniqueId);
//			getResponse().setStatus(308);
//			return null;
//		}
//
//		return pm;
//	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 消除这个页面所对应的session区域及子区域
	 */
	public void destroyWebSession() {
		doDestroyPage(this.pageUniqueId);
	}

	private void doDestroyPage(String pageId) {
		//为了确保页面的正常逻辑，延迟5秒钟之后执行销毁
		DelayRunnable run = new DelayRunnable(){
			public void run() {
				try {
					Thread.sleep(2000);
				} 
				catch (InterruptedException e) {
					LfwLogger.error(e);
				}
				LfwRuntimeEnvironment.setDatasource(datasource);
				ILfwCache cache = LfwCacheManager.getSessionCache(session.getId());
				doDelayDestroyPage(pageId, cache, session);
				if(appSes != null){
					Map<String, String> childIds = (Map<String, String>) appSes.getAttribute(CHILDLIST);
					if(childIds == null || childIds.size() == 0){
						String id = WEB_SESSION_PRE + appSes.getWebSessionId();
						cache.remove(id);
						appSes.destroy();
					}
				}
				doClearSession(cache, session);
			}
		};
		
		run.pageId = pageId;
		run.session = request.getSession(true);
		run.datasource = LfwRuntimeEnvironment.getDatasource();
		WebSession ws = getWebSession(pageId);
		if(ws.getAttribute(APP_UNIQUE_ID) != null)
			run.appSes = getAppSession((String) ws.getAttribute(APP_UNIQUE_ID));
		Executor thread = new Executor(run);
		thread.start();
	}
	
	/**
	 * 确保在浏览器直接叉掉时，缓存被迅速删掉
	 * @param session
	 */
	private void doClearSession(ILfwCache cache, HttpSession session){
		try{
			if(session.getAttribute(LOGIN_DESTROY) != null){
				Iterator em = cache.getKeys().iterator();
				boolean find = false;
				while (em.hasNext()) {
					String key = em.next().toString();
					if (key.startsWith(WEB_SESSION_PRE)) {
						find = true;
						break;
					}
				}
				//如果是login页面，并且没有websession对象，可以确定是login销毁并没有导向到其它页面，可直接注销session
				if(!find)
					session.invalidate();
			}
		}
		/*
		 * 如果是此Exception，表明是因为注销而导致session过期，实际上对应的变量已经确保删除。由于session没有提供判断是否是过期的直接API，此处通过catch异常来
		 *处理，此异常是正常情况，不进行记录及抛出。以免造成大量日志
		*/
		catch(IllegalStateException e){
		}
	}
	/**
	 * 真正进行删除
	 * @param pageId
	 * @param session 
	 */
	@SuppressWarnings("unchecked")
	private void doDelayDestroyPage(String pageId, ILfwCache cache, HttpSession session){
		try{
			//递归消除子页面内存
			Iterator em = cache.getKeys().iterator();
			while (em.hasNext()) {
				String key = em.next().toString();
				if (key.startsWith(WEB_SESSION_PRE)) {
					WebSession ws = (WebSession) cache.get(key);
					if (ws == null)
						continue;
					String pId = (String) ws.getAttribute(PARENT_UNIQUE_ID);
					if (pId != null) {
						if (pId.equals(pageId)) {
							doDelayDestroyPage(ws.getWebSessionId(), cache, session);
						}
					}
				}
			}
		}
		/*
		 * 如果是此Exception，表明是因为注销而导致session过期，实际上对应的变量已经确保删除。由于session没有提供判断是否是过期的直接API，此处通过catch异常来
		 *处理，此异常是正常情况，不进行记录及抛出。以免造成大量日志
		*/
		catch(IllegalStateException e){
		}
		
		if(this.appUniqueId != null && !pageId.equals(this.appUniqueId)){
			WebSession appSes = getAppSession();
			Map<String, String> childIds = (Map<String, String>) appSes.getAttribute(CHILDLIST);
			if(childIds != null)
				childIds.remove(pageId);
		}
		
		String uid = WEB_SESSION_PRE + pageId;
		WebSession ws = (WebSession) cache.remove(uid);
		if(ws != null)
			ws.destroy();
		
		LfwLogger.info(LfwLogger.LOGGER_LFW_SYS, "destroy websession:" + pageId);
	}

	public WebSession getParentSession() {
		return getWebSession(getParentPageUniqueId());
	}

	public WebSession getWebSession(boolean create){
		if(create)
			return getWebSession();
		return ses;
	}
	public WebSession getWebSession() {
		if(ses == null){
			String pageUniqueId = getPageUniqueId();
			if(pageUniqueId == null)
				throw new LfwRuntimeException("此处不能获取WebSession");
			ses = getWebSession(getPageUniqueId());
		}
		return ses;
	}

	public WebSession getAppSession() {
		if(appSes == null){
			if(this.appUniqueId == null)
				throw new LfwRuntimeException("参数不正确");
			appSes = getWebSession(this.appUniqueId, false);
		}
		return appSes;
	}
	
	/**
	 * 内部调用，确保调用时，只创建一次
	 * @param appId
	 * @return
	 */
	private WebSession getAppSession(String appId) {
		if(appSes == null){
			if(this.appUniqueId != null)
				return getAppSession();
			this.appUniqueId = createAppUniqueId();
			appSes = createAppSession(this.appUniqueId, appId);
		}
		return appSes;
	}
	
	public String getAppUniqueId() {
		if (this.appUniqueId == null) {
			this.appUniqueId = createAppUniqueId();
		}
		return this.appUniqueId;
	}
	
	private String createAppUniqueId(){
		return UUID.randomUUID().toString();
	}

	public WebSession getCrossWebSession(String id){
		return getWebSession(id);
	}
	
	
	private WebSession createAppSession(String id, String appId) {
		String sid = WEB_SESSION_PRE + id;
		ILfwCache cache = LfwCacheManager.getSessionCache();
		WebSession ws = createWebSessionImpl();
		ws.setWebSessionId(id);
		ws.setPageId(getPageId());
		ws.setAttribute(APP_UNIQUE_ID, appUniqueId);
		ws.setAttribute(CHILDLIST, new HashMap<String, String>());
		ws.setAttribute(APP_SES, Boolean.TRUE);
		ws.setAttribute(APP_ID, appId);
		cache.put(sid, ws);
		if(ws.getOriginalParamMap() == null){
			initParam(ws);
		}
		ws.created();
		return ws;
	}
	
	private WebSession getWebSession(String id) {
		return getWebSession(id, true);
	}
	
	private WebSession getWebSession(String id, boolean create) {
		String sid = WEB_SESSION_PRE + id;
		ILfwCache cache = LfwCacheManager.getSessionCache();
		WebSession ws = (WebSession) cache.get(sid);
		if (ws == null) {
			if(!create)
				return ws;
			ws = createWebSessionImpl();
			ws.setWebSessionId(id);
			ws.setPageId(getPageId());
			if (parentUniqueId != null) {
				ws.setAttribute(PARENT_UNIQUE_ID, parentUniqueId);
			}
			ws.setAttribute(APP_UNIQUE_ID, appUniqueId);
			if(this.appUniqueId != null){
				WebSession appSes = getAppSession();
				Map<String, String> childIds = (Map<String, String>) appSes.getAttribute(CHILDLIST);
				childIds.put(pageId, id);
			}
			cache.put(sid, ws);
			if(ws.getOriginalParamMap() == null){
				initParam(ws);
			}
			ws.created();
		}
		return ws;
	}
	
	/**
	 * 创建WebSession实例。从配置文件中获取。默认取内存实现
	 * @return
	 */
	private WebSession createWebSessionImpl() {
		String clazz = LfwRuntimeEnvironment.getModelServerConfig().getWebSessionImpl();
		if(clazz == null || clazz.equals(""))
			clazz = DbWebSession.class.getName();
		
		IWebSessionListener[] wsListener = getWebSessionListeners();
		Class<WebSession> sesClazz = (Class<WebSession>) LfwClassUtil.forName(clazz);
		try {
			Constructor<WebSession> cons = sesClazz.getConstructor(new Class[]{IWebSessionListener[].class});
			ses = cons.newInstance(new Object[]{wsListener});
		} 
		catch (Throwable e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("构造websession 类出现异常");
		}
		return ses;
	}

	private IWebSessionListener[] getWebSessionListeners() {
		Object result = request.getSession().getServletContext().getAttribute(ApplicationConstant.WEBSESSION_LISTENER);
		if(result == null){
			createWebSessionListeners();
			result = request.getSession().getServletContext().getAttribute(ApplicationConstant.WEBSESSION_LISTENER);
		}
		if(result instanceof String)
			return null;
		return (IWebSessionListener[]) result;
	}

	private void createWebSessionListeners() {
		String[] listeners = LfwRuntimeEnvironment.getModelServerConfig().getWebSessionListeners();
		if(listeners == null || listeners.length == 0)
			request.getSession().getServletContext().setAttribute(ApplicationConstant.WEBSESSION_LISTENER, "EMPTY");
		else{
			IWebSessionListener[] listenerClazz = new IWebSessionListener[listeners.length];
			for (int i = 0; i < listenerClazz.length; i++) {
				listenerClazz[i] = (IWebSessionListener) LfwClassUtil.newInstance(listeners[i]);
			}
			request.getSession().getServletContext().setAttribute(ApplicationConstant.WEBSESSION_LISTENER, listenerClazz);
		}
	}

	public void adjustPageUniqueId(String newUniqueId) {
		if (newUniqueId == null || newUniqueId.equals(""))
			return;
		WebSession ws = getWebSession(this.pageUniqueId);
		ws.setWebSessionId(newUniqueId);
		String sid = WEB_SESSION_PRE + this.pageUniqueId;
//		HttpSession session = request.getSession(true);
		ILfwCache cache = LfwCacheManager.getSessionCache();
		cache.remove(sid);
		cache.put(WEB_SESSION_PRE + newUniqueId, ws);
		this.pageUniqueId = newUniqueId;
	}

	private WebSession getParentWebSession(String id) {
		String sid = WEB_SESSION_PRE + id;
		ILfwCache cache = LfwCacheManager.getSessionCache();
		return (WebSession) cache.get(sid);
	}

	/**
	 * 获取此处请求是由哪个pagemeta发起的。
	 * 
	 * @return
	 */
	public PageMeta getParentPageMeta() {
		String parentPageId = getParentPageUniqueId();
		WebSession parentWs = getParentWebSession(parentPageId);
		return (PageMeta) parentWs.getAttribute(PAGEMETA_KEY);
	}

	public String getParentPageId() {
		// return (String) getWebSession().getAttribute(PARENT_PAGE_ID);
		return this.parentPageId;
	}

	public String getParentPageUniqueId() {
		//return (String) getWebSession().getAttribute(PARENT_UNIQUE_ID);
		return this.parentUniqueId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public void setPageUniqueId(String pageUniqueId) {
		this.pageUniqueId = pageUniqueId;
	}

	public void setResponse(HttpServletResponse res) {
		this.response = res;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setUIMeta(IUIMeta uiMeta) {
		this.uiMeta = uiMeta;
	}

	public IUIMeta getCrossUm(String crossPageId) {
		String crossUniqueId = getUniqueIdByPageId(crossPageId);
		WebSession ws = getWebSession(crossUniqueId);
		IUIMeta um = (IUIMeta) ws.getAttribute(WebContext.UIMETA_KEY);
		return um.doClone();
	}
}

abstract class DelayRunnable implements Runnable{
	protected String pageId;
	protected HttpSession session;
	protected String datasource;
	protected WebSession appSes;
}
