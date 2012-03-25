package nc.uap.lfw.core.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import nc.bs.framework.server.BusinessAppServer;
import nc.bs.framework.server.ServerConfiguration;
import nc.uap.lfw.core.common.ApplicationConstant;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.dft.LfwDefaultWebSessionListener;
import nc.uap.lfw.util.LfwClassUtil;
/**
 * Lfw context 加载监听器。其它使用lfw的子项目模块,如果需要独立的加载监听器，必须继承此Listener以完成必要的初始化。否则必须直接使用此Listener
 * @author dengjt
 *
 */
public abstract class LfwContextLoaderListenerBase implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent ctxEvent) {
		
	}

	@SuppressWarnings("deprecation")
	public void contextInitialized(ServletContextEvent ctxEvent) {
		ServletContext ctx = ctxEvent.getServletContext();
		LfwLogger.debug("Starting LFW Application \"" + ctx.getInitParameter("ctxPath") + "\" ......");
		ctx.setAttribute(WebConstant.ROOT_PATH, ctx.getInitParameter("ctxPath"));
		//设置web应用真实路径
		String realPath = ctx.getRealPath("/");
		LfwLogger.info("The real path is:" + realPath);
		ctx.setAttribute(WebConstant.REAL_PATH, realPath);
		
		String errorPage = ctx.getInitParameter(ApplicationConstant.ERROR_PAGE);
		if(errorPage != null)
			ctx.setAttribute(ApplicationConstant.ERROR_PAGE, errorPage);
		
		final ServerConfiguration sc = ServerConfiguration.getServerConfiguration();
		//单机或者Master上，进行单点的必要初始化
		if(sc.isSingle() || sc.isMaster()) {
			LfwServerListener listener = getSinglePointServerListener(ctx);
			if(listener != null)
				BusinessAppServer.getInstance().addServerListener(listener);
		}
		
		//在所有server上，进行初始化
		LfwServerListener baseListener = new LfwDefaultBizServerListener(ctx);
		BusinessAppServer.getInstance().addServerListener(baseListener);
//		//初始化页面流
//		LfwServerListener pageFlowListener = new LfwDefaultPageFlowListener(ctx);
//		BusinessAppServer.getInstance().addServerListener(pageFlowListener);
		
		LfwServerListener listener = getAllServerListener(ctx);
		if(listener != null)
			BusinessAppServer.getInstance().addServerListener(listener);
	}
	
	protected LfwServerListener getSinglePointServerListener(ServletContext ctx){
		return null;
	}
	
	protected LfwServerListener getAllServerListener(ServletContext ctx) {
		return null;
	}
}
