package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.TreeViewComp;

/**
 * @author guoweic
 *
 */
public class TreeNodeMouseEvent extends AbstractServerEvent<TreeViewComp> {

	private String nodeRowId;
	
	public TreeNodeMouseEvent(TreeViewComp webElement) {
		super(webElement);
	}

	public String getNodeRowId() {
		return nodeRowId;
	}

	public void setNodeRowId(String nodeRowId) {
		this.nodeRowId = nodeRowId;
	}

}
