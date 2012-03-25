package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.AutoformEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class AutoformServerListener extends AbstractServerListener {

	public AutoformServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public abstract void inActive(AutoformEvent e);

	public abstract void getValue(AutoformEvent e);
	
	public abstract void active(AutoformEvent e);
	
	public abstract void setValue(AutoformEvent e);

}
