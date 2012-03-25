package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ContainerEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public class ContainerServerListener extends AbstractServerListener {

	public ContainerServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public void onContainerCreate(ContainerEvent e) {};

}
