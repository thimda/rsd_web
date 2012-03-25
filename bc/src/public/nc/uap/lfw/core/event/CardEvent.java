package nc.uap.lfw.core.event;

import nc.uap.lfw.jsp.uimeta.UICardLayout;

/**
 * @author guoweic
 *
 */
public class CardEvent extends AbstractServerEvent<UICardLayout> {

	public CardEvent(UICardLayout webElement) {
		super(webElement);
	}

}
