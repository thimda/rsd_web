package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.TreeViewComp;

/**
 * @author guoweic
 *
 */
public class TreeNodeDragEvent extends AbstractServerEvent<TreeViewComp> {

	private String sourceNodeRowId;
	
	private String targetNodeRowId;
	
	public TreeNodeDragEvent(TreeViewComp webElement) {
		super(webElement);
	}

	public String getSourceNodeRowId() {
		return sourceNodeRowId;
	}

	public void setSourceNodeRowId(String sourceNodeRowId) {
		this.sourceNodeRowId = sourceNodeRowId;
	}

	public String getTargetNodeRowId() {
		return targetNodeRowId;
	}

	public void setTargetNodeRowId(String targetNodeRowId) {
		this.targetNodeRowId = targetNodeRowId;
	}

}
