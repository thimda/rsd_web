package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.ExcelEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class ExcelServerListener extends AbstractServerListener {

	public ExcelServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onDataOuterDivContextMenu(ExcelEvent e) {};
//	public void processPageCount(ExcelEvent e) {};
}
