package nc.uap.lfw.core.servlet.dft;

import javax.servlet.ServletContext;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.LfwServerListener;
import nc.uap.lfw.login.itf.ILfwSsoService;

public class LfwSingleServerListener extends LfwServerListener {

	public LfwSingleServerListener(ServletContext ctx) {
		super(ctx);
	}

	@Override
	protected void doAfterStarted() {
		/**
		 * 吊销登陆令牌
		 */
		try {
			if(!LfwRuntimeEnvironment.isDevelopMode())
				NCLocator.getInstance().lookup(ILfwSsoService.class).destoryAllToken();
		} catch (LfwBusinessException e) {
			LfwLogger.error("吊销登陆令牌时出现异常",e);
		}
	}

}
