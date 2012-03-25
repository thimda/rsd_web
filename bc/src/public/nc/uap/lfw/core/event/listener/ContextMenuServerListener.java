package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ContextMenuEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class ContextMenuServerListener extends AbstractServerListener {

	public ContextMenuServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	public void beforeShow(ContextMenuEvent e){};
	public void onshow(ContextMenuEvent e){};
	public void beforeClose(ContextMenuEvent e){};
	public void onclose(ContextMenuEvent e){};
	public void onmouseout(ContextMenuEvent e){};

}
