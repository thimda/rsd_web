package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ExcelRowEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class ExcelRowServerListener extends AbstractServerListener {

	public ExcelRowServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void beforeRowSelected(ExcelRowEvent e){};
	public void onRowDbClick(ExcelRowEvent e){};
	public void onRowSelected(ExcelRowEvent e){};
}
