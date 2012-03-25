package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public class UifQueryTemplateCancelMouseListener<T extends WebElement> extends UifMouseListener<T> {

	public UifQueryTemplateCancelMouseListener(LfwPageContext pagemeta,
			String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onclick(MouseEvent<T> e) {
		getGlobalContext().getParentGlobalContext().hideCurrentDialog();
	}
}
