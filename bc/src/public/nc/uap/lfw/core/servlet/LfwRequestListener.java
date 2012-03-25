package nc.uap.lfw.core.servlet;


import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.mx.thread.ThreadTracer;
import nc.bs.framework.server.RemoteCallPostProcessFactory;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.common.SessionConstant;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.login.vo.LfwSessionBean;
/**
 * ����������������󼶱����ĳ�ʼ��������.
 * TODO �˴�����param��datasource��lang code���޸ģ����һ����datasource���ù淶��
 * @author dengjt
 *
 */
public class LfwRequestListener implements ServletRequestListener {

	private static String TIME_ATTR = "$lfw_timesummary";
	public void requestInitialized(ServletRequestEvent reqEvent) {
		HttpServletRequest request = (HttpServletRequest) reqEvent.getServletRequest();
		beginRequest(request);
		//for development
		//request.getSession(true).setAttribute(WebConstant.DEBUG_MODE, "1");
		String path = request.getRequestURI();
		if(path != null && (path.endsWith(".gif") || path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".swf")))
			return;
		
		initLogger();
		
		WebContext webCtx = new WebContext(request);
		//���õ�ThreadLocal�У��Ա�����ʱȡ��
		LfwRuntimeEnvironment.setWebContext(webCtx);
		
		//����Theme��lang�Ļ�ȡ
		LfwRuntimeEnvironment.getThemeId();
		LfwRuntimeEnvironment.getLangCode();
		
		String debugMode = (String) request.getSession().getAttribute(WebConstant.DEBUG_MODE);
		//�����Դ������Ϊ����
		if(debugMode != null && debugMode.equals("1"))
			LfwRuntimeEnvironment.setMode(WebConstant.MODE_DEBUG);
		else if(debugMode != null && debugMode.equals("0"))
			LfwRuntimeEnvironment.setMode(WebConstant.MODE_PRODUCTION);
		else if(System.getProperty("nc.lfwMode") != null)
			LfwRuntimeEnvironment.setMode(System.getProperty("nc.lfwMode"));
		
		ServletContext ctx = reqEvent.getServletContext();

		LfwRuntimeEnvironment.setRealPath((String) ctx.getAttribute(WebConstant.REAL_PATH));
		LfwRuntimeEnvironment.setRootPath((String) ctx.getAttribute(WebConstant.ROOT_PATH));
		LfwRuntimeEnvironment.setCorePath((String) ctx.getAttribute(WebConstant.CORE_PATH));
		LfwRuntimeEnvironment.setFromServerName(request.getServerName());
		LfwRuntimeEnvironment.setServletContext(ctx);
		
		request.setAttribute(WebConstant.ROOT_PATH, ctx.getAttribute(WebConstant.ROOT_PATH));
		request.setAttribute(WebConstant.REAL_PATH, (String) ctx.getAttribute(WebConstant.REAL_PATH));
		request.setAttribute(WebConstant.CORE_PATH, ctx.getAttribute(WebConstant.CORE_PATH));
		request.setAttribute(WebConstant.LFW_ROOT_PATH, LfwRuntimeEnvironment.getLfwCtx());
		
		restoreEnviroment((HttpServletRequest) reqEvent.getServletRequest());
		
		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
		String pkUser = null;
		if(ses == null)
			pkUser = "annoymous";
		else
			pkUser = ses.getUser_code();
		String callId = System.currentTimeMillis() + "";
		Logger.putMDC("serial", callId);
		Logger.putMDC("user", pkUser);
		ThreadTracer.getInstance().startThreadMonitor(request.getRequestURI(), request.getRemoteAddr(), pkUser);
	}
	
	protected void beginRequest(HttpServletRequest request) {
		if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG)){
			Logger.debug("request begins:===============================================================");
			long startTime = System.currentTimeMillis();
			request.setAttribute(TIME_ATTR, Long.valueOf(startTime));
		}
	}

	/**
	 * �Ӵ洢�лָ�����������Ĭ���Ǵ�cookie�У���ʵ��ҵ��ϵͳ�Ƽ���session�лָ�
	 * @param req
	 */
	protected void restoreEnviroment(HttpServletRequest req) {
		LfwSessionBean bean = (LfwSessionBean) req.getSession().getAttribute(SessionConstant.LOGIN_SESSION_BEAN);
		if(bean != null){
			LfwRuntimeEnvironment.setDatasource(bean.getDatasource());
			bean.fireLocalEnvironment();
		}
		LfwRuntimeEnvironment.setLfwSessionBean(bean);
	}
	
	
	public void requestDestroyed(ServletRequestEvent reqEvent) {
		HttpServletRequest request = (HttpServletRequest) reqEvent.getServletRequest();
		String path = request.getRequestURI();
		if(path != null && (path.endsWith(".gif") || path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".swf")))
			return;
		
		ThreadTracer.getInstance().endThreadMonitor();
		resetLogger();
		if(LfwRuntimeEnvironment.getWebContext()==null){
			return ;
		}

//		WebSession ses = LfwRuntimeEnvironment.getWebContext().getWebSession(false);
//		if(ses != null)
//			ses.flush();
		//���õ�ǰ�߳��еı���
		LfwRuntimeEnvironment.reset();
		
		RemoteCallPostProcessFactory remoteProcessor = (RemoteCallPostProcessFactory) NCLocator.getInstance().lookup("RemoteProcessComponetFactory");
		remoteProcessor.postProcess();
		
		endRequest(request);
	}
	
	protected void endRequest(HttpServletRequest request) {
		if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG)){
			long endTime = System.currentTimeMillis();
			Long startTime = (Long) request.getAttribute(TIME_ATTR);
			if(startTime != null)
				Logger.debug("request end:===============================================================\nrequest time:" + (endTime - startTime) + "\nrequest uri" + request.getRequestURI());
		}
	}
	/**
	 * ��ʼ��Logger
	 * ����Ӧ�ø���ʵ�������д�˷���,�Ա��¼��־���ض��ļ�������Portal
	 *
	 */
	protected void initLogger() {
		Logger.init("nclfw");
	}
	
	/**
	 * ����Logger
	 *
	 */
	protected void resetLogger() {
		Logger.reset();
	}

}
