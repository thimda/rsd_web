package nc.uap.lfw.core.event;

import nc.uap.lfw.core.page.LfwWidget;

/**
 * @author guoweic
 *
 */
public class DialogEvent extends AbstractServerEvent<LfwWidget> {

	public DialogEvent(LfwWidget webElement) {
		super(webElement);
	}

}
