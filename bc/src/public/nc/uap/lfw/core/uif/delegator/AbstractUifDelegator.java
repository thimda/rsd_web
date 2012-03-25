package nc.uap.lfw.core.uif.delegator;

import nc.uap.lfw.core.event.ctx.LfwPageContext;

public abstract class AbstractUifDelegator implements IUifDeletator {
	private LfwPageContext globalContext;
	
	public LfwPageContext getGlobalContext() {
		return globalContext;
	}

	public void setGlobalContext(LfwPageContext ctx) {
		this.globalContext = ctx;
	}
}
