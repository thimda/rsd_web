package nc.uap.lfw.core.event;

import nc.uap.lfw.jsp.uimeta.UITabItem;


/**
 * @author guoweic
 *
 */
public class TabItemEvent extends AbstractServerEvent<UITabItem> {

	public TabItemEvent(UITabItem webElement) {
		super(webElement);
	}

}
