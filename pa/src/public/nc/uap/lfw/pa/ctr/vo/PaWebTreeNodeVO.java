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
public class PaWebTreeNodeVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_treenode;
	private String pk_parent;
	
	private String parentnode;
	private String id;
	private String label;
	private String value;
	private String rowid;
	
	private String parentid;
	private String widgetid;
	@Override
	public String getPKFieldName() {
		return "pk_treenode";
	}
	@Override
	public String getTableName() {
		return "pa_treenode";
	}
	public String getPk_treenode() {
		return pk_treenode;
	}
	public void setPk_treenode(String pk_treenode) {
		this.pk_treenode = pk_treenode;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
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
	public String getParentnode() {
		return parentnode;
	}
	public void setParentnode(String parentnode) {
		this.parentnode = parentnode;
	}
	public String getRowid() {
		return rowid;
	}
	public void setRowid(String rowid) {
		this.rowid = rowid;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}

	
	
}
