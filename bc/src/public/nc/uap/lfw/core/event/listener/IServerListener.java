package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;


public interface IServerListener {
	public LfwPageContext getGlobalContext();
	public WidgetContext getCurrentContext();
	public WidgetContext getContext(String id);
}
