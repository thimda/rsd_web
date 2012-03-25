package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.LinkComp;

/**
 * @author guoweic
 *
 */
public class LinkEvent extends AbstractServerEvent<LinkComp> {

	public LinkEvent(LinkComp webElement) {
		super(webElement);
	}

}
