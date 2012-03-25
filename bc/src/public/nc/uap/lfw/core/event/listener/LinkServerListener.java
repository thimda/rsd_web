package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.LinkEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class LinkServerListener extends AbstractServerListener {

	public LinkServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public abstract void onclick(LinkEvent e);

}
