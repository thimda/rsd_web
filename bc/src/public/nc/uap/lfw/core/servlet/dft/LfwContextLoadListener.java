package nc.uap.lfw.core.servlet.dft;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.servlet.LfwContextLoaderListenerBase;
import nc.uap.lfw.core.servlet.LfwServerListener;

/**
 * lfw项目自用的启动加载器
 * @author dengjt
 *
 */
public final class LfwContextLoadListener extends LfwContextLoaderListenerBase {

	protected LfwServerListener getAllServerListener(ServletContext ctx) {
		return new LfwBizServerListener(ctx);
	}

	@Override
	public void contextInitialized(ServletContextEvent ctxEvent) {
		ServletContext ctx = ctxEvent.getServletContext();
		LfwRuntimeEnvironment.setLfwPath(ctx.getRealPath("/"));
		String ctxPath = ctx.getInitParameter("ctxPath");
		LfwRuntimeEnvironment.setLfwCtx(ctxPath);
		super.contextInitialized(ctxEvent);
	}

	@Override
	protected LfwServerListener getSinglePointServerListener(ServletContext ctx) {
		return new LfwSingleServerListener(ctx);
	}
}
