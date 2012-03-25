package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.TreeCtxMenuEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class TreeContextMenuServerListener extends AbstractServerListener {

	public TreeContextMenuServerListener(LfwPageContext pagemeta,
			String widgetId) {
		super(pagemeta, widgetId);
	}

	public abstract void beforeContextMenu(TreeCtxMenuEvent e);
}
