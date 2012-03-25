package nc.uap.lfw.core.comp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author dengjt
 * 
 */
public class WebTreeNode implements Serializable, Cloneable {

	private static final long serialVersionUID = 5385904440368072255L;
	private List<WebTreeNode> childNodeList = null;
	private String parentNode = null;
	private String id = null;
	private String label = null;
	private String value = null;
	private String rowId = null;

	public List<WebTreeNode> getChildNodeList() {
		return childNodeList;
	}

	public void setChildNodeList(List<WebTreeNode> childNodeList) {
		this.childNodeList = childNodeList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void addTreeNode(WebTreeNode node) {
		if (childNodeList == null)
			childNodeList = new ArrayList<WebTreeNode>();
		childNodeList.add(node);
	}

	public Object clone() {
		WebTreeNode node = new WebTreeNode();
		node.setId(this.id);
		node.setLabel(this.label);
		node.setValue(this.value);
		node.setParentNode(this.parentNode == null ? null : this.parentNode);
		if (this.childNodeList != null) {
			node.childNodeList = new ArrayList<WebTreeNode>();
			Iterator<WebTreeNode> it = this.childNodeList.iterator();
			while (it.hasNext()) {
				node.addTreeNode((WebTreeNode) it.next().clone());
			}
		}
		return node;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getParentNode() {
		return parentNode;
	}

	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}
}
