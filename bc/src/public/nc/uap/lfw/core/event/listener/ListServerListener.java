package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ListEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class ListServerListener extends AbstractServerListener {

	public ListServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	public void valueChanged(ListEvent e) {};
	public void dbValueChange(ListEvent e) {};

}
