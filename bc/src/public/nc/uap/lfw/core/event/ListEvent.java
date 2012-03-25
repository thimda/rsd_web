package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.ListComp;

/**
 * @author guoweic
 *
 */
public class ListEvent extends AbstractServerEvent<ListComp> {

	public ListEvent(ListComp webElement) {
		super(webElement);
	}

}
