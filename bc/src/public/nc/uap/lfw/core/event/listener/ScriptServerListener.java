package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

public abstract class ScriptServerListener extends AbstractServerListener {

	public ScriptServerListener(LfwPageContext pageCtx, String widgetId) {
		super(pageCtx, widgetId);
	}
	
	public abstract void handlerEvent(ScriptEvent event);

}
