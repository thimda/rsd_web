package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;


public abstract class AbstractServerListener implements IServerListener {
	private LfwPageContext context;
	private String widgetId;
	
	public AbstractServerListener(LfwPageContext pagemeta, String widgetId) {
		this.context = pagemeta;
		this.widgetId = widgetId;
	}
	
	public WidgetContext getContext(String id) {
		return context.getWidgetContext(id);
	}

	public WidgetContext getCurrentContext() {
		return getContext(widgetId);
	}

	public LfwPageContext getGlobalContext() {
		return context;
	}
}
