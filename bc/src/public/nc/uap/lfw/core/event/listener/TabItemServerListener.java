package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.TabItemEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public class TabItemServerListener extends AbstractServerListener {

	public TabItemServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onclose(TabItemEvent e){};
	
}
