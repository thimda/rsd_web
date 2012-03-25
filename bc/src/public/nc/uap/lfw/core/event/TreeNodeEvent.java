package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.TreeViewComp;

/**
 * @author guoweic
 *
 */
public class TreeNodeEvent extends AbstractServerEvent<TreeViewComp> {

	private String nodeRowId;
	private String currentdsId;
	
	public String getCurrentdsId() {
		return currentdsId;
	}

	public void setCurrentdsId(String currentdsId) {
		this.currentdsId = currentdsId;
	}

	public TreeNodeEvent(TreeViewComp webElement) {
		super(webElement);
	}

	public String getNodeRowId() {
		return nodeRowId;
	}

	public void setNodeRowId(String nodeRowId) {
		this.nodeRowId = nodeRowId;
	}

}
