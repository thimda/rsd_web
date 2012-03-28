package nc.uap.lfw.app.plugin;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.ControlPlugin;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.common.ParamConstant;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.parser.ApplicationParser;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.util.ApplicationNodeUtil;
/**
 * App 类型节点控制器
 * @author dengjt
 *
 */
public class AppControlPlugin implements ControlPlugin {
	public static final String NODECODE = "nodecode";
	private static Method appMethod;
	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, String path) throws Exception {
		String appPath = req.getPathInfo();
		String appId = appPath.substring(appPath.indexOf("/") +1 , appPath.indexOf("."));
		String realPath = ApplicationNodeUtil.getApplicationNodeDir(appId);
		if(appMethod == null){
			appMethod = WebContext.class.getDeclaredMethod("getAppSession", new Class[]{String.class});
			appMethod.setAccessible(true);
		}
		//创建AppWebSession
		WebSession webSes = (WebSession) appMethod.invoke(LfwRuntimeEnvironment.getWebContext(), new String[]{appId});
		//WebSession webSes = LfwRuntimeEnvironment.getWebContext().getAppSession();
		webSes.setAttribute(WebContext.APP_ID, appId);
		Application app = (Application) webSes.getAttribute(WebContext.APP_CONF);
		String winid = req.getParameter(AppConsts.PARAM_WIN_ID);
		if(app == null){
			if(!appId.equals("mockapp")){
				File appDefFile = ContextResourceUtil.getFile("/html/applications/" + realPath + "/application.app");
				app = ApplicationParser.parse(appDefFile);
			}
			else{
				if(winid == null || winid.equals("")){
					throw new LfwRuntimeException("Mock app 必须输入window id");
				}
				PageMeta pm = new PageMeta();
				pm.setId(winid);
				app = new Application();
				app.addWindow(pm);
			}
			webSes.setAttribute(WebContext.APP_CONF, app);
		}
		
		String winOpe = req.getParameter(AppConsts.PARAM_WIN_OPE);
		if(winid == null || winid.isEmpty() || winid.equals("null")){
			winid = app.getDefaultWindowId();
		}
		
		if(winid == null)
			throw new LfwRuntimeException("没有设置window id");
		
		String url = null;
		int index = winid.indexOf(".");
		if(index != -1){
			url = "/core/uimeta.jsp";
			winid = winid.split("\\.")[0];
		}
		else{
			url = "/core/uimeta.ra";
		}
		
		if(winOpe != null && winOpe.equals("add")){
			if(app.getWindowConf(winid) == null){
				PageMeta win = new PageMeta();
				win.setId(winid);
				app.addWindow(win);
			}
		}
		
		MockRequest mockReq = new MockRequest(req);
		Map paramMap = req.getParameterMap();
		Iterator it = paramMap.entrySet().iterator();
		while(it.hasNext()){
			Entry entry = (Entry) it.next();
			String key = (String) entry.getKey();
			String[] value =(String[])entry.getValue();
			if(!key.equals(AppConsts.PARAM_WIN_ID)){
				mockReq.addParameter((String) entry.getKey() , value[0]);
			}
		}
//		mockReq.addParameter("appId", appId);
		mockReq.addParameter("pageId" , winid);
//		mockReq.addParameter(ParamConstant.PAGE_UNIQUE_ID , winid);
		mockReq.addParameter(ParamConstant.APP_UNIQUE_ID , LfwRuntimeEnvironment.getWebContext().getAppUniqueId());
		WebContext webCtx = new WebContext(mockReq);
		//设置到ThreadLocal中，以便于随时取用
		LfwRuntimeEnvironment.setWebContext(webCtx);
		try{
			AppLifeCycleContext lifeCtx = new AppLifeCycleContext();
			ApplicationContext appCtx = new ApplicationContext();
			
			String nodecode = req.getParameter(NODECODE);
			appCtx.addAppAttribute(NODECODE, nodecode);
			
			lifeCtx.setApplicationContext(appCtx);
			AppLifeCycleContext.current(lifeCtx);
			
			RequestDispatcher dispatcher = LfwRuntimeEnvironment.getServletContext().getRequestDispatcher(url);
			dispatcher.forward(mockReq, res);
//		res.sendRedirect(url);
		}
		finally{
			AppLifeCycleContext.reset();
		}
	}

}
