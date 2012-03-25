package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.TreeViewComp;

/**
 * @author guoweic
 *
 */
public class TreeCtxMenuEvent extends AbstractServerEvent<TreeViewComp> {

	public TreeCtxMenuEvent(TreeViewComp webElement) {
		super(webElement);
	}

}
