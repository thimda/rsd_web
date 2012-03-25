package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.RefTextEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class ReferenceTextServerListener extends
		AbstractServerListener {

	public ReferenceTextServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	public void beforeOpenReference(RefTextEvent e){};

}
