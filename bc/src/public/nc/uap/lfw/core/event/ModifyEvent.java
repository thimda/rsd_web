package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.WebElement;

public class ModifyEvent extends AbstractServerEvent{

	public ModifyEvent(WebElement webElement) {
		super(webElement);
	}

}
