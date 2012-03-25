package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.ContextMenuComp;

/**
 * @author guoweic
 *
 */
public class ContextMenuEvent extends AbstractServerEvent<ContextMenuComp> {

	public ContextMenuEvent(ContextMenuComp webElement) {
		super(webElement);
	}

}
