package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ExcelCellEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class ExcelCellServerListener extends AbstractServerListener {

	public ExcelCellServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onCellClick(ExcelCellEvent e){};
	public void cellEdit(ExcelCellEvent e){};
	public void afterEdit(ExcelCellEvent e){};
	public void beforeEdit(ExcelCellEvent e){};
	public void cellValueChanged(ExcelCellEvent e){};
	
}
