package nc.uap.lfw.core.event;

import nc.uap.lfw.jsp.uimeta.UITabComp;

/**
 *
 */
public class TabEvent extends AbstractServerEvent<UITabComp> {

	public TabEvent(UITabComp uiElement) {
		super(uiElement);
	}

}
