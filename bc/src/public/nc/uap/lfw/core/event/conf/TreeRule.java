package nc.uap.lfw.core.event.conf;

import java.io.Serializable;

/**
 * Tree�ύ����
 * 
 * @author guoweic
 *
 */
public class TreeRule implements Cloneable, Serializable {

	private static final long serialVersionUID = -4616047733374430734L;
	
	//��ǰ�ڵ�
//	public static final String TYPE_CURRENT = "tree_current";
	//��ǰ�ڵ㡢���и��ڵ�
	public static final String TYPE_CURRENT_PARENT = "tree_current_parent";
	//��ǰ�ڵ㡢��ǰ�ڵ�ĵ�һ���ӽڵ�
//	public static final String TREE_CURRENT_CHILDREN = "tree_current_children";
	//��ǰ�ڵ㡢���и��ڵ㡢��ǰ�ڵ�ĵ�һ���ӽڵ�
	public static final String TREE_CURRENT_PARENT_CHILDREN = "tree_current_parent_children";
	//���нڵ�
	public static final String TREE_ALL = "tree_all";
	
//	// ��ǰ�ڵ㡢���ڵ�
//	public static final String TYPE_CURRENT_ROOT = "tree_current_root";
//	// ��ǰ�ڵ㡢���ڵ㡢���ڵ�
//	public static final String TYPE_CURRENT_PARENT_ROOT = "tree_current_parent_root";
//	// ��ǰ�ڵ㡢���ڵ㡢���ڵ㡢������
//	public static final String TYPE_CURRENT_PARENT_ROOT_TREE = "tree_current_parent_root_tree";
	
	// Tree��ID
	private String id;
	// �ύ����
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
