package nc.uap.lfw.core.app;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.event.PageEvent;

public class LfwAppDefaultController {
	public void onPageClosed(PageEvent e){
		LfwRuntimeEnvironment.getWebContext().destroyWebSession();
	}
}
