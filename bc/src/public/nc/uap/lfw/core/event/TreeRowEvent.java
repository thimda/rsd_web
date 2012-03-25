package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.TreeViewComp;

/**
 * @author guoweic
 *
 */
public class TreeRowEvent extends AbstractServerEvent<TreeViewComp> {

	public TreeRowEvent(TreeViewComp webElement) {
		super(webElement);
	}

}
