package nc.uap.lfw.core.cmd.base;

import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;

public abstract class UifCommand implements ICommand {
	protected AppLifeCycleContext getLifeCycleContext() {
		return AppLifeCycleContext.current();
	}
	
	protected void updateButtons() {
		ButtonStateManager.updateButtons();
	}
}
