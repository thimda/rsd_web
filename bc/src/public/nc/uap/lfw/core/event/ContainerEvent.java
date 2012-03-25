package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.WebComponent;

/**
 * @author guoweic
 *
 */
public class ContainerEvent extends AbstractServerEvent<WebComponent> {

	public ContainerEvent(WebComponent webElement) {
		super(webElement);
	}

}
