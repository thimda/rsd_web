package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.CellEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class GridCellServerListener extends AbstractServerListener {

	public GridCellServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onCellClick(CellEvent e){};
	public void cellEdit(CellEvent e){};
	public void afterEdit(CellEvent e){};
	public void beforeEdit(CellEvent e){};
	public void cellValueChanged(CellEvent e){};
	
}
