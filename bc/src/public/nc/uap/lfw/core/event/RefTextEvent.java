package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.text.TextComp;

/**
 * @author guoweic
 *
 */
public class RefTextEvent extends AbstractServerEvent<TextComp> {

	public RefTextEvent(TextComp webElement) {
		super(webElement);
	}

}
