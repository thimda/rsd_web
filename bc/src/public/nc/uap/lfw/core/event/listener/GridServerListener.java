package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.GridEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class GridServerListener extends AbstractServerListener {

	public GridServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onDataOuterDivContextMenu(GridEvent e) {};
//	public void processPageCount(GridEvent e) {};
}
