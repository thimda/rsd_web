package nc.uap.lfw.core.event;

import nc.uap.lfw.core.page.LfwWidget;

/**
 * @author guoweic
 *
 */
public class WidgetEvent extends AbstractServerEvent<LfwWidget> {

	public WidgetEvent(LfwWidget webElement) {
		super(webElement);
	}

}
