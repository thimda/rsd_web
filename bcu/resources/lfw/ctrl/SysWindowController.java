package lfw.ctrl;

import java.io.Serializable;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctrl.WindowController;
import nc.uap.lfw.core.event.PageEvent;

public class SysWindowController implements WindowController, Serializable {
	private static final long serialVersionUID = 7532916478964732880L;
	public void sysWindowClosed(PageEvent event){
		LfwRuntimeEnvironment.getWebContext().destroyWebSession();
	}
}
