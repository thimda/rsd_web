package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ModifyEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class ModifyServerListener extends AbstractServerListener {

	public ModifyServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void modifyText(ModifyEvent e){};
}
