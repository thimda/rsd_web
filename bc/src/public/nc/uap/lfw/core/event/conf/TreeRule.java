package nc.uap.lfw.core.event.conf;

import java.io.Serializable;

/**
 * Tree提交类型
 * 
 * @author guoweic
 *
 */
public class TreeRule implements Cloneable, Serializable {

	private static final long serialVersionUID = -4616047733374430734L;
	
	//当前节点
//	public static final String TYPE_CURRENT = "tree_current";
	//当前节点、所有父节点
	public static final String TYPE_CURRENT_PARENT = "tree_current_parent";
	//当前节点、当前节点的第一级子节点
//	public static final String TREE_CURRENT_CHILDREN = "tree_current_children";
	//当前节点、所有父节点、当前节点的第一级子节点
	public static final String TREE_CURRENT_PARENT_CHILDREN = "tree_current_parent_children";
	//所有节点
	public static final String TREE_ALL = "tree_all";
	
//	// 当前节点、根节点
//	public static final String TYPE_CURRENT_ROOT = "tree_current_root";
//	// 当前节点、父节点、根节点
//	public static final String TYPE_CURRENT_PARENT_ROOT = "tree_current_parent_root";
//	// 当前节点、父节点、根节点、树本身
//	public static final String TYPE_CURRENT_PARENT_ROOT_TREE = "tree_current_parent_root_tree";
	
	// Tree的ID
	private String id;
	// 提交类型
	private String type = TYPE_CURRENT_PARENT;

	public Object clone(){
		TreeRule treeRule = new TreeRule();
		treeRule.setId(id);
		treeRule.setType(type);
		return treeRule;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
