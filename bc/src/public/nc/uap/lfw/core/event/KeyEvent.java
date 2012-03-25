package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.text.TextComp;

public class KeyEvent extends AbstractServerEvent<TextComp> {

	public KeyEvent(TextComp webElement) {
		super(webElement);
	}

}
