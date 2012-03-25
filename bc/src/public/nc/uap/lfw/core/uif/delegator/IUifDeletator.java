package nc.uap.lfw.core.uif.delegator;

import nc.uap.lfw.core.event.ctx.LfwPageContext;

public interface IUifDeletator {
	public LfwPageContext getGlobalContext();
	public void setGlobalContext(LfwPageContext ctx);
	public void execute();
}
