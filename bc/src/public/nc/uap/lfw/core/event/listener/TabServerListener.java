package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.TabEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class TabServerListener extends AbstractServerListener {

	public TabServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public void beforeItemInit(TabEvent e){};
	public void afterItemInit(TabEvent e){};
	public void closeItem(TabEvent e){};
	public void beforeActivedTabItemChange(TabEvent e){};
	public void afterActivedTabItemChange(TabEvent e){};
}
