package nc.uap.lfw.core.comp;

import java.io.Serializable;

/**
 * 
 * @author dengjt
 * 
 */
public class WebTreeModel implements Serializable, Cloneable {

	private static final long serialVersionUID = 925102983368848677L;

	private WebTreeNode rootNode = null;
	
	private String currentNodeId = null;
	
	private String level = null;
	
	public WebTreeModel() {
		super();
	}

	public WebTreeModel(WebTreeNode root) {
		this.rootNode = root;
	}

	public Object clone() {
		if(this.rootNode == null)
			return new WebTreeModel();
		return new WebTreeModel((WebTreeNode) this.rootNode.clone());
	}

	public WebTreeNode getRootNode() {
		return rootNode;
	}
	
	public void setRootNode(WebTreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public WebTreeNode getCurrNode() {
		if (this.currentNodeId != null && !this.currentNodeId.equals(""))
			return getTreeNodeById(this.currentNodeId);
		else
			return null;
	}

	/**
	 * 取当前节点层级数
	 * 
	 * @return
	 */
	public int getCurrNodeLevel(){
		if (this.currentNodeId == null || this.currentNodeId.equals(""))
			return -1;
		return getNodeLevel(this.currentNodeId, rootNode, 0);
		
	}
	
	private int getNodeLevel(String id, WebTreeNode node,int level){
		if (null != node && null != id && !"".equals(id)) {
			if (id.equals(node.getId()))
				return level;
			if (node.getChildNodeList() != null) {
				for (WebTreeNode subNode : node.getChildNodeList()) {
					int i = getNodeLevel(id, subNode, level + 1);
					if (i != -1)
						return i;
				}
			}
		}
		return -1;
	}

	public String getCurrentNodeId() {
		return currentNodeId;
	}

	public void setCurrentNodeId(String currentNodeId) {
		this.currentNodeId = currentNodeId;
	}

	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * 根据ID获取树节点
	 * @param id
	 * @return
	 */
	public WebTreeNode getTreeNodeById(String id) {
		return getTreeNodeById(id, rootNode);
	}
	
	/**
	 * 根据ID获取树节点
	 * @param id
	 * @param node
	 * @return
	 */
	private WebTreeNode getTreeNodeById(String id, WebTreeNode node) {
		if (null != node && null != id && !"".equals(id)) {
			if (id.equals(node.getId()))
				return node;
			if (node.getChildNodeList() != null) {
				for (WebTreeNode subNode : node.getChildNodeList()) {
					WebTreeNode resultNode = getTreeNodeById(id, subNode);
					if (null != resultNode) {
						return resultNode;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据数据行ID获取树节点
	 * @param rowId
	 * @return
	 */
	public WebTreeNode getTreeNodeByRowId(String rowId) {
		return getTreeNodeByRowId(rowId, rootNode);
	}
	
	/**
	 * 根据数据行ID获取树节点
	 * @param rowId
	 * @param node
	 * @return
	 */
	private WebTreeNode getTreeNodeByRowId(String rowId, WebTreeNode node) {
		if (null != node && null != rowId && !"".equals(rowId)) {
			if (rowId.equals(node.getRowId()))
				return node;
			if (node.getChildNodeList() != null) {
				for (WebTreeNode subNode : node.getChildNodeList()) {
					WebTreeNode resultNode = getTreeNodeByRowId(rowId, subNode);
					if (null != resultNode) {
						return resultNode;
					}
				}
			}
		}
		return null;
	}
	
}
