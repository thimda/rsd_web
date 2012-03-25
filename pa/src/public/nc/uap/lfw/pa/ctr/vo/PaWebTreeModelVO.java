/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-25
 * @since 1.6
 */
public class PaWebTreeModelVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;

	private String pk_treemodel;
	
	private String pk_parent;
	
	private String pk_treenode;
	private String parentid;
	private String id;
	private String widgetid;

	@Override
	public String getPKFieldName() {
		return "pk_treemodel";
	}

	@Override
	public String getTableName() {
		return "pa_treemodel";
	}

	public String getPk_treemodel() {
		return pk_treemodel;
	}

	public void setPk_treemodel(String pk_treemodel) {
		this.pk_treemodel = pk_treemodel;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getPk_treenode() {
		return pk_treenode;
	}

	public void setPk_treenode(String pk_treenode) {
		this.pk_treenode = pk_treenode;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWidgetid() {
		return widgetid;
	}

	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	

}
