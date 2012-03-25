package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.AbstractServerEvent;

public class ScriptEvent extends AbstractServerEvent<WebElement> {

	public ScriptEvent(WebElement webElement) {
		super(webElement);
	}

}
