package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.ICommand;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;

public class UifCloseViewCmd implements ICommand {
	private String viewId;
	public UifCloseViewCmd(String viewId) {
		this.viewId = viewId;
	}

	@Override
	public void execute() {
		AppLifeCycleContext.current().getWindowContext().closeViewDialog(viewId);
	}

}
