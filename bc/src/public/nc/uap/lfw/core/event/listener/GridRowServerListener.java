package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.GridRowEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class GridRowServerListener extends AbstractServerListener {

	public GridRowServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void beforeRowSelected(GridRowEvent e){};
	public void onRowDbClick(GridRowEvent e){};
	public void onRowSelected(GridRowEvent e){};
}
