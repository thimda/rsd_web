package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

public class UifApproveStateMouseListener<T extends WebElement> extends UifMouseListener<T> {

	public UifApproveStateMouseListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onclick(MouseEvent<T> e) {
		
	}

}
