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
		 * ������½����
		 */
		try {
			if(!LfwRuntimeEnvironment.isDevelopMode())
				NCLocator.getInstance().lookup(ILfwSsoService.class).destoryAllToken();
		} catch (LfwBusinessException e) {
			LfwLogger.error("������½����ʱ�����쳣",e);
		}
	}

}
