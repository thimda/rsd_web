package nc.uap.lfw.core.servlet;

import javax.servlet.ServletContext;

import nc.bs.framework.core.ServerListener;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;

public abstract class LfwServerListener implements ServerListener {
	private ServletContext ctx;
	public LfwServerListener(ServletContext ctx) {
		this.ctx = ctx;
	}
	
	public ServletContext getCtx() {
		return ctx;
	}
	
	public void afterStarted() {
		LfwRuntimeEnvironment.setServletContext(ctx);
		LfwRuntimeEnvironment.setRootPath((String) ctx.getAttribute(WebConstant.ROOT_PATH));
		doAfterStarted();
	}

	protected abstract void doAfterStarted();
}
