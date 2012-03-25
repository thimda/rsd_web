package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.WidgetEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

public abstract class WidgetServerListener extends AbstractServerListener {

	public WidgetServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	public abstract void beforeInitData(WidgetEvent e);

}
