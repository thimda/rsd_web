package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.text.TextComp;

/**
 * @author guoweic
 *
 */
public class TextEvent extends AbstractServerEvent<TextComp> {

	public TextEvent(TextComp webElement) {
		super(webElement);
	}

}
