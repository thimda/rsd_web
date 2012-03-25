package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.TreeRowEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class TreeRowServerListener extends AbstractServerListener {

	public TreeRowServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public abstract void beforeNodeCreate(TreeRowEvent e);
}
