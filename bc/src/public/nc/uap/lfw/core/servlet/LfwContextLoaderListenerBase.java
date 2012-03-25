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
 * Lfw context ���ؼ�����������ʹ��lfw������Ŀģ��,�����Ҫ�����ļ��ؼ�����������̳д�Listener����ɱ�Ҫ�ĳ�ʼ�����������ֱ��ʹ�ô�Listener
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
		//����webӦ����ʵ·��
		String realPath = ctx.getRealPath("/");
		LfwLogger.info("The real path is:" + realPath);
		ctx.setAttribute(WebConstant.REAL_PATH, realPath);
		
		String errorPage = ctx.getInitParameter(ApplicationConstant.ERROR_PAGE);
		if(errorPage != null)
			ctx.setAttribute(ApplicationConstant.ERROR_PAGE, errorPage);
		
		final ServerConfiguration sc = ServerConfiguration.getServerConfiguration();
		//��������Master�ϣ����е���ı�Ҫ��ʼ��
		if(sc.isSingle() || sc.isMaster()) {
			LfwServerListener listener = getSinglePointServerListener(ctx);
			if(listener != null)
				BusinessAppServer.getInstance().addServerListener(listener);
		}
		
		//������server�ϣ����г�ʼ��
		LfwServerListener baseListener = new LfwDefaultBizServerListener(ctx);
		BusinessAppServer.getInstance().addServerListener(baseListener);
//		//��ʼ��ҳ����
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
